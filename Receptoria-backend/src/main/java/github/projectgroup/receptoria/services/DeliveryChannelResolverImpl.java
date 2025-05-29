package github.projectgroup.receptoria.services;

import github.projectgroup.receptoria.model.enums.SendTo;
import github.projectgroup.receptoria.services.interfaces.DeliveryChannel;
import github.projectgroup.receptoria.services.interfaces.DeliveryChannelResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryChannelResolverImpl implements DeliveryChannelResolver {
    private final List<DeliveryChannel> deliveryChannels;

    @Override
    public void sendNotification(String subject,
                                 String body,
                                 String to,
                                 SendTo sendTo) {

        for(DeliveryChannel channel : deliveryChannels) {
            if (channel.suppostChannel(sendTo)) {
                channel.sendMail(
                        subject,
                        body,
                        to
                );
            }
        }
    }
}
