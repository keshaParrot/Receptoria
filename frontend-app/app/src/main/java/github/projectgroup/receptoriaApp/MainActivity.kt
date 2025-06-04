package github.projectgroup.receptoriaApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import github.projectgroup.receptoriaApp.di.NetworkModule
import github.projectgroup.receptoriaApp.presentation.auth.*
import github.projectgroup.receptoriaApp.ui.theme.ReceptoriaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReceptoriaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AuthNavigation()
                }
            }
        }
    }
}

@Composable
fun AuthNavigation() {
    val navController = rememberNavController()

    // Создаем ViewModel с фабрикой
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(NetworkModule.authRepository)
    )

    val uiState by authViewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginClick = { username, password ->
                    authViewModel.login(username, password)
                },
                onRegisterClick = {
                    navController.navigate("register")
                },
                onForgotPasswordClick = {
                    navController.navigate("reset_password")
                },
                isLoading = uiState.isLoading,
                errorMessage = uiState.errorMessage
            )
        }

        composable("register") {
            RegisterScreen(
                onRegisterClick = { username, email, password ->
                    authViewModel.register(username, email, password)
                },
                onLoginClick = {
                    navController.navigate("login")
                },
                isLoading = uiState.isLoading,
                errorMessage = uiState.errorMessage
            )
        }

        composable("reset_password") {
            ResetPasswordScreen(
                onSendResetRequest = { email ->
                    authViewModel.sendResetPasswordRequest(email)
                },
                onBackClick = {
                    navController.popBackStack()
                },
                isLoading = uiState.isLoading,
                errorMessage = uiState.errorMessage,
                successMessage = uiState.successMessage
            )
        }

        composable("change_password/{token}") { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            ChangePasswordScreen(
                token = token,
                onChangePassword = { tokenParam, newPassword ->
                    authViewModel.changePassword(tokenParam, newPassword)
                },
                onBackToLogin = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                isLoading = uiState.isLoading,
                errorMessage = uiState.errorMessage,
                successMessage = uiState.successMessage
            )
        }
    }

    // Handle successful login
    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn) {
            // Navigate to main app or save token
            // navController.navigate("main_app")
        }
    }
}