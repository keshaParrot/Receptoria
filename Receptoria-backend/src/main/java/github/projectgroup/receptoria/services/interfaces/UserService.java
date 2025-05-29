package github.projectgroup.receptoria.services.interfaces;

import github.projectgroup.receptoria.model.dtos.ChangePasswordRequest;
import github.projectgroup.receptoria.model.dtos.UserDTO;
import github.projectgroup.receptoria.model.dtos.UserUpdateRequest;
import github.projectgroup.receptoria.model.enums.MealCategory;
import github.projectgroup.receptoria.utils.result.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Result<UserDTO> getById(Long id);

    Result<Void> setVerifiedStatus(Long Id);

    Result<UserDTO> updateById(Long id, UserUpdateRequest request);
    Result<Void> changePassword(String token, ChangePasswordRequest request);
    Page<UserDTO> findBy(
            String name,
            MealCategory[] MainCategory,
            Pageable pageable
    );
}
