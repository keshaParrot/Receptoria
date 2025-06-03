package github.projectgroup.receptoria.utils.converters;

import org.springframework.core.convert.converter.Converter;
import github.projectgroup.receptoria.domain.enities.RecipeIngredients;
import org.springframework.stereotype.Component;

@Component
public class StringToRecipeIngredientsConverter
        implements Converter<String, RecipeIngredients> {

    @Override
    public RecipeIngredients convert(String source) {
        // Приклад: "Tomato:2pcs" → product="Tomato", quantity="2pcs"
        String[] parts = source.split(":", 2);
        return RecipeIngredients.builder()
                .product(parts[0])
                .quantity(parts.length > 1 ? parts[1] : "")
                .build();
    }
}