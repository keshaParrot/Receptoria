package github.projectgroup.receptoria.repositories;

import github.projectgroup.receptoria.model.enities.User;
import github.projectgroup.receptoria.model.enums.MealCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
    SELECT DISTINCT u FROM User u
    JOIN u.mainMealCategories c
    WHERE CONCAT(u.firstName, ' ', u.lastName) LIKE %:name%
    AND c IN :categories
""")
    Page<User> findByFullNameAndCategories(@Param("name") String name,
                                           @Param("categories") MealCategory[] categories,
                                           Pageable pageable);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByUsername(String username);
}
