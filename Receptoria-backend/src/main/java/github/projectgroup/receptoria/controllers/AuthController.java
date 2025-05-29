package github.projectgroup.receptoria.controllers;

import github.projectgroup.receptoria.model.dtos.ChangePasswordRequest;
import github.projectgroup.receptoria.model.dtos.LoginRequest;
import github.projectgroup.receptoria.model.dtos.UserRegisterRequest;
import github.projectgroup.receptoria.model.mappers.ResultMapper;
import github.projectgroup.receptoria.services.interfaces.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegisterRequest request) {
        return ResultMapper.toResponseEntity(authService.registerUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        return ResultMapper.toResponseEntity(authService.authenticateUser(request));
    }

    @GetMapping("/reset-password")
    public ResponseEntity<?> sendChangePasswordRequest(
            @RequestParam("email") String email){
        return ResultMapper.toResponseEntity(authService.sendResetPasswordRequest(email));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestParam("token") String token,
            @RequestBody ChangePasswordRequest request)
    {
        return ResultMapper.toResponseEntity(authService.changePassword(token, request));
    }

    @GetMapping("/validate-mail")
    public ResponseEntity<?> validateMail(
            @RequestParam("token") String token) {

        return ResultMapper.toResponseEntity(authService.validateMail(token));
    }
}

