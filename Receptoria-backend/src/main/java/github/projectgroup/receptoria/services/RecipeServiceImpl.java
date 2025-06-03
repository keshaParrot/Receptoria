package github.projectgroup.receptoria.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import github.projectgroup.receptoria.domain.dtos.CreateRecipeRequest;
import github.projectgroup.receptoria.domain.dtos.RecipeDTO;
import github.projectgroup.receptoria.domain.dtos.UserPreviewDTO;
import github.projectgroup.receptoria.domain.enities.RecipePhoto;
import github.projectgroup.receptoria.domain.enities.RecipeIngredients;
import github.projectgroup.receptoria.domain.enities.User;
import github.projectgroup.receptoria.domain.enities.UserRecipe;
import github.projectgroup.receptoria.domain.enums.CookingMethod;
import github.projectgroup.receptoria.repositories.RecipeRepository;
import github.projectgroup.receptoria.repositories.UserRepository;
import github.projectgroup.receptoria.repositories.SavedRecipeRepository;
import github.projectgroup.receptoria.services.interfaces.RecipeService;
import github.projectgroup.receptoria.utils.result.Result;
import github.projectgroup.receptoria.utils.result.ResultCase;
import github.projectgroup.receptoria.utils.result.RecipeNotFoundCase;
import github.projectgroup.receptoria.utils.result.SuccessCase;

import java.nio.file.Path;
import java.util.List;


@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final SavedRecipeRepository savedRecipeRepository;
    private final UserRepository userRepository;

    private RecipeDTO toDto(UserRecipe r) {
        RecipeDTO dto = new RecipeDTO();
        dto.setId(r.getId());
        dto.setIngredients(r.getIngredients());
        dto.setDescription(r.getDescription());
        dto.setInstruction(r.getInstructions());
        dto.setPortionSize(r.getPortionSize());
        dto.setMethod(r.getMethod());
        dto.setCategory(r.getCategory());
        dto.setPhotos(r.getPhotos());

        // Тут використовуємо UserPreviewDTO з його полями firstName та lastName
        UserPreviewDTO ownerDto = new UserPreviewDTO();
        ownerDto.setId(r.getOwner().getId());
        ownerDto.setFirstName(r.getOwner().getFirstName());
        ownerDto.setLastName(r.getOwner().getLastName());
        dto.setOwner(ownerDto);

        if (r.getCoOwner() != null) {
            UserPreviewDTO coOwnerDto = new UserPreviewDTO();
            coOwnerDto.setId(r.getCoOwner().getId());
            coOwnerDto.setFirstName(r.getCoOwner().getFirstName());
            coOwnerDto.setLastName(r.getCoOwner().getLastName());
            dto.setCoOwner(coOwnerDto);
        }
        return dto;
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
        // 1) Конвертуємо рядок у CookingMethod
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
        // 1) Знайти власника
        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "User not found: " + request.getOwnerId()
                ));

        // 2) Знайти співвласника (якщо вказано)
        User coOwner = null;
        if (request.getCoOwnerId() != null) {
            coOwner = userRepository.findById(request.getCoOwnerId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Co-owner not found: " + request.getCoOwnerId()
                    ));
        }

        // 3) Побудувати ентіті рецепту
        UserRecipe recipe = UserRecipe.builder()
                .ingredients(request.getIngredients())
                .description(request.getDescription())
                // Якщо в тебе є поле instructions — додай в CreateRecipeRequest відповідне,
                // або ж поки поклади пустий рядок:
                .instructions("")
                .portionSize(request.getPortionSize())
                .method(request.getMethod())
                .category(null)         // якщо необхідно — додай в CreateRecipeRequest
                .owner(owner)
                .coOwner(coOwner)
                .isPublic(false)        // за замовчуванням приватний
                .build();

        // 4) Зберегти, щоб отримати ID
        UserRecipe saved = recipeRepository.save(recipe);

        // 5) Додати фото, якщо є шляхи
        if (request.getPhotoPaths() != null && !request.getPhotoPaths().isEmpty()) {
            final UserRecipe recipeForPhotos = saved;
            List<RecipePhoto> photos = request.getPhotoPaths().stream()
                    .map(pathStr -> {
                        RecipePhoto p = new RecipePhoto();
                        p.setRecipe(recipeForPhotos);
                        p.setFilePath(pathStr);
                        p.setFileName(Path.of(pathStr).getFileName().toString());
                        p.setContentType(null);  // або визначати по розширенню
                        return p;
                    })
                    .toList();

            saved.setPhotos(photos);
            saved = recipeRepository.save(saved);
        }

        // 6) Маппінг в DTO
        RecipeDTO dto = toDto(saved);

        // 7) Повернути обгортку Result; використовуй відповідний ResultCase
        return Result.success(dto, new SuccessCase("Recipe created successfully"));
    }
    @Override
    public Result<RecipeDTO> update(RecipeDTO request) {
        // 1) Знайти існуючий рецепт
        UserRecipe existing = recipeRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Recipe not found: " + request.getId()
                ));

        // 2) Оновити поля
        existing.setIngredients(request.getIngredients());
        existing.setDescription(request.getDescription());
        existing.setInstructions(request.getInstruction());
        existing.setPortionSize(request.getPortionSize());
        existing.setMethod(request.getMethod());
        existing.setCategory(request.getCategory());

        // 3) Оновити coOwner (якщо прийшов новий)
        if (request.getCoOwner() != null) {
            Long coOwnerId = request.getCoOwner().getId();
            User coOwner = userRepository.findById(coOwnerId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Co-owner not found: " + coOwnerId
                    ));
            existing.setCoOwner(coOwner);
        } else {
            existing.setCoOwner(null);
        }

        // 4) (опційно) якщо треба оновити фото, то заміни список photoPaths аналогічно create(...)
        //    наприклад:
        //    existing.getPhotos().clear();
        //    for (RecipePhoto p : convertFromDtoPhotos(request.getPhotos())) { … }

        // 5) Зберегти оновлений рецепт
        UserRecipe saved = recipeRepository.save(existing);

        // 6) Змоделити DTO і повернути успіх
        RecipeDTO dto = toDto(saved);
        return Result.success(dto, new SuccessCase("Recipe updated successfully"));
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
