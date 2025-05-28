package github.projectgroup.receptoria.controllers;

import github.projectgroup.receptoria.domain.dtos.ChangePasswordRequest;
import github.projectgroup.receptoria.domain.dtos.UserDTO;
import github.projectgroup.receptoria.domain.dtos.UserUpdateRequest;
import github.projectgroup.receptoria.domain.enums.MealCategory;
import github.projectgroup.receptoria.domain.mappers.ResultMapper;
import github.projectgroup.receptoria.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("{id}/get")
    public ResponseEntity<?> getUserById(@PathVariable Long id){
        return ResultMapper.toResponseEntity(userService.getById(id));
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateById(
            @PathVariable Long id,
            @RequestBody UserUpdateRequest request)
    {
        return ResultMapper.toResponseEntity(userService.updateById(id, request));
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

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestParam("token") String token,
            @RequestBody ChangePasswordRequest request)
    {
        return ResultMapper.toResponseEntity(userService.changePassword(token,request));
    }

}
