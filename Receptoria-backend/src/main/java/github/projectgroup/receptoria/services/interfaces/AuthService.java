package github.projectgroup.receptoria.services.interfaces;

import github.projectgroup.receptoria.model.dtos.LoginRequest;
import github.projectgroup.receptoria.model.dtos.UserDTO;
import github.projectgroup.receptoria.model.dtos.UserRegisterRequest;
import github.projectgroup.receptoria.utils.result.Result;

public interface AuthService {
    Result<Void> registerUser(UserRegisterRequest request);
    Result<String> authenticateUser(LoginRequest request);
}
