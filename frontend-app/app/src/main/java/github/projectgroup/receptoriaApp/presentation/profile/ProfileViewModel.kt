package github.projectgroup.receptoriaApp.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import github.projectgroup.receptoriaApp.data.models.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun loadUserProfile(userId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // TODO: Replace with actual API call
            val mockUser = UserDTO(
                id = userId,
                firstName = "Іван",
                lastName = "Петренко",
                email = "ivan@example.com",
                username = "ivan_petrenko",
                followers = 150,
                following = 89
            )

            _uiState.value = _uiState.value.copy(
                user = mockUser,
                isLoading = false
            )
        }
    }

    fun loadUserRecipes(userId: Long, isOwnProfile: Boolean) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // Загружаем все типы рецептов
            val myRecipes = createMockMyRecipes()
            val savedRecipes = createMockSavedRecipes()
            val coAuthoredRecipes = createMockCoAuthoredRecipes()

            _uiState.value = _uiState.value.copy(
                myRecipes = myRecipes,
                savedRecipes = savedRecipes,
                coAuthoredRecipes = coAuthoredRecipes,
                recipes = when (_uiState.value.selectedTab) {
                    RecipeTab.MY_RECIPES -> myRecipes
                    RecipeTab.SAVED_RECIPES -> savedRecipes
                    RecipeTab.CO_AUTHORED -> coAuthoredRecipes
                },
                isLoading = false
            )
        }
    }

    fun selectTab(tab: RecipeTab) {
        _uiState.value = _uiState.value.copy(
            selectedTab = tab,
            recipes = when (tab) {
                RecipeTab.MY_RECIPES -> _uiState.value.myRecipes
                RecipeTab.SAVED_RECIPES -> _uiState.value.savedRecipes
                RecipeTab.CO_AUTHORED -> _uiState.value.coAuthoredRecipes
            }
        )
    }

    fun createRecipe(request: CreateRecipeRequest) {
        viewModelScope.launch {
            // TODO: Implement API call to create recipe
            _uiState.value = _uiState.value.copy(
                successMessage = "Рецепт успішно створено!"
            )

            // Обновляем список "Мої рецепти" после создания
            val newRecipe = RecipeDTO(
                id = System.currentTimeMillis(),
                ingredients = request.ingredients,
                description = request.description,
                instruction = request.instructions,
                portionSize = request.portionSize,
                method = request.method,
                category = request.category,
                owner = UserPreviewDTO(1L, "Іван", "Петренко"),
                photos = emptyList(),
                averageRating = 0f
            )

            val updatedMyRecipes = _uiState.value.myRecipes + newRecipe
            _uiState.value = _uiState.value.copy(
                myRecipes = updatedMyRecipes,
                recipes = if (_uiState.value.selectedTab == RecipeTab.MY_RECIPES) updatedMyRecipes else _uiState.value.recipes
            )
        }
    }

    fun saveRecipe(recipeId: Long) {
        viewModelScope.launch {
            // TODO: Implement API call to save recipe
            _uiState.value = _uiState.value.copy(
                successMessage = "Рецепт збережено!"
            )
        }
    }

    fun addReaction(recipeId: Long, rating: Float, content: String) {
        viewModelScope.launch {
            // TODO: Implement API call to add reaction
            val request = CreateReactionRequest(
                rating = rating,
                ownerId = 1L, // TODO: Get from current user
                content = content,
                ratedRecipeId = recipeId
            )

            _uiState.value = _uiState.value.copy(
                successMessage = "Відгук додано!"
            )
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }

    // Создание тестовых данных для "Мої рецепти"
    private fun createMockMyRecipes(): List<RecipeDTO> {
        return listOf(
            RecipeDTO(
                id = 1L,
                ingredients = listOf(
                    RecipeIngredients("Борошно", "500", "г"),
                    RecipeIngredients("Молоко", "250", "мл"),
                    RecipeIngredients("Яйця", "2", "шт")
                ),
                description = "Смачні млинці на молоці",
                instruction = "Змішати всі інгредієнти та смажити на сковороді",
                portionSize = 4,
                method = "FRYING",
                category = "BREAKFAST",
                owner = UserPreviewDTO(1L, "Іван", "Петренко"),
                photos = listOf(
                    RecipePhotoInfo("/placeholder.svg?height=200&width=300", "pancakes.jpg", "image/jpeg")
                ),
                averageRating = 4.5f
            ),
            RecipeDTO(
                id = 2L,
                ingredients = listOf(
                    RecipeIngredients("Картопля", "1", "кг"),
                    RecipeIngredients("Сіль", "1", "ч.л.")
                ),
                description = "Картопля по-селянськи",
                instruction = "Запекти картоплю в духовці",
                portionSize = 6,
                method = "BAKING",
                category = "SIDE_DISH",
                owner = UserPreviewDTO(1L, "Іван", "Петренко"),
                photos = listOf(
                    RecipePhotoInfo("/placeholder.svg?height=200&width=300", "potatoes.jpg", "image/jpeg")
                ),
                averageRating = 4.2f
            )
        )
    }

    // Создание тестовых данных для "Збережені"
    private fun createMockSavedRecipes(): List<RecipeDTO> {
        return listOf(
            RecipeDTO(
                id = 3L,
                ingredients = listOf(
                    RecipeIngredients("Буряк", "3", "шт"),
                    RecipeIngredients("Капуста", "300", "г"),
                    RecipeIngredients("Морква", "2", "шт")
                ),
                description = "Український борщ",
                instruction = "Варити овочі та додати спеції",
                portionSize = 8,
                method = "BOILING",
                category = "SOUP",
                owner = UserPreviewDTO(2L, "Марія", "Коваленко"),
                photos = listOf(
                    RecipePhotoInfo("/placeholder.svg?height=200&width=300", "borscht.jpg", "image/jpeg")
                ),
                averageRating = 5.0f
            ),
            RecipeDTO(
                id = 4L,
                ingredients = listOf(
                    RecipeIngredients("Салат", "1", "пучок"),
                    RecipeIngredients("Помідори", "2", "шт"),
                    RecipeIngredients("Огірки", "2", "шт")
                ),
                description = "Свіжий овочевий салат",
                instruction = "Нарізати овочі та заправити олією",
                portionSize = 2,
                method = "RAW",
                category = "SALAD",
                owner = UserPreviewDTO(3L, "Олена", "Сидоренко"),
                photos = listOf(
                    RecipePhotoInfo("/placeholder.svg?height=200&width=300", "salad.jpg", "image/jpeg")
                ),
                averageRating = 4.3f
            )
        )
    }

    // Создание тестовых данных для "Співавтор"
    private fun createMockCoAuthoredRecipes(): List<RecipeDTO> {
        return listOf(
            RecipeDTO(
                id = 5L,
                ingredients = listOf(
                    RecipeIngredients("Шоколад", "200", "г"),
                    RecipeIngredients("Масло", "100", "г"),
                    RecipeIngredients("Цукор", "150", "г")
                ),
                description = "Шоколадний торт",
                instruction = "Розтопити шоколад та змішати з іншими інгредієнтами",
                portionSize = 12,
                method = "BAKING",
                category = "DESSERT",
                owner = UserPreviewDTO(4L, "Андрій", "Мельник"),
                coOwner = UserPreviewDTO(1L, "Іван", "Петренко"),
                photos = listOf(
                    RecipePhotoInfo("/placeholder.svg?height=200&width=300", "cake.jpg", "image/jpeg")
                ),
                averageRating = 4.8f
            )
        )
    }
}

data class ProfileUiState(
    val user: UserDTO? = null,
    val recipes: List<RecipeDTO> = emptyList(),
    val myRecipes: List<RecipeDTO> = emptyList(),
    val savedRecipes: List<RecipeDTO> = emptyList(),
    val coAuthoredRecipes: List<RecipeDTO> = emptyList(),
    val selectedTab: RecipeTab = RecipeTab.MY_RECIPES,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)
