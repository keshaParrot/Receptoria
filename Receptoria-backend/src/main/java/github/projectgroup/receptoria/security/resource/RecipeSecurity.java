package github.projectgroup.receptoria.security.resource;

import github.projectgroup.receptoria.model.enities.UserRecipe;
import github.projectgroup.receptoria.repositories.RecipeRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;


@Component("recipeSecurity")
public class RecipeSecurity extends AbstractDomainSecurity<UserRecipe, Long> {

    private final RecipeRepository recipeRepository;

    public RecipeSecurity(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    protected JpaRepository<UserRecipe, Long> repository() {
        return recipeRepository;
    }
}

