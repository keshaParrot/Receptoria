package github.projectgroup.receptoriaApp.di

import github.projectgroup.receptoriaApp.data.api.AuthApi
import github.projectgroup.receptoriaApp.data.repository.AuthRepository
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object NetworkModule {

    private val httpClient by lazy {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
    }

    private val authApi by lazy {
        AuthApi(httpClient)
    }

    val authRepository by lazy {
        AuthRepository(authApi)
    }
}