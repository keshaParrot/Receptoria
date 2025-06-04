package github.projectgroup.receptoria.services;

import github.projectgroup.receptoria.model.dtos.ChangePasswordRequest;
import github.projectgroup.receptoria.model.dtos.UserDTO;
import github.projectgroup.receptoria.model.dtos.UserUpdateRequest;
import github.projectgroup.receptoria.model.enities.User;
import github.projectgroup.receptoria.model.enities.UserRecipe;
import github.projectgroup.receptoria.model.enums.MealCategory;
import github.projectgroup.receptoria.model.mappers.UserMapper;
import github.projectgroup.receptoria.repositories.UserRepository;
import github.projectgroup.receptoria.services.interfaces.UserService;
import github.projectgroup.receptoria.utils.result.BadArgumentsCase;
import github.projectgroup.receptoria.utils.result.Result;
import github.projectgroup.receptoria.utils.result.SuccessCase;
import github.projectgroup.receptoria.utils.result.UserNotFoundCase;
import jdk.jshell.spi.ExecutionControl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Result<UserDTO> getById(Long id) {
        return userRepository.findById(id)
                .map(user -> Result.success(toDTO(user), new SuccessCase(null)))
                .orElseGet(() -> Result.failure(new UserNotFoundCase(id)));
    }

    @Override
    public Result<UserDTO> updateById(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id).orElse(null);
        int updatedCounter = 0;

        if (user != null) {
            if (request.getFirstName() != null){
                user.setFirstName(request.getFirstName());
                updatedCounter++;
            }
            if (request.getLastName() != null){
                user.setLastName(request.getLastName());
                updatedCounter++;
            }
            if (request.getEmail() != null ){
                if (userRepository.findByEmail(request.getEmail()).isPresent()){
                    return Result.failure(new BadArgumentsCase("There is already a user with this email in the system"));
                }
                user.setEmail(request.getEmail());
                updatedCounter++;
            }
            if (!request.getMainMealCategories().isEmpty()){
                user.setMainMealCategories(request.getMainMealCategories());
                updatedCounter++;
            }
            return Result.success(
                    toDTO(userRepository.save(user)),
                    new SuccessCase(
                            "The user has updated "+updatedCounter+" account attributes."
                    )
            );
        }

        return Result.failure(new UserNotFoundCase(id));
    }

    @Override
    public Page<UserDTO> findBy(String name, MealCategory[] mainCategory, Pageable pageable) {
        Page<User> users = userRepository.findByFullNameAndCategories(name, mainCategory, pageable);
        return users.map(this::toDTO);
    }


    private UserDTO toDTO(User user) {
        return userMapper.toDto(user);
    }
}
