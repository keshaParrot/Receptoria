package github.projectgroup.receptoria.model.enities;

import github.projectgroup.receptoria.model.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification implements Ownable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private NotificationTarget target;
    private Boolean isSent = false;
    private LocalDateTime createdAt;
    private LocalDateTime lastInteraction;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "notification_initiators",
            joinColumns = @JoinColumn(name = "notification_id"),
            inverseJoinColumns = @JoinColumn(name = "initiator_user_id")
    )
    private Set<User> initiators = new HashSet<>();

    @Override
    public Long getOwnerIdentifier() {
        return user.getId();
    }
}

