package github.projectgroup.receptoria.services;

import github.projectgroup.receptoria.model.enities.VerificationCode;
import github.projectgroup.receptoria.model.enums.VerificationCodeType;
import github.projectgroup.receptoria.services.interfaces.DeliveryChannelResolver;
import github.projectgroup.receptoria.services.interfaces.NotificationService;
import github.projectgroup.receptoria.utils.Templates.mail.EmailConfirmationHtmlTemplatesImpl;
import github.projectgroup.receptoria.utils.Templates.mail.PasswordResetHtmlTemplatesImpl;
import github.projectgroup.receptoria.utils.Templates.mail.MailHtmlTemplates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final DeliveryChannelResolver resolver;

    @Override
    public void sendVerificationCode(VerificationCode code, String email) {
        MailHtmlTemplates templates;
        if (code.getType()== VerificationCodeType.VERIFICATION_MAIL){
            templates = new EmailConfirmationHtmlTemplatesImpl();
        }
        else{
            templates = new PasswordResetHtmlTemplatesImpl();
        }

        resolver.sendNotification(
                templates.getSubject(),
                templates.getResetBody(code.getCode()),
                email,
                code.getChannel()
        );
    }
}
