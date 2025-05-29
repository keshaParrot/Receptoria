package github.projectgroup.receptoria.services.interfaces;

import github.projectgroup.receptoria.model.enities.VerificationCode;
import github.projectgroup.receptoria.model.enums.SendTo;

public interface NotificationService {

    void sendVerificationCode(VerificationCode code, String email);
}
