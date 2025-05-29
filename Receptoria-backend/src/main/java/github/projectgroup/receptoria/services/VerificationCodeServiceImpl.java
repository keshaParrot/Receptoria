package github.projectgroup.receptoria.services;

import github.projectgroup.receptoria.model.enities.User;
import github.projectgroup.receptoria.model.enities.VerificationCode;
import github.projectgroup.receptoria.model.enums.SendTo;
import github.projectgroup.receptoria.model.enums.VerificationCodeType;
import github.projectgroup.receptoria.repositories.UserRepository;
import github.projectgroup.receptoria.repositories.VerificationCodeRepository;
import github.projectgroup.receptoria.services.interfaces.VerificationCodeService;
import github.projectgroup.receptoria.utils.result.InvalidVerificationCode;
import github.projectgroup.receptoria.utils.result.Result;
import github.projectgroup.receptoria.utils.result.SuccessCase;
import github.projectgroup.receptoria.utils.result.UserNotFoundCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private final VerificationCodeRepository verificationCodeRepository;
    private final UserRepository userRepository;

    @Override
    public Result<VerificationCode> generateVerificationCode(String email, SendTo channel, VerificationCodeType codeType) {
        Optional<User> user = userRepository.findByEmail(email);
        String token = codeType == VerificationCodeType.VERIFICATION_MAIL
                ? generateVerificationCode()
                : UUID.randomUUID().toString();

        if (user.isPresent()) {
            VerificationCode code = VerificationCode.builder()
                    .code(token)
                    .generatedFor(user.get())
                    .type(codeType)
                    .channel(channel)
                    .expirationDate(LocalDateTime.now().plusMonths(1))
                    .build();

            if (codeType==VerificationCodeType.RESET_PASSWORD || codeType==VerificationCodeType.RESET_MAIL) {
                code.setExpirationDate(LocalDateTime.now().plusHours(8));
            }

            return Result.success(
                    verificationCodeRepository.save(code),
                    new SuccessCase(null)
            );
        }
        return Result.failure(new UserNotFoundCase(email));
    }

    @Override
    public Result<VerificationCode> verifyVerificationCode(String code, VerificationCodeType codeType) {
        Optional<VerificationCode> optVerificationCode = verificationCodeRepository.findByCode(code);

        if (optVerificationCode.isPresent() &&
                optVerificationCode.get().getExpirationDate().isAfter(LocalDateTime.now())) {

            VerificationCode verificationCode = optVerificationCode.get();
            if (verificationCode.getType().equals(codeType) ){
                return Result.success(optVerificationCode.get(), new SuccessCase("code is valid"));
            }
            return Result.failure(new InvalidVerificationCode());
        }
        return Result.failure(new InvalidVerificationCode());
    }

    @Override
    public void deleteById(Long id){
        verificationCodeRepository.deleteById(id);
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}
