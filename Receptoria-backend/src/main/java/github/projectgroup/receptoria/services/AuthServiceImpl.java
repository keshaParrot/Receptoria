package github.projectgroup.receptoria.services;

import github.projectgroup.receptoria.model.dtos.LoginRequest;
import github.projectgroup.receptoria.model.dtos.UserDTO;
import github.projectgroup.receptoria.model.dtos.UserRegisterRequest;
import github.projectgroup.receptoria.model.enities.User;
import github.projectgroup.receptoria.repositories.UserRepository;
import github.projectgroup.receptoria.security.JwtUtil;
import github.projectgroup.receptoria.services.interfaces.AuthService;
import github.projectgroup.receptoria.utils.result.BadArgumentsCase;
import github.projectgroup.receptoria.utils.result.Result;
import github.projectgroup.receptoria.utils.result.SuccessCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
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
        user.setVerified(true); //TODO later change on false
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
        return Result.success(null,new SuccessCase("User successfully registered"));
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
}
