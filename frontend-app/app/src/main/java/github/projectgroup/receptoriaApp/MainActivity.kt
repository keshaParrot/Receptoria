package github.projectgroup.receptoriaApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import github.projectgroup.receptoriaApp.presentation.auth.*
import github.projectgroup.receptoriaApp.presentation.feed.FeedScreen
import github.projectgroup.receptoriaApp.presentation.feed.SearchScreen
import github.projectgroup.receptoriaApp.presentation.navigation.BottomNavItem
import github.projectgroup.receptoriaApp.presentation.navigation.ReceptoriaBottomNavigation
import github.projectgroup.receptoriaApp.presentation.profile.ProfileScreen
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
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: SimpleAuthViewModel = viewModel()
    val uiState by authViewModel.uiState.collectAsState()

    // Определяем, нужно ли показывать нижнюю навигацию
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: "login"
    val showBottomNav = remember(currentRoute) {
        currentRoute in listOf(
            BottomNavItem.HOME.route,
            BottomNavItem.SEARCH.route,
            "profile/{userId}"
        ) || currentRoute.startsWith("profile/")
    }

    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                ReceptoriaBottomNavigation(
                    currentRoute = when {
                        currentRoute.startsWith("profile/") -> BottomNavItem.PROFILE.route
                        else -> currentRoute
                    },
                    onNavigate = { route ->
                        navigateToBottomNavRoute(navController, route)
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(paddingValues)
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
                        navController.popBackStack()
                    },
                    onLoginClick = {
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }
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

            // Новые экраны
            composable(BottomNavItem.HOME.route) {
                FeedScreen(
                    onNavigateToRecipeDetail = { recipeId ->
                        navController.navigate("recipe/$recipeId")
                    },
                    onNavigateToSearch = {
                        navController.navigate(BottomNavItem.SEARCH.route)
                    }
                )
            }

            composable(BottomNavItem.SEARCH.route) {
                SearchScreen(
                    onNavigateToRecipeDetail = { recipeId ->
                        navController.navigate("recipe/$recipeId")
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable("profile/{userId}") { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId")?.toLongOrNull() ?: 1L
                ProfileScreen(
                    userId = userId,
                    isOwnProfile = true,
                    onNavigateToFollowers = { followerId ->
                        navController.navigate("followers/$followerId")
                    },
                    onNavigateToFollowing = { followingId ->
                        navController.navigate("following/$followingId")
                    },
                    onNavigateToRecipeDetail = { recipeId ->
                        navController.navigate("recipe/$recipeId")
                    }
                )
            }

            // Заглушки для других экранов
            composable("followers/{userId}") { /* TODO */ }
            composable("following/{userId}") { /* TODO */ }
            composable("recipe/{recipeId}") { /* TODO */ }
        }
    }

    // Обработка успешного входа
    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn) {
            // Переходим на экран ленты после успешного входа
            navController.navigate(BottomNavItem.HOME.route) {
                popUpTo("login") { inclusive = true }
            }
        }
    }
}

private fun navigateToBottomNavRoute(navController: NavHostController, route: String) {
    when (route) {
        BottomNavItem.PROFILE.route -> {
            // Для профиля переходим к конкретному маршруту с userId
            navController.navigate("profile/1") {
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
        else -> {
            navController.navigate(route) {
                // Избегаем создания множества копий экранов в стеке навигации
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                // Избегаем создания дубликатов экранов при повторном нажатии
                launchSingleTop = true
                // Восстанавливаем состояние при возврате на экран
                restoreState = true
            }
        }
    }
}
