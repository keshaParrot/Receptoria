package github.projectgroup.receptoriaApp.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import github.projectgroup.receptoriaApp.data.models.RecipeDTO
import github.projectgroup.receptoriaApp.ui.theme.Orange500
import github.projectgroup.receptoriaApp.ui.theme.Green500

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailDialog(
    recipe: RecipeDTO,
    onDismiss: () -> Unit,
    onSaveRecipe: () -> Unit,
    onAddReaction: (Float, String) -> Unit
) {
    var rating by remember { mutableStateOf(0f) }
    var comment by remember { mutableStateOf("") }
    var showReactionForm by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            LazyColumn(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    // Header with actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Деталі рецепту",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Orange500
                        )
                        
                        Row {
                            IconButton(onClick = onSaveRecipe) {
                                Icon(
                                    imageVector = Icons.Default.Bookmark,
                                    contentDescription = "Зберегти",
                                    tint = Orange500
                                )
                            }
                            
                            TextButton(onClick = onDismiss) {
                                Text("Закрити")
                            }
                        }
                    }
                }

                // Recipe Photos
                if (recipe.photos.isNotEmpty()) {
                    item {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(recipe.photos) { photo ->
                                AsyncImage(
                                    model = photo.url,
                                    contentDescription = "Recipe photo",
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }

                item {
                    // Recipe Info
                    Text(
                        text = recipe.description,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                item {
                    // Owner info
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Автор: ${recipe.owner.firstName} ${recipe.owner.lastName}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        
                        recipe.coOwner?.let { coOwner ->
                            Text(
                                text = " • Співавтор: ${coOwner.firstName} ${coOwner.lastName}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                item {
                    // Rating
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Рейтинг: ")
                        repeat(5) { index ->
                            Icon(
                                imageVector = if (index < recipe.averageRating) Icons.Filled.Star else Icons.Outlined.Star,
                                contentDescription = null,
                                tint = Orange500,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            text = " (${String.format("%.1f", recipe.averageRating)})",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                item {
                    // Recipe details
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Деталі",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Medium
                                )
                            )
                            Text("Порції: ${recipe.portionSize}")
                            Text("Метод: ${recipe.method}")
                            Text("Категорія: ${recipe.category}")
                        }
                    }
                }

                item {
                    // Ingredients
                    Text(
                        text = "Інгредієнти",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                    
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            recipe.ingredients.forEach { ingredient ->
                                Text(
                                    text = "• ${ingredient.name} - ${ingredient.amount} ${ingredient.unit}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                item {
                    // Instructions
                    Text(
                        text = "Інструкції",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                    
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        Text(
                            text = recipe.instruction,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                item {
                    // Add Reaction Section
                    if (!showReactionForm) {
                        Button(
                            onClick = { showReactionForm = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Green500
                            )
                        ) {
                            Text("Додати відгук")
                        }
                    } else {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = "Ваш відгук",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                                
                                // Star Rating
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Оцінка: ")
                                    repeat(5) { index ->
                                        IconButton(
                                            onClick = { rating = (index + 1).toFloat() }
                                        ) {
                                            Icon(
                                                imageVector = if (index < rating) Icons.Filled.Star else Icons.Outlined.Star,
                                                contentDescription = null,
                                                tint = Orange500
                                            )
                                        }
                                    }
                                }
                                
                                OutlinedTextField(
                                    value = comment,
                                    onValueChange = { comment = it },
                                    label = { Text("Коментар") },
                                    modifier = Modifier.fillMaxWidth(),
                                    minLines = 3,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Orange500,
                                        focusedLabelColor = Orange500
                                    )
                                )
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = { 
                                            showReactionForm = false
                                            rating = 0f
                                            comment = ""
                                        },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Скасувати")
                                    }
                                    
                                    Button(
                                        onClick = {
                                            onAddReaction(rating, comment)
                                            showReactionForm = false
                                            rating = 0f
                                            comment = ""
                                        },
                                        modifier = Modifier.weight(1f),
                                        enabled = rating > 0,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Green500
                                        )
                                    ) {
                                        Text("Відправити")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
