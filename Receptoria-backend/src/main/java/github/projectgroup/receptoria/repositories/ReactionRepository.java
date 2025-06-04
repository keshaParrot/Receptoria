// src/main/java/github/projectgroup/receptoria/repositories/ReactionRepository.java
package github.projectgroup.receptoria.repositories;

import github.projectgroup.receptoria.domain.enities.Reaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    Page<Reaction> findAllByRatedRecipeId(Long recipeId, Pageable pageable);
    Page<Reaction> findAllByOwnerId(Long ownerId, Pageable pageable);
}
