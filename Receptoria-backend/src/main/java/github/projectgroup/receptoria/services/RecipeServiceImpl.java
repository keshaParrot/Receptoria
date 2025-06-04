package github.projectgroup.receptoria.services;

import github.projectgroup.receptoria.domain.mappers.RecipeMapper;
import github.projectgroup.receptoria.utils.result.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import github.projectgroup.receptoria.domain.dtos.CreateRecipeRequest;
import github.projectgroup.receptoria.domain.dtos.RecipeDTO;
import github.projectgroup.receptoria.domain.enities.User;
import github.projectgroup.receptoria.domain.enities.UserRecipe;
import github.projectgroup.receptoria.domain.enums.CookingMethod;
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
        Result<User> owner = userRepository.findById(request.getOwnerId())
                .map(user -> Result.success(user, new SuccessCase(null)))
                .orElseGet(() -> Result.failure(new UserNotFoundCase(request.getOwnerId())));

        Result<User> coOwner = null;
        if (request.getCoOwnerId() != null) {
            coOwner = userRepository.findById(request.getCoOwnerId())
                    .map(user -> Result.success(user, new SuccessCase(null)))
                    .orElseGet(() -> Result.failure(new UserNotFoundCase(request.getCoOwnerId())));
        }

        if (owner.isSuccess() && (coOwner == null || coOwner.isSuccess())){
            List<MultipartFile> photos = request.getPhotos();
            if (photos != null && photos.size() > 5) {
                return Result.failure(new BadArgumentsCase("Creation failed: more than 5 photos uploaded"));
            }

            UserRecipe recipe = UserRecipe.builder()
                    .ingredients(request.getIngredients())
                    .description(request.getDescription())
                    .instructions(request.getInstructions())
                    .portionSize(request.getPortionSize())
                    .method(request.getMethod())
                    .category(request.getCategory())
                    .owner(owner.getValue())
                    .coOwner(
                            coOwner != null
                                    ? coOwner.getValue()
                                    : null
                    )
                    .isPublic(false)
                    .build();

            recipe = recipeRepository.save(recipe);

            if (photos != null && !photos.isEmpty()) {
                Path recipeFolder = Paths.get(uploadDir, recipe.getId().toString());
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

                    // Оригінальне ім'я (щоб відобразити у клієнті, можемо додати до DTO, якщо буде потрібно)
                    String originalName = file.getOriginalFilename();
                    // Розширення (наприклад, ".jpg")
                    String ext = "";
                    if (originalName != null && originalName.contains(".")) {
                        ext = originalName.substring(originalName.lastIndexOf("."));
                    }

                    // Генеруємо унікальне імя файлу
                    String uniqueName = UUID.randomUUID().toString() + ext;

                    // Повний шлях: uploads/recipes/{recipeId}/{uniqueName}
                    Path destination = recipeFolder.resolve(uniqueName);
                    try {
                        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        // Якщо не вдалося зберегти хоча б одне фото,
                        // можемо вирішити: або кинути виняток (відкат транзакції),
                        // або просто пропустити цей файл (і зберегти решту).
                        throw new ResponseStatusException(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "Не вдалося зберегти фото: " + originalName
                        );
                    }

                    String url = "/uploads/recipes/" + recipe.getId() + "/" + uniqueName;
                    savedPaths.add(url);
                }

                // Заповнюємо у UserRecipe поле photoPaths, і JPA збереже в таблиці recipe_photos
                recipe.setPhotoPaths(savedPaths);
                recipeRepository.save(recipe);
            }

            return Result.success(toDto(recipe), new SuccessCase("Recipe created successfully"));
        }
        return Result.failure(new UserNotFoundCase(coOwner.isSuccess()?request.getOwnerId():request.getCoOwnerId()));
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

        // Обновляем coOwner, если передан
        if (request.getCoOwnerId() != null) {
            User coOwner = userRepository.findById(request.getCoOwnerId())
                    .orElseThrow(() -> new IllegalArgumentException("Co-owner not found: " + request.getCoOwnerId()));
            existing.setCoOwner(coOwner);
        } else {
            existing.setCoOwner(null);
        }

        // Обработка фотографий
        List<MultipartFile> newPhotos = request.getPhotos();
        if (newPhotos != null) {
            if (newPhotos.size() > 5) {
                return Result.failure(new BadArgumentsCase("Update failed: more than 5 photos uploaded"));
            }

            // 1) Удаляем старые файлы с диска (если они были)
            List<String> oldPaths = existing.getPhotoPaths();
            if (oldPaths != null && !oldPaths.isEmpty()) {
                for (String oldUrl : oldPaths) {
                    String relative = oldUrl.replaceFirst("^/uploads/recipes/", "");
                    Path oldFile = Paths.get(uploadDir).resolve(relative);
                    try {
                        Files.deleteIfExists(oldFile);
                    } catch (IOException ignored) { }
                }
            }
            existing.getPhotoPaths().clear();

            // 2) Готовим папку (uploads/recipes/{id})
            Path recipeFolder = Paths.get(uploadDir, existing.getId().toString());
            try {
                if (!Files.exists(recipeFolder)) {
                    Files.createDirectories(recipeFolder);
                }
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create folder for images");
            }

            // 3) Сохраняем новые файлы и собираем новые пути
            List<String> savedPaths = new ArrayList<>();
            for (MultipartFile file : newPhotos) {
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
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save photo: " + originalName);
                }
                String url = "/uploads/recipes/" + existing.getId() + "/" + uniqueName;
                savedPaths.add(url);
            }
            existing.setPhotoPaths(savedPaths);
        }

        UserRecipe saved = recipeRepository.save(existing);
        return Result.success(recipeMapper.toDto(saved), new SuccessCase("Recipe updated successfully"));
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
    public void setPublicStatus(Long id, boolean status) {
        UserRecipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Recipe not found: " + id
                ));
        recipe.setPublic(status);
        recipeRepository.save(recipe);
    }
}
