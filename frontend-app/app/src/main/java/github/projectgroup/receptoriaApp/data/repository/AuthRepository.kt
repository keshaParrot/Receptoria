package github.projectgroup.receptoriaApp.data.repository

import github.projectgroup.receptoriaApp.data.api.AuthApi
import github.projectgroup.receptoriaApp.data.models.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepository(private val authApi: AuthApi) {

    suspend fun login(username: String, password: String): Flow<Result<AuthResponse>> = flow {
        try {
            val response = authApi.login(LoginRequest(username, password))
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun register(username: String, email: String, password: String): Flow<Result<ApiResponse>> = flow {
        try {
            val response = authApi.register(UserRegisterRequest(username, email, password))
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun sendResetPasswordRequest(email: String): Flow<Result<ApiResponse>> = flow {
        try {
            val response = authApi.sendResetPasswordRequest(email)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun changePassword(token: String, newPassword: String): Flow<Result<ApiResponse>> = flow {
        try {
            val response = authApi.changePassword(token, ChangePasswordRequest(newPassword))
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
