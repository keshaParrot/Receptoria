package github.projectgroup.receptoria.model.mappers;

import github.projectgroup.receptoria.model.dtos.RecipeDTO;
import github.projectgroup.receptoria.model.dtos.RecipePhotoInfo;
import github.projectgroup.receptoria.model.enities.UserRecipe;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RecipeMapper {

    @Mapping(source = "description", target = "description", qualifiedByName = "extractString")
    @Mapping(source = "instructions", target = "instructions", qualifiedByName = "extractString")
    @Mapping(source = "photoPaths", target = "photos", qualifiedByName = "mapPhotoPathsToInfos")
    RecipeDTO toDto(UserRecipe recipe);

    @Named("extractString")
    default String extractString(String string) {
        return string;
    }

    @Named("mapPhotoPathsToInfos")
    default List<RecipePhotoInfo> mapPhotoPathsToInfos(List<String> photoPaths) {
        if (photoPaths == null || photoPaths.isEmpty()) {
            return null;
        }
        return photoPaths.stream()
                .filter(Objects::nonNull)
                .map(this::toPhotoInfo)
                .collect(Collectors.toList());
    }

    default RecipePhotoInfo toPhotoInfo(String fullPath) {
        if (fullPath == null || fullPath.isBlank()) {
            return null;
        }
        String fileName = Paths.get(fullPath).getFileName().toString();
        String recipeId = extractRecipeIdFromPath(fullPath);
        String url = "/uploads/recipes/" + recipeId + "/" + fileName;
        String contentType;
        try {
            Path p = Paths.get(fullPath);
            contentType = Files.probeContentType(p);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
        } catch (IOException e) {
            contentType = "application/octet-stream";
        }
        return new RecipePhotoInfo(url, fileName, contentType);
    }

    default String extractRecipeIdFromPath(String fullPath) {
        if (fullPath == null) {
            return "";
        }
        String[] parts = fullPath.replace("\\", "/").split("/recipes/");
        if (parts.length < 2) {
            return "";
        }
        String remainder = parts[1];
        String[] remainderParts = remainder.split("/");
        return remainderParts.length > 0 ? remainderParts[0] : "";
    }
}
