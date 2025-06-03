package github.projectgroup.receptoria.domain.mappers;

import github.projectgroup.receptoria.domain.dtos.RecipeDTO;
import github.projectgroup.receptoria.domain.enities.UserRecipe;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
public interface RecipeMapper {
    RecipeMapper INSTANCE = Mappers.getMapper( RecipeMapper.class );

    // 1) Переназначаємо поле instructions → instruction
    // 2) MapStruct автоматично підхопить інгредієнти та photos (однорівневий список)
    @Mapping(source = "instructions", target = "instruction")
    RecipeDTO toDTO(UserRecipe entity);
}
