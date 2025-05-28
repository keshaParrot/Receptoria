package github.projectgroup.receptoria.utils.result;

import org.springframework.http.HttpStatus;

public class RecipeNotFoundCase implements ResultCase {

    private final Long recipeId;

    public RecipeNotFoundCase(Long recipeId) {
        this.recipeId = recipeId;
    }
    @Override
    public String getCaseMessage() {
        return "Recipe with id:"+ recipeId +"not found";
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
