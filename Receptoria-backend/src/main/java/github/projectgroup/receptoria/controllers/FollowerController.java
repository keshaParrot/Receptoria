package github.projectgroup.receptoria.controllers;

import github.projectgroup.receptoria.domain.enums.Followtype;
import org.springframework.data.domain.Page;

public class FollowerController {

    public Page<String> getAllByFollowType(Long userId, Followtype followtype){
        throw new RuntimeException("not implemented yet");
    }

    public void follow(Long followUserId,Long followerUserId ){
        throw new RuntimeException("not implemented yet");
    }

    public void unfollow(Long followId){
        throw new RuntimeException("not implemented yet");
    }
}
