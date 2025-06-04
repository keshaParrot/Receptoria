package github.projectgroup.receptoria.utils.converters;

import github.projectgroup.receptoria.model.enities.RecipeIngredients;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToRecipeIngredientsConverter
        implements Converter<String, RecipeIngredients> {

    @Override
    public RecipeIngredients convert(String source) {
        String[] parts = source.split(":", 2);
        return RecipeIngredients.builder()
                .product(parts[0])
                .quantity(parts.length > 1 ? parts[1] : "")
                .build();
    }
}