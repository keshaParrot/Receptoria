package github.projectgroup.receptoria.controllers;

import github.projectgroup.receptoria.model.dtos.ChangePasswordRequest;
import github.projectgroup.receptoria.model.dtos.UserDTO;
import github.projectgroup.receptoria.model.dtos.UserUpdateRequest;
import github.projectgroup.receptoria.model.enums.MealCategory;
import github.projectgroup.receptoria.model.mappers.ResultMapper;
import github.projectgroup.receptoria.services.interfaces.AuthService;
import github.projectgroup.receptoria.services.interfaces.UserService;
import github.projectgroup.receptoria.utils.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @GetMapping("{id}/get")
    public ResponseEntity<?> getUserById(@PathVariable Long id){
        return ResultMapper.toResponseEntity(userService.getById(id));
    }

    @PreAuthorize("@userSecurity.canEdit(authentication, #id)")
    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateById(
            @PathVariable Long id,
            @RequestBody UserUpdateRequest request)
    {
        Result<UserDTO> updateResult = userService.updateById(id, request);
        if(updateResult.isSuccess()){
            authService.sendVerificationMailCode(updateResult.getValue().getEmail());
        }
        return ResultMapper.toResponseEntity(updateResult);
    }

    @GetMapping("/get-by")
    public ResponseEntity<Page<UserDTO>> getUserBy(
            @RequestParam("userName") String name,
            @RequestParam("usersCategory") MealCategory[] mainCategory,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok().body(
                userService.findBy(
                        name,
                        mainCategory,
                        pageable
                )
        );
    }
}
