package github.projectgroup.receptoriaApp.presentation.feed

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import github.projectgroup.receptoriaApp.ui.theme.Orange500
import github.projectgroup.receptoriaApp.ui.theme.Green500
import github.projectgroup.receptoriaApp.presentation.utils.formatEnumName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDialog(
    onDismiss: () -> Unit,
    onApplyFilters: (List<String>, String?) -> Unit
) {
    var ingredients by remember { mutableStateOf(listOf("")) }
    var selectedCookingMethod by remember { mutableStateOf<String?>(null) }
    val cookingMethods = listOf(
        "BAKING", "FRYING", "AIR_FRYING", "BOILING",
        "STEAMING", "GRILLING", "RAW"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // Header with close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Фільтри пошуку",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Orange500
                    )

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Закрити"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Cooking Method Filter
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
                                value = selectedCookingMethod?.let { formatEnumName(it) } ?: "Всі методи",
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMethod) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Orange500,
                                    unfocusedBorderColor = Orange500
                                )
                            )

                            ExposedDropdownMenu(
                                expanded = expandedMethod,
                                onDismissRequest = { expandedMethod = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Всі методи") },
                                    onClick = {
                                        selectedCookingMethod = null
                                        expandedMethod = false
                                    }
                                )

                                cookingMethods.forEach { method ->
                                    DropdownMenuItem(
                                        text = { Text(formatEnumName(method)) },
                                        onClick = {
                                            selectedCookingMethod = method
                                            expandedMethod = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Ingredients Filter
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
                                    ingredients = ingredients + ""
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

                    items(ingredients.indices.toList()) { index ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = ingredients[index],
                                onValueChange = {
                                    ingredients = ingredients.toMutableList().apply {
                                        set(index, it)
                                    }
                                },
                                label = { Text("Інгредієнт ${index + 1}") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Orange500,
                                    focusedLabelColor = Orange500
                                )
                            )

                            if (ingredients.size > 1) {
                                IconButton(
                                    onClick = {
                                        ingredients = ingredients.toMutableList().apply {
                                            removeAt(index)
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Видалити",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }

                    // Apply Button
                    item {
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                val filteredIngredients = ingredients.filter { it.isNotBlank() }
                                onApplyFilters(
                                    filteredIngredients,
                                    selectedCookingMethod
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Green500
                            )
                        ) {
                            Text("Застосувати фільтри")
                        }
                    }
                }
            }
        }
    }
}
