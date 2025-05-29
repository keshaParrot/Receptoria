package github.projectgroup.receptoria.security.resource;

import github.projectgroup.receptoria.model.enities.User;
import github.projectgroup.receptoria.repositories.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component("userSecurity")
public class UserSecurity extends AbstractDomainSecurity<User, Long> {

    private final UserRepository userRepository;

    public UserSecurity(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected JpaRepository<User, Long> repository() {
        return userRepository;
    }
}

