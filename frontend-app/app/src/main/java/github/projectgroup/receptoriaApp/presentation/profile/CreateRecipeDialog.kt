package github.projectgroup.receptoriaApp.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import github.projectgroup.receptoriaApp.data.models.*
import github.projectgroup.receptoriaApp.ui.theme.Orange500
import github.projectgroup.receptoriaApp.ui.theme.Green500
import github.projectgroup.receptoriaApp.data.models.enums.CookingMethod
import github.projectgroup.receptoriaApp.data.models.enums.MealCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRecipeDialog(
    onDismiss: () -> Unit,
    onCreateRecipe: (CreateRecipeRequest) -> Unit
) {
    var description by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }
    var portionSize by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(MealCategory.BREAKFAST) }
    var selectedMethod by remember { mutableStateOf(CookingMethod.BAKING) }
    var ingredients by remember { mutableStateOf(listOf(RecipeIngredients("", "", ""))) }
    var isPublic by remember { mutableStateOf(true) }

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
                    Text(
                        text = "Створити рецепт",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Orange500
                    )
                }

                item {
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Опис рецепту") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Orange500,
                            focusedLabelColor = Orange500
                        )
                    )
                }

                item {
                    OutlinedTextField(
                        value = instructions,
                        onValueChange = { instructions = it },
                        label = { Text("Інструкції") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Orange500,
                            focusedLabelColor = Orange500
                        )
                    )
                }

                item {
                    OutlinedTextField(
                        value = portionSize,
                        onValueChange = { portionSize = it },
                        label = { Text("Порції") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Orange500,
                            focusedLabelColor = Orange500
                        )
                    )
                }

                item {
                    Text(
                        text = "Категорія",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )

                    var expandedCategory by remember { mutableStateOf(false) }

                    ExposedDropdownMenuBox(
                        expanded = expandedCategory,
                        onExpandedChange = { expandedCategory = !expandedCategory }
                    ) {
                        OutlinedTextField(
                            value = formatEnumName(selectedCategory.name),
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Orange500,
                                unfocusedBorderColor = Orange500
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = expandedCategory,
                            onDismissRequest = { expandedCategory = false }
                        ) {
                            MealCategory.values().forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(formatEnumName(category.name)) },
                                    onClick = {
                                        selectedCategory = category
                                        expandedCategory = false
                                    }
                                )
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Метод приготування",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )

                    var expandedMethod by remember { mutableStateOf(false) }

                    ExposedDropdownMenuBox(
                        expanded = expandedMethod,
                        onExpandedChange = { expandedMethod = !expandedMethod }
                    ) {
                        OutlinedTextField(
                            value = formatEnumName(selectedMethod.name),
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMethod) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Orange500,
                                unfocusedBorderColor = Orange500
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = expandedMethod,
                            onDismissRequest = { expandedMethod = false }
                        ) {
                            CookingMethod.values().forEach { method ->
                                DropdownMenuItem(
                                    text = { Text(formatEnumName(method.name)) },
                                    onClick = {
                                        selectedMethod = method
                                        expandedMethod = false
                                    }
                                )
                            }
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Інгредієнти",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Medium
                            )
                        )

                        IconButton(
                            onClick = {
                                ingredients = ingredients + RecipeIngredients("", "", "")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Додати інгредієнт",
                                tint = Orange500
                            )
                        }
                    }
                }

                itemsIndexed(ingredients) { index, ingredient ->
                    IngredientItem(
                        ingredient = ingredient,
                        onIngredientChange = { newIngredient ->
                            ingredients = ingredients.toMutableList().apply {
                                set(index, newIngredient)
                            }
                        },
                        onRemove = if (ingredients.size > 1) {
                            {
                                ingredients = ingredients.toMutableList().apply {
                                    removeAt(index)
                                }
                            }
                        } else null
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Публічний рецепт")
                        Switch(
                            checked = isPublic,
                            onCheckedChange = { isPublic = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Orange500,
                                checkedTrackColor = Orange500.copy(alpha = 0.5f)
                            )
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Orange500
                            )
                        ) {
                            Text("Скасувати")
                        }

                        Button(
                            onClick = {
                                val request = CreateRecipeRequest(
                                    ingredients = ingredients.filter { it.name.isNotBlank() },
                                    description = description,
                                    instructions = instructions,
                                    category = selectedCategory.name,
                                    portionSize = portionSize.toIntOrNull() ?: 1,
                                    method = selectedMethod.name,
                                    ownerId = 1L, // TODO: Get from current user
                                    isPublic = isPublic
                                )
                                onCreateRecipe(request)
                            },
                            modifier = Modifier.weight(1f),
                            enabled = description.isNotBlank() && instructions.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Green500
                            )
                        ) {
                            Text("Створити")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IngredientItem(
    ingredient: RecipeIngredients,
    onIngredientChange: (RecipeIngredients) -> Unit,
    onRemove: (() -> Unit)?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Інгредієнт",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    )
                )

                onRemove?.let {
                    IconButton(onClick = it) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Видалити",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            OutlinedTextField(
                value = ingredient.name,
                onValueChange = { onIngredientChange(ingredient.copy(name = it)) },
                label = { Text("Назва") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Orange500,
                    focusedLabelColor = Orange500
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = ingredient.amount,
                    onValueChange = { onIngredientChange(ingredient.copy(amount = it)) },
                    label = { Text("Кількість") },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Orange500,
                        focusedLabelColor = Orange500
                    )
                )

                OutlinedTextField(
                    value = ingredient.unit,
                    onValueChange = { onIngredientChange(ingredient.copy(unit = it)) },
                    label = { Text("Одиниця") },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Orange500,
                        focusedLabelColor = Orange500
                    )
                )
            }
        }
    }
}

// Вспомогательная функция для форматирования имен перечислений
fun formatEnumName(name: String): String {
    return name.replace("_", " ").split(" ").joinToString(" ") {
        it.lowercase().replaceFirstChar { char -> char.uppercase() }
    }
}
