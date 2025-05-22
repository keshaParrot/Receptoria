package github.projectgroup.receptoria.utils.result;

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
}
