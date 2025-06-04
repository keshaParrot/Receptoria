package github.projectgroup.receptoriaApp.presentation.utils

/**
 * Форматирует имя перечисления для отображения пользователю.
 * Преобразует "SNAKE_CASE" в "Snake Case"
 */
fun formatEnumName(name: String): String {
    return name.replace("_", " ").split(" ").joinToString(" ") {
        it.lowercase().replaceFirstChar { char -> char.uppercase() }
    }
}
