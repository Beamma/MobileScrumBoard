package nz.ac.canterbury.seng303.scrumboardmobile.screens.user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.flow.Flow
import androidx.compose.ui.res.stringResource
import nz.ac.canterbury.seng303.scrumboardmobile.R

@Composable
fun UserPreferenceScreen(
    navController: NavController,
    language: Flow<String>,
    onLanguageChangeFn: (String) -> Unit
) {
    // Collect the current language
    val currentLanguage by language.collectAsState(initial = "")

    // States for dropdown menus
    var expandedLanguage by remember { mutableStateOf(false) }

    // List of languages
    val languages = listOf(
        "en" to "English",
        "ja" to "Japanese",
        "ko" to "Korean"
    )

    // Column layout for the screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text(text = stringResource(id = R.string.select_language), style = MaterialTheme.typography.titleMedium)

        // Language selection dropdown
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            OutlinedTextField(
                value = languages.find { it.first == currentLanguage }?.second ?: stringResource(id = R.string.language),
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(id = R.string.language)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedLanguage = true },
                trailingIcon = {
                    IconButton(onClick = { expandedLanguage = true }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = stringResource(id = R.string.dropdown_description))
                    }
                }
            )

            DropdownMenu(
                expanded = expandedLanguage,
                onDismissRequest = { expandedLanguage = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                languages.forEach { (code, name) ->
                    DropdownMenuItem(
                        text = { Text(text = name) },
                        onClick = {
                            onLanguageChangeFn(code) // Call the provided function
                            expandedLanguage = false // Dismiss the dropdown
                        }
                    )
                }
            }
        }
    }
}
