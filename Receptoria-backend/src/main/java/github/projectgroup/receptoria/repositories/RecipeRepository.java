package github.projectgroup.receptoria.repositories;

import github.projectgroup.receptoria.model.enities.UserRecipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<UserRecipe, Long> {

    Page<UserRecipe> findAllByOwner_Id(Long ownerId, Pageable pageable);



}
