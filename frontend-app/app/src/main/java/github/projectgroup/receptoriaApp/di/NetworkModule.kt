package github.projectgroup.receptoriaApp.di

import android.content.Context
import github.projectgroup.receptoriaApp.data.SessionManager
import github.projectgroup.receptoriaApp.data.api.AuthApi
import github.projectgroup.receptoriaApp.data.repository.AuthRepository
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object NetworkModule {

    private lateinit var sessionManager: SessionManager

    fun initialize(context: Context) {
        sessionManager = SessionManager(context)
    }

    private val httpClient by lazy {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }

            // Добавляем интерцептор для токена
            engine {
                addInterceptor { chain ->
                    val original = chain.request()
                    val token = sessionManager.getAuthToken()

                    val request = if (token != null) {
                        original.newBuilder()
                            .header("Authorization", "Bearer $token")
                            .build()
                    } else {
                        original
                    }

                    chain.proceed(request)
                }
            }
        }
    }

    private val authApi by lazy {
        AuthApi(httpClient)
    }

    val authRepository by lazy {
        AuthRepository(authApi)
    }

    // Добавьте доступ к SessionManager
    val session get() = sessionManager
}