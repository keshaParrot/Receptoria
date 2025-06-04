package github.projectgroup.receptoriaApp.presentation.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import github.projectgroup.receptoriaApp.data.models.RecipeDTO
import github.projectgroup.receptoriaApp.ui.theme.Orange500
import coil.compose.AsyncImage
import androidx.compose.foundation.BorderStroke
import github.projectgroup.receptoriaApp.presentation.utils.formatEnumName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    onNavigateToRecipeDetail: (Long) -> Unit,
    onNavigateToSearch: () -> Unit,
    viewModel: FeedViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showFilterDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadFeed()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search Bar
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToSearch() },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(1.dp, Orange500.copy(alpha = 0.5f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Пошук",
                    tint = Orange500.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Пошук рецептів",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "Фільтри",
                    tint = Orange500.copy(alpha = 0.7f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Feed Title
        Text(
            text = "Стрічка рецептів",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Feed Content
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Orange500)
            }
        } else if (uiState.recipes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Немає рецептів для відображення",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.recipes) { recipe ->
                    RecipeCard(
                        recipe = recipe,
                        onClick = { onNavigateToRecipeDetail(recipe.id) }
                    )
                }
            }
        }
    }

    // Filter Dialog
    if (showFilterDialog) {
        FilterDialog(
            onDismiss = { showFilterDialog = false },
            onApplyFilters = { ingredients, cookingMethod ->
                viewModel.applyFilters(ingredients, cookingMethod)
                showFilterDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeCard(
    recipe: RecipeDTO,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Recipe Image
            if (recipe.photos.isNotEmpty()) {
                AsyncImage(
                    model = recipe.photos.first().url,
                    contentDescription = recipe.description,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Немає фото")
                }
            }

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Recipe Title
                Text(
                    text = recipe.description,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Author
                Text(
                    text = "Автор: ${recipe.owner.firstName} ${recipe.owner.lastName}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Recipe Info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Category and Method
                    Column {
                        Text(
                            text = "Категорія: ${formatEnumName(recipe.category)}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "Метод: ${formatEnumName(recipe.method)}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    // Rating
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = Orange500,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("%.1f", recipe.averageRating),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
