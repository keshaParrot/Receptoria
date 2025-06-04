package github.projectgroup.receptoriaApp.data.api

import github.projectgroup.receptoriaApp.data.models.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class AuthApi(private val client: HttpClient) {
    private val baseUrl = "http://your-backend-url/api/v1/auth"

    suspend fun login(request: LoginRequest): AuthResponse {
        return client.post("$baseUrl/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun register(request: UserRegisterRequest): ApiResponse {
        return client.post("$baseUrl/register") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun sendResetPasswordRequest(email: String): ApiResponse {
        return client.get("$baseUrl/reset-password") {
            parameter("email", email)
        }.body()
    }

    suspend fun changePassword(token: String, request: ChangePasswordRequest): ApiResponse {
        return client.post("$baseUrl/change-password") {
            parameter("token", token)
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}