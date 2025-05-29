package github.projectgroup.receptoria.security.resource;


import github.projectgroup.receptoria.model.enities.Ownable;
import github.projectgroup.receptoria.model.enities.User;
import org.springframework.security.core.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.Function;

public abstract class AbstractDomainSecurity<E extends Ownable, ID> {

    protected abstract JpaRepository<E, ID> repository();

    protected Function<E, Long> ownerExtractor(){
        return Ownable::getOwnerIdentifier;
    }

    public boolean canSee(Authentication auth, ID id) {
        return repository().findById(id)
                .map(ownerExtractor())
                .map(ownerId -> ownerId.equals(((User) auth.getPrincipal()).getId()))
                .orElse(false);
    }

    public boolean canEdit(Authentication auth, ID id) {
        return canSee(auth, id);
    }
}


