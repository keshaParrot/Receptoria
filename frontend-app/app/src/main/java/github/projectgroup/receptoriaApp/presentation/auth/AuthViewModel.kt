package github.projectgroup.receptoriaApp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import github.projectgroup.receptoriaApp.data.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            // Для тестирования - имитируем задержку сети
            delay(1000)

            // Для тестирования - всегда успешный вход
            if (true) { // Для реального API замените на проверку ответа
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoggedIn = true,
                    token = "mock_token_123",
                    userId = 1L
                )
                return@launch
            }

            // Реальный код для API (закомментирован для тестирования)
            /*
            authRepository.login(username, password).collect { result ->
                result.fold(
                    onSuccess = { response ->
                        if (response.success && response.token != null) {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                isLoggedIn = true,
                                token = response.token
                            )
                        } else {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                errorMessage = response.message ?: "Помилка входу"
                            )
                        }
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: "Помилка мережі"
                        )
                    }
                )
            }
            */
        }
    }

    // Остальные методы без изменений...
}

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val token: String? = null,
    val userId: Long? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null
)