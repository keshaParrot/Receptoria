package github.projectgroup.receptoria.services.interfaces;

public interface DeliveryChannel {
    void sendMail(String subject, String body, String to);
}
