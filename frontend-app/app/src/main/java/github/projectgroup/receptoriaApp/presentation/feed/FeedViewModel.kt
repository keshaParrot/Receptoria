package github.projectgroup.receptoriaApp.presentation.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import github.projectgroup.receptoriaApp.data.models.RecipeDTO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FeedViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    fun loadFeed() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            // TODO: Replace with actual API call
            delay(1000) // Имитация задержки сети
            
            val mockRecipes = createMockRecipes()
            
            _uiState.value = _uiState.value.copy(
                recipes = mockRecipes,
                allRecipes = mockRecipes,
                isLoading = false
            )
        }
    }

    fun applyFilters(ingredients: List<String>, cookingMethod: String?) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            // TODO: Replace with actual API call
            delay(500) // Имитация задержки сети
            
            val filteredRecipes = _uiState.value.allRecipes.filter { recipe ->
                val methodMatch = cookingMethod.isNullOrEmpty() || recipe.method == cookingMethod
                val ingredientsMatch = ingredients.isEmpty() || 
                    recipe.ingredients.any { ingredient -> 
                        ingredients.any { it.lowercase() in ingredient.name.lowercase() }
                    }
                
                methodMatch && ingredientsMatch
            }
            
            _uiState.value = _uiState.value.copy(
                recipes = filteredRecipes,
                isLoading = false
            )
        }
    }

    fun searchRecipes(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                _uiState.value = _uiState.value.copy(
                    recipes = _uiState.value.allRecipes
                )
                return@launch
            }
            
            val searchResults = _uiState.value.allRecipes.filter { recipe ->
                recipe.description.contains(query, ignoreCase = true) ||
                recipe.owner.firstName.contains(query, ignoreCase = true) ||
                recipe.owner.lastName.contains(query, ignoreCase = true) ||
                recipe.ingredients.any { it.name.contains(query, ignoreCase = true) }
            }
            
            _uiState.value = _uiState.value.copy(
                recipes = searchResults
            )
        }
    }

    // Создание тестовых данных для разработки
    private fun createMockRecipes(): List<RecipeDTO> {
        return listOf(
            createMockRecipe(
                id = 1L,
                description = "Смачні млинці на молоці",
                method = "FRYING",
                category = "BREAKFAST",
                rating = 4.5f
            ),
            createMockRecipe(
                id = 2L,
                description = "Борщ український",
                method = "BOILING",
                category = "SOUP",
                rating = 5.0f
            ),
            createMockRecipe(
                id = 3L,
                description = "Салат Цезар",
                method = "RAW",
                category = "SALAD",
                rating = 4.2f
            ),
            createMockRecipe(
                id = 4L,
                description = "Вареники з картоплею",
                method = "BOILING",
                category = "MAIN_COURSE",
                rating = 4.8f
            ),
            createMockRecipe(
                id = 5L,
                description = "Шоколадний торт",
                method = "BAKING",
                category = "DESSERT",
                rating = 4.7f
            )
        )
    }

    private fun createMockRecipe(
        id: Long,
        description: String,
        method: String,
        category: String,
        rating: Float
    ): RecipeDTO {
        return RecipeDTO(
            id = id,
            ingredients = listOf(
                github.projectgroup.receptoriaApp.data.models.RecipeIngredients("Борошно", "500", "г"),
                github.projectgroup.receptoriaApp.data.models.RecipeIngredients("Молоко", "250", "мл")
            ),
            description = description,
            instruction = "Змішати всі інгредієнти та приготувати за рецептом",
            portionSize = 4,
            method = method,
            category = category,
            owner = github.projectgroup.receptoriaApp.data.models.UserPreviewDTO(1L, "Іван", "Петренко"),
            photos = listOf(
                github.projectgroup.receptoriaApp.data.models.RecipePhotoInfo("/placeholder.svg?height=300&width=500", "recipe.jpg", "image/jpeg")
            ),
            averageRating = rating
        )
    }
}

data class FeedUiState(
    val recipes: List<RecipeDTO> = emptyList(),
    val allRecipes: List<RecipeDTO> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
