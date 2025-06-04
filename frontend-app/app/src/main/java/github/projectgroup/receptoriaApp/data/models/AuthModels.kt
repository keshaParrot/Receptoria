package github.projectgroup.receptoriaApp.data.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

@Serializable
data class UserRegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

@Serializable
data class ChangePasswordRequest(
    val newPassword: String
)

@Serializable
data class AuthResponse(
    val success: Boolean,
    val token: String?,
    val message: String?
)

@Serializable
data class ApiResponse(
    val success: Boolean,
    val message: String?
)

// Добавляем AuthUiState для SimpleAuthViewModel
data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val token: String? = null,
    val userId: Long? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null
)
