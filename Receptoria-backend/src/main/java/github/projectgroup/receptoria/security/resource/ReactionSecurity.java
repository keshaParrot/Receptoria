package github.projectgroup.receptoria.security.resource;

import github.projectgroup.receptoria.model.enities.Reaction;
import github.projectgroup.receptoria.repositories.ReactionRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;


@Component("reactionSecurity")
public class ReactionSecurity extends AbstractDomainSecurity<Reaction, Long> {

    private final ReactionRepository reactionRepository;

    public ReactionSecurity(ReactionRepository reactionRepository) {
        this.reactionRepository = reactionRepository;
    }

    @Override
    protected JpaRepository<Reaction, Long> repository() {
        return reactionRepository;
    }
}

