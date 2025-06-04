package github.projectgroup.receptoria.services.interfaces;

import github.projectgroup.receptoria.model.enities.VerificationCode;
import github.projectgroup.receptoria.model.enums.SendTo;
import github.projectgroup.receptoria.model.enums.VerificationCodeType;
import github.projectgroup.receptoria.utils.result.Result;

public interface VerificationCodeService {

    Result<VerificationCode> generateVerificationCode(String email, SendTo channel, VerificationCodeType codeType);
    Result<VerificationCode> verifyVerificationCode(String code, VerificationCodeType codeType);

    void deleteById(Long id);
}
