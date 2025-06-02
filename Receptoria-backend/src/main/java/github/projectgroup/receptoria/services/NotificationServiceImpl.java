package github.projectgroup.receptoria.services;

import github.projectgroup.receptoria.model.dtos.NotificationDTO;
import github.projectgroup.receptoria.model.enities.*;
import github.projectgroup.receptoria.model.enums.NotificationType;
import github.projectgroup.receptoria.model.enums.VerificationCodeType;
import github.projectgroup.receptoria.model.mappers.NotificationMapper;
import github.projectgroup.receptoria.repositories.UserRepository;
import github.projectgroup.receptoria.repositories.NotificationRepository;
import github.projectgroup.receptoria.services.interfaces.DeliveryChannelResolver;
import github.projectgroup.receptoria.services.interfaces.NotificationService;
import github.projectgroup.receptoria.utils.Templates.mail.EmailConfirmationHtmlTemplatesImpl;
import github.projectgroup.receptoria.utils.Templates.mail.PasswordResetHtmlTemplatesImpl;
import github.projectgroup.receptoria.utils.Templates.mail.MailHtmlTemplates;
import github.projectgroup.receptoria.utils.result.NotificationNotFoundCase;
import github.projectgroup.receptoria.utils.result.Result;
import github.projectgroup.receptoria.utils.result.SuccessCase;
import github.projectgroup.receptoria.utils.result.UserNotFoundCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final DeliveryChannelResolver resolver;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserRepository userRepository;

    @Override
    public Result<NotificationDTO> getById(Long id) {
        return notificationRepository.findById(id)
                .map(notification -> Result.success(toDTO(notification),new SuccessCase(null)))
                .orElseGet(() -> Result.failure(new NotificationNotFoundCase(id)));
    }

    @Override
    public Page<NotificationDTO> getAllByUserId(Long userId, Pageable pageable) {
        Page<Notification> notificationsPage =
                notificationRepository.findByUserId(userId, pageable);
        return notificationsPage.map(this::toDTO);
    }

    @Override
    public Result<Void> notifyAboutReaction(Reaction reaction, Long userId) {
        return addOrUpdateNotification(
                userId,
                NotificationType.NEW_REACTION,
                reaction.getId(),
                reaction.getOwnerIdentifier()
        );
    }

    @Override
    public Result<Void> notifyAboutNewRecipe(UserRecipe recipe, Long userId) {
        return addOrUpdateNotification(
                userId,
                NotificationType.NEW_RECIPE_FROM_FOLLOWED_USER,
                recipe.getId(),
                recipe.getOwnerIdentifier()
        );
    }

    @Override
    public Result<Void> notifyAboutFollow(Follow follow, Long userId) {
        return addOrUpdateNotification(
                userId,
                NotificationType.NEW_FOLLOWER,
                follow.getId(),
                follow.getFollower().getId()
        );
    }

    @Override
    public Result<Void> notifyAboutRecipeSaved(UserRecipe recipe, Long userId) {
        if(isUserWantToHaveNotificationAboutRecipe()){
            return addOrUpdateNotification(
                    userId,
                    NotificationType.RECIPE_SAVED,
                    recipe.getId(),
                    recipe.getOwner().getId()
            );
        }
        return Result.success(null,new SuccessCase("user wont to see new recipes from this user"));
    }

    private Result<Void> addOrUpdateNotification(
            Long targetUserId,
            NotificationType type,
            Long relatedObjectId,
            Long initiatorUserId
    ){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thresholdWindow = now.minusMinutes(10);
        LocalDateTime thresholdMaxAge = now.minusMinutes(30);

        Result<User> targetUser = userRepository.findById(targetUserId)
                .map(user -> Result.success(user,new SuccessCase(null)))
                .orElseGet(() -> Result.failure(new UserNotFoundCase(targetUserId)));

        Result<User> initiatorUser = userRepository.findById(initiatorUserId)
                .map(user -> Result.success(user,new SuccessCase(null)))
                .orElseGet(() -> Result.failure(new UserNotFoundCase(targetUserId)));

        if (targetUser.isSuccess() && initiatorUser.isSuccess()) {
            Optional<Notification> opt = notificationRepository.findActiveNotification(
                    targetUserId,
                    relatedObjectId,
                    thresholdWindow,
                    thresholdMaxAge
            );

            if (opt.isPresent()) {
                Notification existing = opt.get();
                existing.setLastInteraction(now.plusMinutes(10));
                existing.getInitiators().add(initiatorUser.getValue());
                notificationRepository.save(existing);
            } else {
                Notification newGroup = Notification.builder()
                        .user(targetUser.getValue())
                        .target(
                                NotificationTarget.builder()
                                        .objectId(relatedObjectId)
                                        .type(type)
                                        .build()
                        )
                        .createdAt(now)
                        .lastInteraction(now.plusMinutes(10))
                        .build();
                newGroup.getInitiators().add(initiatorUser.getValue());
                notificationRepository.save(newGroup);
            }
            return Result.success(null,new SuccessCase("Notification created successfully"));
        }
        return Result.failure(targetUser.isSuccess()?initiatorUser.getResultCase():targetUser.getResultCase());
    }

    @Override
    public void sendVerificationCode(VerificationCode code, String email) {
        MailHtmlTemplates templates;
        if (code.getType()== VerificationCodeType.VERIFICATION_MAIL){
            templates = new EmailConfirmationHtmlTemplatesImpl();
        }
        else{
            templates = new PasswordResetHtmlTemplatesImpl();
        }

        resolver.sendNotification(
                templates.getSubject(),
                templates.getResetBody(code.getCode()),
                email,
                code.getChannel()
        );
    }

    private boolean isUserWantToHaveNotificationAboutRecipe(){
        return true;
    }
    private NotificationDTO toDTO(Notification notification) {
        return notificationMapper.toDto(notification);
    }
}
