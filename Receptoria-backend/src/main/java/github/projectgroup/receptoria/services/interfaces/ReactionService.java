package github.projectgroup.receptoria.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import github.projectgroup.receptoria.domain.dtos.ReactionDTO;
import github.projectgroup.receptoria.domain.dtos.CreateReactionRequest;

public interface ReactionService {

    /**
     * Повернути всі реакції для конкретного рецепту з пагінацією.
     *
     * @param recipeId ідентифікатор рецепту
     * @param pageable параметри пагінації
     * @return сторінка ReactionDTO
     */
    Page<ReactionDTO> getByRecipeId(Long recipeId, Pageable pageable);

    /**
     * Повернути всі реакції конкретного користувача з пагінацією.
     *
     * @param userId   ідентифікатор користувача
     * @param pageable параметри пагінації
     * @return сторінка ReactionDTO
     */
    Page<ReactionDTO> getByUserId(Long userId, Pageable pageable);

    /**
     * Обчислити загальний рейтинг рецепту (наприклад, середнє значення усіх оцінок).
     *
     * @param recipeId ідентифікатор рецепту
     * @return середній рейтинг (float)
     */
    float getRecipeRating(Long recipeId);

    /**
     * Створити нову реакцію (оцінку) до рецепту.
     *
     * @param request дані для створення реакції
     * @return true, якщо створення вдалося
     */
    boolean create(CreateReactionRequest request);

    /**
     * Оновити існуючу реакцію (змінити оцінку).
     *
     * @param id        ідентифікатор реакції
     * @param newRating нове значення рейтингу (float)
     * @return true, якщо оновлення вдалося
     */
    boolean update(Long id, float newRating);

    /**
     * Видалити реакцію за її ID.
     *
     * @param id ідентифікатор реакції
     * @return true, якщо видалення вдалося
     */
    boolean deleteById(Long id);
}
