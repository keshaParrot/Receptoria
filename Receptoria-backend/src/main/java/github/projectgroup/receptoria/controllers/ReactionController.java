package github.projectgroup.receptoria.controllers;

import github.projectgroup.receptoria.model.enities.Reaction;

public class ReactionController {

    public Reaction reactRecipe(
            Long recipeId,
            Long userId,
            float rating,
            String comment
    ) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public Reaction reactRecipe(
            Long reactId,
            String newContent,
            float newRating
    ){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public void deleteReaction(Long reactId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
