package github.projectgroup.receptoria.repositories;

import github.projectgroup.receptoria.model.enities.UserRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<UserRecipe, Long> {





}
