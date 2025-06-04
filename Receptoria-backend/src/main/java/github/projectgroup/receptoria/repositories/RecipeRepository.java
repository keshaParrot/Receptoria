package github.projectgroup.receptoria.repositories;

import github.projectgroup.receptoria.model.enities.UserRecipe;
import github.projectgroup.receptoria.model.enums.CookingMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<UserRecipe, Long> {
    Page<UserRecipe> findAllByOwnerId(Long userId, Pageable pageable);

    @Query("""
        SELECT DISTINCT r
        FROM UserRecipe r
        JOIN r.ingredients i
        WHERE r.method = :method
          AND i.product IN :products
    """)
    Page<UserRecipe> findAllByMethodAndProductIn(
            @Param("method") CookingMethod method,
            @Param("products") List<String> products,
            Pageable pageable
    );

}
