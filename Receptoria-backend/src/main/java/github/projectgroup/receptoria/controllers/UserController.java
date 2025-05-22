package github.projectgroup.receptoria.controllers;

import github.projectgroup.receptoria.domain.dtos.ChangePasswordRequest;
import github.projectgroup.receptoria.domain.dtos.UserDTO;
import github.projectgroup.receptoria.domain.dtos.UserRegisterRequest;
import github.projectgroup.receptoria.domain.dtos.UserUpdateRequest;
import github.projectgroup.receptoria.domain.enums.Followtype;

public class UserController {

    public UserDTO getUserById(Long id){
        throw new RuntimeException("not implemented yet");
    }

    public UserDTO updateById(UserUpdateRequest request){
        throw new RuntimeException("not implemented yet");
    }

    public void sendChangePasswordRequest(){
        throw new RuntimeException("not implemented yet");
    }

    public void registerUser(UserRegisterRequest request){

    }

    public String changePassword(ChangePasswordRequest request){
        throw new RuntimeException("not implemented yet");
    }
}
