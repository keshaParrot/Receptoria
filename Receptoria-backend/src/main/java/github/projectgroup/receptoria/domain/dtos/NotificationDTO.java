package github.projectgroup.receptoria.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import github.projectgroup.receptoria.domain.enums.NotificationType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {
    private Long id;
    private Long userId;
    private NotificationType type;
    private Long relatedObjectId;
    private String content;
    private LocalDateTime createdAt;
}
