package github.projectgroup.receptoriaApp.presentation.auth

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.*

class SimpleAuthViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(username: String, password: String) {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            isLoggedIn = true,
            token = "mock_token"
        )
    }

    fun register(username: String, email: String, password: String) {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            successMessage = "Регистрация успешна!"
        )
    }

    fun sendResetPasswordRequest(email: String) {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            successMessage = "Письмо отправлено!"
        )
    }

    fun changePassword(token: String, newPassword: String) {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            successMessage = "Пароль изменен!"
        )
    }
}
