package github.projectgroup.receptoria.services.interfaces;

import github.projectgroup.receptoria.model.dtos.NotificationDTO;
import github.projectgroup.receptoria.model.enities.*;
import github.projectgroup.receptoria.utils.result.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {

    Result<NotificationDTO> getById(Long id);
    Page<NotificationDTO> getAllByUserId(Long userId,Pageable pageable);

    Result<Void> notifyAboutReaction(Reaction reaction, Long userId);
    Result<Void> notifyAboutNewRecipe(UserRecipe recipe, Long userId);
    Result<Void> notifyAboutFollow(Follow follow, Long userId);
    Result<Void> notifyAboutRecipeSaved(UserRecipe recipe, Long userId);

    void sendVerificationCode(VerificationCode code, String email);
}
