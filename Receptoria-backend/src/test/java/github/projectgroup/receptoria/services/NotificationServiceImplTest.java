package github.projectgroup.receptoria.services;

import github.projectgroup.receptoria.model.dtos.NotificationDTO;
import github.projectgroup.receptoria.model.enities.*;
import github.projectgroup.receptoria.model.enums.NotificationType;
import github.projectgroup.receptoria.model.mappers.NotificationMapper;
import github.projectgroup.receptoria.repositories.NotificationRepository;
import github.projectgroup.receptoria.repositories.UserRepository;
import github.projectgroup.receptoria.services.interfaces.DeliveryChannelResolver;
import github.projectgroup.receptoria.utils.result.NotificationNotFoundCase;
import github.projectgroup.receptoria.utils.result.Result;
import github.projectgroup.receptoria.utils.result.SuccessCase;
import github.projectgroup.receptoria.utils.result.UserNotFoundCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock private DeliveryChannelResolver resolver;
    @Mock private NotificationRepository notificationRepository;
    @Mock private NotificationMapper notificationMapper;
    @Mock private UserRepository userRepository;

    @InjectMocks private NotificationServiceImpl service;
    @Captor private ArgumentCaptor<Notification> notificationCaptor;

    @Test
    void testGetById_Found() {
        Notification notification = Notification.builder().id(1L).build();
        NotificationDTO dto = new NotificationDTO();
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        when(notificationMapper.toDto(notification)).thenReturn(dto);

        Result<NotificationDTO> result = service.getById(1L);

        assertTrue(result.isSuccess());
        assertEquals(dto, result.getValue());
        assertTrue(result.isCaseInstanceOf(SuccessCase.class));
    }

    @Test
    void testGetById_NotFound() {
        when(notificationRepository.findById(2L)).thenReturn(Optional.empty());

        Result<NotificationDTO> result = service.getById(2L);

        assertFalse(result.isSuccess());
        assertTrue(result.isCaseInstanceOf(NotificationNotFoundCase.class));
    }

    @Test
    void testGetAllByUserId_ReturnsPage() {
        Notification n1 = Notification.builder().id(1L).build();
        Notification n2 = Notification.builder().id(2L).build();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Page<Notification> page = new PageImpl<>(List.of(n1, n2), pageable, 2);
        NotificationDTO dto1 = new NotificationDTO();
        NotificationDTO dto2 = new NotificationDTO();
        when(notificationRepository.findByUserId(5L, pageable)).thenReturn(page);
        when(notificationMapper.toDto(n1)).thenReturn(dto1);
        when(notificationMapper.toDto(n2)).thenReturn(dto2);

        Page<NotificationDTO> result = service.getAllByUserId(5L, pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals(dto1, result.getContent().get(0));
        assertEquals(dto2, result.getContent().get(1));
    }

    @Test
    void testNotifyAboutReaction_TargetMissing() {
        User initiator = User.builder().id(20L).build();
        Reaction reaction = Reaction.builder().id(30L).owner(initiator).build();
        when(userRepository.findById(10L)).thenReturn(Optional.empty());

        Result<Void> result = service.notifyAboutReaction(reaction, 10L);

        assertFalse(result.isSuccess());
        assertTrue(result.isCaseInstanceOf(UserNotFoundCase.class));
        verify(notificationRepository, never()).save(any());
    }

    @Test
    void testNotifyAboutReaction_InitiatorMissing() {
        User targetUser = User.builder().id(10L).build();
        Reaction reaction = Reaction.builder().id(30L).owner(User.builder().id(20L).build()).build();
        when(userRepository.findById(10L)).thenReturn(Optional.of(targetUser));
        when(userRepository.findById(20L)).thenReturn(Optional.empty());

        Result<Void> result = service.notifyAboutReaction(reaction, 10L);

        assertFalse(result.isSuccess());
        assertTrue(result.isCaseInstanceOf(UserNotFoundCase.class));
        verify(notificationRepository, never()).save(any());
    }

    @Test
    void testNotifyAboutReaction_Success_ExistingNotification() {
        User targetUser = User.builder().id(10L).build();
        User initiator = User.builder().id(20L).build();
        Reaction reaction = Reaction.builder().id(30L).owner(initiator).build();

        when(userRepository.findById(10L)).thenReturn(Optional.of(targetUser));
        when(userRepository.findById(20L)).thenReturn(Optional.of(initiator));

        // Simulate existing notification with non-null initiators
        Notification existing = Notification.builder()
                .id(100L)
                .user(targetUser)
                .target(NotificationTarget.builder()
                        .objectId(30L)
                        .type(NotificationType.NEW_REACTION)
                        .build())
                .createdAt(LocalDateTime.now().minusMinutes(5))
                .lastInteraction(LocalDateTime.now().minusMinutes(5))
                .initiators(new HashSet<>())
                .build();
        when(notificationRepository.findActiveNotification(
                eq(10L), eq(30L),
                any(LocalDateTime.class),
                any(LocalDateTime.class)))
                .thenReturn(Optional.of(existing));
        when(notificationRepository.save(any(Notification.class))).thenAnswer(inv -> inv.getArgument(0));

        Result<Void> result = service.notifyAboutReaction(reaction, 10L);

        assertTrue(result.isSuccess());
        verify(notificationRepository).save(notificationCaptor.capture());
        Notification saved = notificationCaptor.getValue();
        assertEquals(10L, saved.getUser().getId());
        assertEquals(NotificationType.NEW_REACTION, saved.getTarget().getType());
        assertEquals(30L, saved.getTarget().getObjectId());
    }

    @Test
    void testNotifyAboutReaction_ThrowsOnNewNotification() {
        User targetUser = User.builder().id(10L).build();
        User initiator = User.builder().id(20L).build();
        Reaction reaction = Reaction.builder().id(30L).owner(initiator).build();

        when(userRepository.findById(10L)).thenReturn(Optional.of(targetUser));
        when(userRepository.findById(20L)).thenReturn(Optional.of(initiator));
        when(notificationRepository.findActiveNotification(
                eq(10L), eq(30L),
                any(LocalDateTime.class),
                any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () ->
                service.notifyAboutReaction(reaction, 10L)
        );
    }

    @Test
    void testNotifyAboutFollow_TargetMissing() {
        User initiator = User.builder().id(8L).build();
        Follow follow = Follow.builder()
                .id(40L)
                .follower(initiator)
                .followed(User.builder().id(7L).build())
                .build();
        when(userRepository.findById(7L)).thenReturn(Optional.empty());

        Result<Void> result = service.notifyAboutFollow(follow, 7L);

        assertFalse(result.isSuccess());
        assertTrue(result.isCaseInstanceOf(UserNotFoundCase.class));
        verify(notificationRepository, never()).save(any());
    }

    @Test
    void testNotifyAboutFollow_InitiatorMissing() {
        User target = User.builder().id(7L).build();
        Follow follow = Follow.builder()
                .id(40L)
                .follower(User.builder().id(8L).build())
                .followed(target)
                .build();
        when(userRepository.findById(7L)).thenReturn(Optional.of(target));
        when(userRepository.findById(8L)).thenReturn(Optional.empty());

        Result<Void> result = service.notifyAboutFollow(follow, 7L);

        assertFalse(result.isSuccess());
        assertTrue(result.isCaseInstanceOf(UserNotFoundCase.class));
        verify(notificationRepository, never()).save(any());
    }

    @Test
    void testNotifyAboutFollow_Success_ExistingNotification() {
        User target = User.builder().id(7L).build();
        User initiator = User.builder().id(8L).build();
        Follow follow = Follow.builder()
                .id(40L)
                .follower(initiator)
                .followed(target)
                .build();

        when(userRepository.findById(7L)).thenReturn(Optional.of(target));
        when(userRepository.findById(8L)).thenReturn(Optional.of(initiator));

        Notification existing = Notification.builder()
                .id(101L)
                .user(target)
                .target(NotificationTarget.builder()
                        .objectId(40L)
                        .type(NotificationType.NEW_FOLLOWER)
                        .build())
                .createdAt(LocalDateTime.now().minusMinutes(5))
                .lastInteraction(LocalDateTime.now().minusMinutes(5))
                .initiators(new HashSet<>())
                .build();
        when(notificationRepository.findActiveNotification(
                eq(7L), eq(40L),
                any(LocalDateTime.class),
                any(LocalDateTime.class)))
                .thenReturn(Optional.of(existing));
        when(notificationRepository.save(any(Notification.class))).thenAnswer(inv -> inv.getArgument(0));

        Result<Void> result = service.notifyAboutFollow(follow, 7L);

        assertTrue(result.isSuccess());
        verify(notificationRepository).save(notificationCaptor.capture());
        Notification saved = notificationCaptor.getValue();
        assertEquals(7L, saved.getUser().getId());
        assertEquals(NotificationType.NEW_FOLLOWER, saved.getTarget().getType());
        assertEquals(40L, saved.getTarget().getObjectId());
    }

    @Test
    void testNotifyAboutFollow_ThrowsOnNewNotification() {
        User target = User.builder().id(7L).build();
        User initiator = User.builder().id(8L).build();
        Follow follow = Follow.builder()
                .id(40L)
                .follower(initiator)
                .followed(target)
                .build();

        when(userRepository.findById(7L)).thenReturn(Optional.of(target));
        when(userRepository.findById(8L)).thenReturn(Optional.of(initiator));
        when(notificationRepository.findActiveNotification(
                eq(7L), eq(40L),
                any(LocalDateTime.class),
                any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () ->
                service.notifyAboutFollow(follow, 7L)
        );
    }

    @Test
    void testNotifyAboutRecipeSaved_TargetMissing() {
        UserRecipe recipe = UserRecipe.builder()
                .id(9L)
                .owner(User.builder().id(4L).build())
                .build();
        when(userRepository.findById(3L)).thenReturn(Optional.empty());

        Result<Void> result = service.notifyAboutRecipeSaved(recipe, 3L);

        assertFalse(result.isSuccess());
        assertTrue(result.isCaseInstanceOf(UserNotFoundCase.class));
        verify(notificationRepository, never()).save(any());
    }

    @Test
    void testNotifyAboutRecipeSaved_InitiatorMissing() {
        User target = User.builder().id(3L).build();
        UserRecipe recipe = UserRecipe.builder()
                .id(9L)
                .owner(User.builder().id(4L).build())
                .build();
        when(userRepository.findById(3L)).thenReturn(Optional.of(target));
        when(userRepository.findById(4L)).thenReturn(Optional.empty());

        Result<Void> result = service.notifyAboutRecipeSaved(recipe, 3L);

        assertFalse(result.isSuccess());
        assertTrue(result.isCaseInstanceOf(UserNotFoundCase.class));
        verify(notificationRepository, never()).save(any());
    }

    @Test
    void testNotifyAboutRecipeSaved_Success_ExistingNotification() {
        User target = User.builder().id(3L).build();
        User owner = User.builder().id(4L).build();
        UserRecipe recipe = UserRecipe.builder()
                .id(9L)
                .owner(owner)
                .build();

        when(userRepository.findById(3L)).thenReturn(Optional.of(target));
        when(userRepository.findById(4L)).thenReturn(Optional.of(owner));

        Notification existing = Notification.builder()
                .id(102L)
                .user(target)
                .target(NotificationTarget.builder()
                        .objectId(9L)
                        .type(NotificationType.RECIPE_SAVED)
                        .build())
                .createdAt(LocalDateTime.now().minusMinutes(5))
                .lastInteraction(LocalDateTime.now().minusMinutes(5))
                .initiators(new HashSet<>())
                .build();
        when(notificationRepository.findActiveNotification(
                eq(3L), eq(9L),
                any(LocalDateTime.class),
                any(LocalDateTime.class)))
                .thenReturn(Optional.of(existing));
        when(notificationRepository.save(any(Notification.class))).thenAnswer(inv -> inv.getArgument(0));

        Result<Void> result = service.notifyAboutRecipeSaved(recipe, 3L);

        assertTrue(result.isSuccess());
        verify(notificationRepository).save(notificationCaptor.capture());
        Notification saved = notificationCaptor.getValue();
        assertEquals(3L, saved.getUser().getId());
        assertEquals(NotificationType.RECIPE_SAVED, saved.getTarget().getType());
        assertEquals(9L, saved.getTarget().getObjectId());
    }

    @Test
    void testNotifyAboutRecipeSaved_ThrowsOnNewNotification() {
        User target = User.builder().id(3L).build();
        User owner = User.builder().id(4L).build();
        UserRecipe recipe = UserRecipe.builder()
                .id(9L)
                .owner(owner)
                .build();

        when(userRepository.findById(3L)).thenReturn(Optional.of(target));
        when(userRepository.findById(4L)).thenReturn(Optional.of(owner));
        when(notificationRepository.findActiveNotification(
                eq(3L), eq(9L),
                any(LocalDateTime.class),
                any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () ->
                service.notifyAboutRecipeSaved(recipe, 3L)
        );
    }
}
