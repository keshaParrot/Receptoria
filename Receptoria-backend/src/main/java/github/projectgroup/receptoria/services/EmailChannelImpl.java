package github.projectgroup.receptoria.services;

import github.projectgroup.receptoria.model.enums.SendTo;
import github.projectgroup.receptoria.services.interfaces.DeliveryChannel;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import static java.awt.SystemColor.text;

@Service
@RequiredArgsConstructor
public class EmailChannelImpl implements DeliveryChannel {

    private final JavaMailSender mailSender;

    @Override
    public void sendMail(String subject, String htmlBody, String to) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("receptoriahelp@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send HTML email", e);
        }
    }

    @Override
    public boolean suppostChannel(SendTo sendTo) {
        return sendTo.toString().equals(sendTo.toString());
    }
}
