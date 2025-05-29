package github.projectgroup.receptoria.services;

import github.projectgroup.receptoria.model.enums.SendTo;
import github.projectgroup.receptoria.services.interfaces.DeliveryChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailChannelImpl implements DeliveryChannel {


    @Override
    public void sendMail(String subject, String body, String to) {
        int a = 5;
    }

    @Override
    public boolean suppostChannel(SendTo sendTo) {
        return sendTo.toString().equals(sendTo.toString());
    }
}
