package github.projectgroup.receptoria.services.interfaces;

import github.projectgroup.receptoria.model.dtos.NotificationDTO;
import github.projectgroup.receptoria.model.enities.Notification;
import github.projectgroup.receptoria.model.enities.VerificationCode;
import github.projectgroup.receptoria.model.enums.SendTo;

public interface DeliveryChannelResolver {

    void sendNotification(String subject,
                          String body,
                          String to,
                          SendTo sendTo);
}
