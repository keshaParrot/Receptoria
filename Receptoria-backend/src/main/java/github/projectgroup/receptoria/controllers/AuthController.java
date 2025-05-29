package github.projectgroup.receptoria.controllers;

import github.projectgroup.receptoria.model.dtos.LoginRequest;
import github.projectgroup.receptoria.model.dtos.UserDTO;
import github.projectgroup.receptoria.model.dtos.UserRegisterRequest;
import github.projectgroup.receptoria.model.mappers.ResultMapper;
import github.projectgroup.receptoria.security.JwtUtil;
import github.projectgroup.receptoria.services.interfaces.AuthService;
import github.projectgroup.receptoria.services.interfaces.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    public void sendChangePasswordRequest(){
        throw new RuntimeException("not implemented yet");
    }
}

