package github.projectgroup.receptoria.services;

import github.projectgroup.receptoria.model.dtos.ChangePasswordRequest;
import github.projectgroup.receptoria.model.dtos.LoginRequest;
import github.projectgroup.receptoria.model.dtos.UserRegisterRequest;
import github.projectgroup.receptoria.model.dtos.ValidationErrorResponse;
import github.projectgroup.receptoria.model.enities.User;
import github.projectgroup.receptoria.model.enities.VerificationCode;
import github.projectgroup.receptoria.model.enums.SendTo;
import github.projectgroup.receptoria.model.enums.VerificationCodeType;
import github.projectgroup.receptoria.repositories.UserRepository;
import github.projectgroup.receptoria.security.JwtUtil;
import github.projectgroup.receptoria.services.interfaces.AuthService;
import github.projectgroup.receptoria.services.interfaces.NotificationService;
import github.projectgroup.receptoria.services.interfaces.VerificationCodeService;
import github.projectgroup.receptoria.utils.result.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final VerificationCodeService verificationCodeService;
    private final NotificationService notificationService;
    private final JwtUtil jwtUtil;

    @Override
    public Result<Void> registerUser(UserRegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return Result.failure(new BadArgumentsCase("Username is already taken"));
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            return Result.failure(new BadArgumentsCase("Email is already in use"));
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setVerified(false);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        sendVerificationMailCode(user.getEmail());

        return Result.success(null,new SuccessCase("User successfully registered"));
    }

    @Override
    public void sendVerificationMailCode(String email){
        Result<VerificationCode> codeResult = verificationCodeService.generateVerificationCode(
                email,
                SendTo.EMAIL,
                VerificationCodeType.VERIFICATION_MAIL);
        notificationService.sendVerificationCode(codeResult.getValue(),email);
    }

    @Override
    public Result<String> authenticateUser(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        String token = jwtUtil.generateToken(request.getUsername());
        return Result.success(token,new SuccessCase("User authenticated successfully"));
    }

    @Override
    public Result<?> sendResetPasswordRequest(String email){
        Result<VerificationCode> codeResult = verificationCodeService.generateVerificationCode(email, SendTo.EMAIL, VerificationCodeType.RESET_PASSWORD);
        if (!codeResult.isSuccess()){
            return codeResult;
        }
        notificationService.sendVerificationCode(codeResult.getValue(),email);
        return Result.success(null, new SuccessCase("Verification code sent"));
    }

    @Override
    public Result<Void> validateMail(String token){
        Result<VerificationCode> result = verificationCodeService.verifyVerificationCode(token,VerificationCodeType.VERIFICATION_MAIL);
        if (result.isSuccess() && userRepository.existsById(result.getValue().getId())) {
            User user = userRepository.findById(result.getValue().getId()).get();
            user.setVerified(true);
            userRepository.save(user);
            return Result.success(null, new SuccessCase("User verified successfully"));
        }
        return Result.failure(result.getResultCase());
    }

    @Override
    public Result<Void> changePassword(String token, ChangePasswordRequest request) {
        Result<VerificationCode> codeResult = verificationCodeService.verifyVerificationCode(token, VerificationCodeType.RESET_PASSWORD);

        long userId = codeResult.getValue().getGeneratedFor().getId();
        long codeId = codeResult.getValue().getId();

        if (codeResult.isSuccess()) {
            if (userRepository.existsById(userId)) {
                User user = userRepository.findById(userId).get();
                user.setPassword(passwordEncoder.encode(request.getNewPassword()));
                userRepository.save(user);
                verificationCodeService.deleteById(codeId);
                return Result.success(null,new SuccessCase("Password has been changed"));
            }
            return Result.failure(new UserNotFoundCase(userId));
        }
        return Result.failure(new InvalidVerificationCode());
    }
}
