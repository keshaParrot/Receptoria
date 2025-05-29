package github.projectgroup.receptoria.security.resource;


import org.springframework.security.core.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.function.Function;

public abstract class AbstractDomainSecurity<E, ID> {

    protected abstract JpaRepository<E, ID> repository();

    protected abstract Function<E, String> ownerExtractor();

    public boolean canSee(Authentication auth, ID id) {
        return repository().findById(id)
                .map(ownerExtractor())
                .map(ownerId -> ownerId.equals(auth.getName()))
                .orElse(false);
    }

    public boolean canEdit(Authentication auth, ID id) {
        return canSee(auth, id);
    }
}


