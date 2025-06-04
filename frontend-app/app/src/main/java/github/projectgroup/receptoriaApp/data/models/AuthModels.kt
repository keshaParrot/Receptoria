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
    val token: String? = null,
    val message: String? = null,
    val success: Boolean = false
)

@Serializable
data class ApiResponse(
    val message: String,
    val success: Boolean
)
