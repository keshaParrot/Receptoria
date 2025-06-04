package github.projectgroup.receptoriaApp.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import github.projectgroup.receptoriaApp.data.models.*
import github.projectgroup.receptoriaApp.ui.theme.Orange500
import github.projectgroup.receptoriaApp.ui.theme.Green500

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userId: Long,
    isOwnProfile: Boolean = true,
    onNavigateToFollowers: (Long) -> Unit,
    onNavigateToFollowing: (Long) -> Unit,
    onNavigateToRecipeDetail: (Long) -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showCreateRecipeDialog by remember { mutableStateOf(false) }
    var showRecipeDetailDialog by remember { mutableStateOf<RecipeDTO?>(null) }

    LaunchedEffect(userId) {
        viewModel.loadUserProfile(userId)
        viewModel.loadUserRecipes(userId, isOwnProfile)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Profile Header
        uiState.user?.let { user ->
            ProfileHeader(
                user = user,
                isOwnProfile = isOwnProfile,
                onFollowersClick = { onNavigateToFollowers(user.id) },
                onFollowingClick = { onNavigateToFollowing(user.id) },
                onCreateRecipeClick = { showCreateRecipeDialog = true }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Recipe Tabs
        RecipeTabSection(
            selectedTab = uiState.selectedTab,
            onTabSelected = viewModel::selectTab,
            recipes = uiState.recipes,
            isLoading = uiState.isLoading,
            onRecipeClick = { recipe ->
                showRecipeDetailDialog = recipe
            }
        )
    }

    // Create Recipe Dialog
    if (showCreateRecipeDialog) {
        CreateRecipeDialog(
            onDismiss = { showCreateRecipeDialog = false },
            onCreateRecipe = { request ->
                viewModel.createRecipe(request)
                showCreateRecipeDialog = false
            }
        )
    }

    // Recipe Detail Dialog
    showRecipeDetailDialog?.let { recipe ->
        RecipeDetailDialog(
            recipe = recipe,
            onDismiss = { showRecipeDetailDialog = null },
            onSaveRecipe = { viewModel.saveRecipe(recipe.id) },
            onAddReaction = { rating, content ->
                viewModel.addReaction(recipe.id, rating, content)
            }
        )
    }
}

@Composable
fun ProfileHeader(
    user: UserDTO,
    isOwnProfile: Boolean,
    onFollowersClick: () -> Unit,
    onFollowingClick: () -> Unit,
    onCreateRecipeClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Picture
            AsyncImage(
                model = "/placeholder.svg?height=100&width=100",
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // User Info
            Text(
                text = "${user.firstName} ${user.lastName}",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "@${user.username}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    count = user.followers,
                    label = "Підписники",
                    onClick = onFollowersClick
                )
                StatItem(
                    count = user.following,
                    label = "Підписки",
                    onClick = onFollowingClick
                )
            }

            if (isOwnProfile) {
                Spacer(modifier = Modifier.height(16.dp))

                // Create Recipe Button
                Button(
                    onClick = onCreateRecipeClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Orange500
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Створити рецепт")
                }
            }
        }
    }
}

@Composable
fun StatItem(
    count: Int,
    label: String,
    onClick: () -> Unit
) {
    TextButton(onClick = onClick) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Orange500
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun RecipeTabSection(
    selectedTab: RecipeTab,
    onTabSelected: (RecipeTab) -> Unit,
    recipes: List<RecipeDTO>,
    isLoading: Boolean,
    onRecipeClick: (RecipeDTO) -> Unit
) {
    Column {
        // Tab Row
        TabRow(
            selectedTabIndex = selectedTab.ordinal,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = Orange500
        ) {
            RecipeTab.values().forEach { tab ->
                Tab(
                    selected = selectedTab == tab,
                    onClick = { onTabSelected(tab) },
                    text = {
                        Text(
                            text = when (tab) {
                                RecipeTab.MY_RECIPES -> "Мої рецепти"
                                RecipeTab.SAVED_RECIPES -> "Збережені"
                                RecipeTab.CO_AUTHORED -> "Співавтор"
                            }
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Recipe Grid
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Orange500)
            }
        } else if (recipes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (selectedTab) {
                        RecipeTab.MY_RECIPES -> "У вас ще немає рецептів"
                        RecipeTab.SAVED_RECIPES -> "У вас немає збережених рецептів"
                        RecipeTab.CO_AUTHORED -> "У вас немає рецептів як співавтор"
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(recipes) { recipe ->
                    RecipeCard(
                        recipe = recipe,
                        onClick = { onRecipeClick(recipe) }
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeCard(
    recipe: RecipeDTO,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.8f),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Recipe Image
            AsyncImage(
                model = recipe.photos.firstOrNull()?.url ?: "/placeholder.svg?height=120&width=160",
                contentDescription = recipe.description,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = recipe.description.take(50) + if (recipe.description.length > 50) "..." else "",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "★",
                        color = Orange500,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = String.format("%.1f", recipe.averageRating),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}
