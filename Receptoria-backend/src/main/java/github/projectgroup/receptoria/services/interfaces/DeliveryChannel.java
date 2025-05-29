package github.projectgroup.receptoria.services.interfaces;

import github.projectgroup.receptoria.model.enums.SendTo;

public interface DeliveryChannel {
    void sendMail(String subject, String body, String to);
    boolean suppostChannel(SendTo sendTo);
}
