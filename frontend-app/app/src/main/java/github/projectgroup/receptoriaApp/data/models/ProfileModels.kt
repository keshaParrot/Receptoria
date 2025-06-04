package github.projectgroup.receptoriaApp.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val username: String,
    val followers: Int,
    val following: Int
)

@Serializable
data class UserPreviewDTO(
    val id: Long,
    val firstName: String,
    val lastName: String
)

@Serializable
data class RecipePhotoInfo(
    val url: String,
    val fileName: String,
    val contentType: String
)

@Serializable
data class RecipeIngredients(
    val name: String,
    val amount: String,
    val unit: String
)

@Serializable
data class RecipeDTO(
    val id: Long,
    val ingredients: List<RecipeIngredients>,
    val description: String,
    val instruction: String,
    val portionSize: Int,
    val method: String, // CookingMethod enum as string
    val category: String, // MealCategory enum as string
    val owner: UserPreviewDTO,
    val coOwner: UserPreviewDTO? = null,
    val photos: List<RecipePhotoInfo>,
    val isPublic: Boolean = true,
    val averageRating: Float = 0f
)

@Serializable
data class CreateRecipeRequest(
    val ingredients: List<RecipeIngredients>,
    val description: String,
    val instructions: String,
    val category: String,
    val portionSize: Int,
    val method: String,
    val ownerId: Long,
    val coOwnerId: Long? = null,
    val isPublic: Boolean = true
)

@Serializable
data class CreateReactionRequest(
    val rating: Float,
    val ownerId: Long,
    val content: String,
    val ratedRecipeId: Long
)

enum class CookingMethod {
    BAKING, FRYING, AIR_FRYING, BOILING, STEAMING, GRILLING, RAW
}

enum class MealCategory {
    BREAKFAST, LUNCH, DINNER, SNACK, DESSERT, DRINK, SALAD, SOUP,
    APPETIZER, MAIN_COURSE, SIDE_DISH, FAST_FOOD, VEGETARIAN, VEGAN, GLUTEN_FREE
}

enum class RecipeTab {
    MY_RECIPES, SAVED_RECIPES, CO_AUTHORED
}
