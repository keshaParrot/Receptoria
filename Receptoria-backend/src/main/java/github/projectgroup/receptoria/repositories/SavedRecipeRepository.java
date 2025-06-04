package github.projectgroup.receptoria.repositories;



import github.projectgroup.receptoria.model.enities.SavedRecipe;
import github.projectgroup.receptoria.model.enities.UserRecipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SavedRecipeRepository extends JpaRepository<SavedRecipe, Long> {
    @Query("select sr.recipe from SavedRecipe sr where sr.user.id = :userId")
    Page<UserRecipe> findAllRecipesByUserId(@Param("userId") Long userId, Pageable pageable);
}
