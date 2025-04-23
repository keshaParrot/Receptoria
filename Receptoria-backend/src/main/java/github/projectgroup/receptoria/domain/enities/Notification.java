package github.projectgroup.receptoria.domain.enities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private github.projectgroup.receptoria.domain.enums.NotificationType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id")
    private NotificationTarget target;

    private Long relatedObjectId;
    private int counter;
    private boolean isSent;
    private LocalDateTime createdAt;
    private LocalDateTime lastInteraction;
}

