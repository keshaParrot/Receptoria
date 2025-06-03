package github.projectgroup.receptoria.services.interfaces;

import github.projectgroup.receptoria.domain.dtos.CreateRecipeRequest;
import github.projectgroup.receptoria.domain.dtos.RecipeDTO;
import github.projectgroup.receptoria.domain.enities.RecipeIngredients;
import github.projectgroup.receptoria.domain.enities.UserRecipe;
import github.projectgroup.receptoria.utils.result.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecipeService {

    /**
     * Отримати рецепт за його ID.
     *
     * @param id ідентифікатор рецепту
     * @return DTO з даними рецепту
     */
    Result<RecipeDTO> getById(Long id);

    /**
     * Повернути всі рецепти певного користувача.
     * @param userId ідентифікатор користувача
     * @param pageable параметри пагінації
     * @return сторінка сутностей UserRecipe
     */
    Page<UserRecipe> getAllByUserId(Long userId, Pageable pageable);

    /**
     * Повернути всі збережені рецепти певного користувача у вигляді DTO.
     * @param userId ідентифікатор користувача
     * @param pageable параметри пагінації
     * @return сторінка DTO рецептів
     */
    Page<RecipeDTO> getAllSavedByUserId(Long userId, Pageable pageable);

    /**
     * Знайти рецепти за списком інгредієнтів та методом приготування.
     * @param ingredients список інгредієнтів для фільтрації
     * @param cookingMethod метод приготування
     * @param pageable параметри пагінації
     * @return сторінка сутностей UserRecipe
     */
    Page<UserRecipe> findAllSorted(
            List<String> ingredients,
            String cookingMethod,
            Pageable pageable
    );

    /**
     * Створити новий рецепт.
     * @param request дані для створення
     * @return створений рецепт у вигляді DTO
     */
    Result<RecipeDTO> create(CreateRecipeRequest request);

    /**
     * Оновити існуючий рецепт.
     * @param request дані для оновлення
     * @return оновлений рецепт у вигляді DTO
     */
    Result<RecipeDTO> update(RecipeDTO request);

    /**
     * Видалити рецепт за ID.
     * @param id ідентифікатор рецепту
     * @return true, якщо видалення успішне
     */
    boolean deleteById(Long id);

    /**
     * Встановити публічний/приватний статус рецепту.
     * @param id ідентифікатор рецепту
     * @param status true — зробити публічним, false — прихованим
     */
    void setPublicStatus(Long id, boolean status);
}