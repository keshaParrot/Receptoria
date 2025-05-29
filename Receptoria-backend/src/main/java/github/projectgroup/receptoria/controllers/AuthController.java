package github.projectgroup.receptoria.controllers;

import github.projectgroup.receptoria.model.dtos.ChangePasswordRequest;
import github.projectgroup.receptoria.model.dtos.LoginRequest;
import github.projectgroup.receptoria.model.dtos.UserRegisterRequest;
import github.projectgroup.receptoria.model.mappers.ResultMapper;
import github.projectgroup.receptoria.services.interfaces.AuthService;
import github.projectgroup.receptoria.utils.Templates.html.VerificationMail;
import github.projectgroup.receptoria.utils.result.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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

    @GetMapping(value = "/validate-mail", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> validateMail(@RequestParam("token") String token) {
        Result<Void> result = authService.validateMail(token);
        return ResponseEntity.ok(VerificationMail.generateValidationResponsePage(result.isSuccess()));
    }

}

