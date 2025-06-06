package github.projectgroup.receptoria.services;

import github.projectgroup.receptoria.model.dtos.CreateRecipeRequest;
import github.projectgroup.receptoria.model.dtos.RecipeDTO;
import github.projectgroup.receptoria.model.dtos.ValidationErrorResponse;
import github.projectgroup.receptoria.model.enities.User;
import github.projectgroup.receptoria.model.enities.UserRecipe;
import github.projectgroup.receptoria.model.enums.CookingMethod;
import github.projectgroup.receptoria.model.mappers.RecipeMapper;
import github.projectgroup.receptoria.utils.result.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import github.projectgroup.receptoria.repositories.RecipeRepository;
import github.projectgroup.receptoria.repositories.UserRepository;
import github.projectgroup.receptoria.repositories.SavedRecipeRepository;
import github.projectgroup.receptoria.services.interfaces.RecipeService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final SavedRecipeRepository savedRecipeRepository;
    private final UserRepository userRepository;
    private final RecipeMapper recipeMapper;

    @Value("${app.upload.dir}")
    private String uploadDir;

    private RecipeDTO toDto(UserRecipe r) {
        return recipeMapper.toDto(r);
    }

    @Override
    public Result<RecipeDTO> getById(Long id) {
        return recipeRepository.findById(id)
                .map(userRecipe -> Result.success(toDto(userRecipe), new SuccessCase(null)))
                .orElseGet(()->Result.failure(new RecipeNotFoundCase(id)));
    }

    @Override
    public Page<UserRecipe> getAllByUserId(Long userId, Pageable pageable) {
        return recipeRepository.findAllByOwnerId(userId, pageable);
    }

    @Override
    public Page<RecipeDTO> getAllSavedByUserId(Long userId, Pageable pageable) {
        return savedRecipeRepository
                .findAllRecipesByUserId(userId, pageable)
                .map(this::toDto);
    }

    @Override
    public Page<UserRecipe> findAllSorted(List<String> ingredients, String cookingMethod, Pageable pageable) {
        CookingMethod methodEnum;

        try {
            methodEnum = CookingMethod.valueOf(cookingMethod.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Unknown cookingMethod: " + cookingMethod
            );
        }

        return recipeRepository.findAllByMethodAndProductIn(
                methodEnum,
                ingredients,
                pageable
        );
    }

    @Override
    public Result<RecipeDTO> create(CreateRecipeRequest request) {
        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + request.getOwnerId()));
        User coOwner = null;
        if (request.getCoOwnerId() != null) {
            coOwner = userRepository.findById(request.getCoOwnerId())
                    .orElseThrow(() -> new IllegalArgumentException("Co-owner not found: " + request.getCoOwnerId()));
        }

        UserRecipe recipe = UserRecipe.builder()
                .ingredients(request.getIngredients())
                .description(request.getDescription())
                .instructions(request.getInstructions())
                .portionSize(request.getPortionSize())
                .method(request.getMethod())
                .category(request.getCategory())
                .owner(owner)
                .coOwner(coOwner)
                .isPublic(false)
                .build();

        recipe = recipeRepository.save(recipe);

        List<MultipartFile> photos = request.getPhotos();
        if (photos != null && !photos.isEmpty()) {
            if (photos.size() > 5) {
                return Result.failure(new BadArgumentsCase("Creation failed: more than 5 photos uploaded"));
            }
            List<String> savedPaths = saveNewPhotos(recipe.getId(), photos);
            recipe.setPhotoPaths(savedPaths);
            recipe = recipeRepository.save(recipe);
        }

        return Result.success(toDto(recipe), new SuccessCase("Recipe created successfully"));
    }

    @Override
    public Result<RecipeDTO> update(Long id, CreateRecipeRequest request) {
        UserRecipe existing = recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recipe not found: " + id));

        existing.setIngredients(request.getIngredients());
        existing.setDescription(request.getDescription());
        existing.setInstructions(request.getInstructions());
        existing.setPortionSize(request.getPortionSize());
        existing.setMethod(request.getMethod());
        existing.setCategory(request.getCategory());

        if (request.getCoOwnerId() != null) {
            User coOwner = userRepository.findById(request.getCoOwnerId())
                    .orElseThrow(() -> new IllegalArgumentException("Co-owner not found: " + request.getCoOwnerId()));
            existing.setCoOwner(coOwner);
        } else {
            existing.setCoOwner(null);
        }

        List<MultipartFile> newPhotos = request.getPhotos();
        if (newPhotos != null) {
            if (newPhotos.size() > 5) {
                return Result.failure(new BadArgumentsCase("Update failed: more than 5 photos uploaded"));
            }

            List<String> oldPaths = existing.getPhotoPaths();
            if (oldPaths != null && !oldPaths.isEmpty()) {
                deleteOldPhotos(oldPaths);
                existing.getPhotoPaths().clear();
            }

            List<String> savedPaths = saveNewPhotos(existing.getId(), newPhotos);
            existing.setPhotoPaths(savedPaths);
        }

        UserRecipe saved = recipeRepository.save(existing);
        return Result.success(toDto(saved), new SuccessCase("Recipe updated successfully"));
    }

    private void deleteOldPhotos(List<String> oldPaths) {
        for (String oldUrl : oldPaths) {
            String relative = oldUrl.replaceFirst("^/uploads/recipes/", "");
            Path oldFile = Paths.get(uploadDir).resolve(relative);
            try {
                Files.deleteIfExists(oldFile);
            } catch (IOException ignored) {
            }
        }
    }

    private List<String> saveNewPhotos(Long recipeId, List<MultipartFile> photos) {
        Path recipeFolder = Paths.get(uploadDir, recipeId.toString());
        try {
            if (!Files.exists(recipeFolder)) {
                Files.createDirectories(recipeFolder);
            }
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to create folder for images"
            );
        }

        List<String> savedPaths = new ArrayList<>();
        for (MultipartFile file : photos) {
            if (file.isEmpty()) continue;
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                continue;
            }

            String originalName = file.getOriginalFilename();
            String ext = "";
            if (originalName != null && originalName.contains(".")) {
                ext = originalName.substring(originalName.lastIndexOf("."));
            }

            String uniqueName = UUID.randomUUID().toString() + ext;
            Path destination = recipeFolder.resolve(uniqueName);
            try {
                Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Не вдалося зберегти фото: " + originalName
                );
            }
            String url = "/uploads/recipes/" + recipeId + "/" + uniqueName;
            savedPaths.add(url);
        }

        return savedPaths;
    }


    @Override
    public boolean deleteById(Long id) {
        if (recipeRepository.existsById(id)) {
            recipeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Result<Void> setPublicStatus(Long id, boolean status) {
        return recipeRepository.findById(id)
                .map(userRecipe -> {
                    userRecipe.setPublic(status);
                    recipeRepository.save(userRecipe);
                    return Result.<Void>success(null, new SuccessCase(null));
                })
                .orElseGet(() ->
                        Result.failure(new UserNotFoundCase(id))
                );
    }
}
