package github.projectgroup.receptoria.repositories;

import github.projectgroup.receptoria.model.enities.Notification;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
      SELECT n 
        FROM Notification n
       WHERE n.user.id = :userId
         AND n.target.objectId = :objectId
         AND n.lastInteraction > :thresholdWindow
         AND n.createdAt > :thresholdMaxAge
       ORDER BY n.lastInteraction DESC
    """)
    Optional<Notification> findActiveNotification(
            @Param("userId") Long userId,
            @Param("objectId") Long objectId,
            @Param("thresholdWindow") LocalDateTime thresholdWindow,
            @Param("thresholdMaxAge") LocalDateTime thresholdMaxAge
    );

    Page<Notification> findByUserId(Long userId, Pageable pageable);
}
