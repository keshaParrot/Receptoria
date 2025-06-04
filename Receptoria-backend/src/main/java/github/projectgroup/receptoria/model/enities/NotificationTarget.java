package github.projectgroup.receptoria.model.enities;

import github.projectgroup.receptoria.model.enums.NotificationType;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Value;
import lombok.*;

import static github.projectgroup.receptoria.model.enums.NotificationType.*;

@Embeddable
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationTarget {

    private Long objectId;
    private NotificationType type;
}
