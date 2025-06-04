package github.projectgroup.receptoria.model.mappers;

import github.projectgroup.receptoria.model.dtos.NotificationDTO;
import github.projectgroup.receptoria.model.dtos.UserPreviewDTO;
import github.projectgroup.receptoria.model.enities.Notification;
import github.projectgroup.receptoria.model.enities.NotificationTarget;
import github.projectgroup.receptoria.model.enities.User;
import github.projectgroup.receptoria.model.enums.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NotificationMapperTest {

    private NotificationMapper mapper;

    @BeforeEach
    void setUp() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("github.projectgroup.receptoria.model.mappers");
        context.refresh();
        mapper = context.getBean(NotificationMapper.class);
    }

    @Test
    void testToDto_AllFieldsPopulated() {
        User owner = User.builder()
                .id(10L)
                .firstName("OwnerFirst")
                .lastName("OwnerLast")
                .build();
        NotificationTarget target = NotificationTarget.builder()
                .type(NotificationType.NEW_REACTION)
                .objectId(99L)
                .build();
        User initiator1 = User.builder()
                .id(1L)
                .firstName("Init1First")
                .lastName("Init1Last")
                .build();
        User initiator2 = User.builder()
                .id(2L)
                .firstName("Init2First")
                .lastName("Init2Last")
                .build();
        Set<User> initiators = new HashSet<>();
        initiators.add(initiator1);
        initiators.add(initiator2);
        LocalDateTime now = LocalDateTime.now();
        Notification notification = Notification.builder()
                .id(1L)
                .target(target)
                .createdAt(now)
                .user(owner)
                .initiators(initiators)
                .build();

        NotificationDTO dto = mapper.toDto(notification);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getUserId());
        assertEquals(NotificationType.NEW_REACTION, dto.getType());
        assertEquals(99L, dto.getRelatedObjectId());
        assertEquals(now, dto.getCreatedAt());
        assertNull(dto.getContent());
        List<UserPreviewDTO> previewList = dto.getInitiators();
        assertNotNull(previewList);
        assertEquals(2, previewList.size());
        assertTrue(previewList.stream().anyMatch(u ->
                u.getId().equals(1L) &&
                        "Init1First".equals(u.getFirstName()) &&
                        "Init1Last".equals(u.getLastName())));
        assertTrue(previewList.stream().anyMatch(u ->
                u.getId().equals(2L) &&
                        "Init2First".equals(u.getFirstName()) &&
                        "Init2Last".equals(u.getLastName())));
    }

    @Test
    void testToDto_EmptyInitiators() {
        User owner = User.builder()
                .id(5L)
                .firstName("A")
                .lastName("B")
                .build();
        NotificationTarget target = NotificationTarget.builder()
                .type(NotificationType.NEW_FOLLOWER)
                .objectId(7L)
                .build();
        Notification notification = Notification.builder()
                .id(2L)
                .target(target)
                .createdAt(LocalDateTime.of(2025, 6, 1, 12, 0))
                .user(owner)
                .initiators(new HashSet<>())
                .build();

        NotificationDTO dto = mapper.toDto(notification);

        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals(5L, dto.getUserId());
        assertEquals(NotificationType.NEW_FOLLOWER, dto.getType());
        assertEquals(7L, dto.getRelatedObjectId());
        assertEquals(LocalDateTime.of(2025, 6, 1, 12, 0), dto.getCreatedAt());
        assertNotNull(dto.getInitiators());
        assertTrue(dto.getInitiators().isEmpty());
    }

    @Test
    void testToDto_NullInitiators() {
        User owner = User.builder().id(3L).build();
        NotificationTarget target = NotificationTarget.builder()
                .type(NotificationType.NEW_RECIPE_FROM_FOLLOWED_USER)
                .objectId(15L)
                .build();
        Notification notification = Notification.builder()
                .id(3L)
                .target(target)
                .createdAt(LocalDateTime.of(2025, 1, 1, 0, 0))
                .user(owner)
                .initiators(null)
                .build();

        NotificationDTO dto = mapper.toDto(notification);

        assertNotNull(dto);
        assertEquals(3L, dto.getId());
        assertEquals(3L, dto.getUserId());
        assertEquals(NotificationType.NEW_RECIPE_FROM_FOLLOWED_USER, dto.getType());
        assertEquals(15L, dto.getRelatedObjectId());
        assertEquals(LocalDateTime.of(2025, 1, 1, 0, 0), dto.getCreatedAt());
        assertNull(dto.getInitiators());
    }

    @Test
    void testToDto_NullTarget() {
        User owner = User.builder()
                .id(8L)
                .firstName("X")
                .lastName("Y")
                .build();
        Notification notification = Notification.builder()
                .id(4L)
                .target(null)
                .createdAt(LocalDateTime.of(2024, 12, 31, 23, 59))
                .user(owner)
                .initiators(new HashSet<>())
                .build();

        NotificationDTO dto = mapper.toDto(notification);

        assertNotNull(dto);
        assertEquals(4L, dto.getId());
        assertEquals(8L, dto.getUserId());
        assertNull(dto.getType());
        assertNull(dto.getRelatedObjectId());
        assertEquals(LocalDateTime.of(2024, 12, 31, 23, 59), dto.getCreatedAt());
        assertNotNull(dto.getInitiators());
        assertTrue(dto.getInitiators().isEmpty());
    }

    @Test
    void testToDto_NullNotification() {
        assertNull(mapper.toDto(null));
    }
}
