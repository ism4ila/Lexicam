package com.traduction.traductiondeslangueslocales.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
// Assurez-vous que cet import est présent si vous utilisez Icons.Outlined.Translate
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager // Pour la copie
import androidx.compose.ui.platform.LocalContext // Pour Toasts éventuels
import androidx.compose.ui.text.AnnotatedString // Pour la copie
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.traduction.traductiondeslangueslocales.data.HistoryManager
import com.traduction.traductiondeslangueslocales.data.TranslationService
import com.traduction.traductiondeslangueslocales.ui.components.SpecialCharactersKeyboard
import kotlinx.coroutines.delay // Supprimé 'Color' de compose.ui.graphics car non utilisé directement, MaterialTheme gère les couleurs
import kotlinx.coroutines.launch

/**
 * Écran de traduction avec clavier pour caractères spéciaux et historique
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslationScreen() {
    var textFieldValueState by remember { mutableStateOf(TextFieldValue("")) }
    var translatedText by remember { mutableStateOf("") }
    var sourceLanguage by remember { mutableStateOf("Français") }
    var targetLanguage by remember { mutableStateOf("Gbaya") }
    var isTranslating by remember { mutableStateOf(false) }
    var showSpecialKeyboard by remember { mutableStateOf(false) }
    var translationError by remember { mutableStateOf<String?>(null) } // Pour gérer les erreurs de traduction

    val languages = remember { listOf("Français", "Gbaya", "Fulfuldé") } // 'remember' pour la liste
    var sourceLanguageMenuExpanded by remember { mutableStateOf(false) }
    var targetLanguageMenuExpanded by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current // Pour afficher des Toasts (optionnel)

    val sourceText = textFieldValueState.text
    val isSourceLangSpecial = sourceLanguage == "Gbaya" || sourceLanguage == "Fulfuldé"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // Bon pour le contenu long
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Traduction",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        // Sélection des langues
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LanguageSelector(
                        label = sourceLanguage,
                        expanded = sourceLanguageMenuExpanded,
                        onExpandToggle = { sourceLanguageMenuExpanded = !sourceLanguageMenuExpanded },
                        languages = languages,
                        onLanguageSelected = { lang ->
                            sourceLanguage = lang
                            sourceLanguageMenuExpanded = false
                            // Affiche le clavier si la nouvelle langue source est spéciale et qu'il était masqué,
                            // ou le masque si la nouvelle langue source n'est pas spéciale.
                            showSpecialKeyboard = (lang == "Gbaya" || lang == "Fulfuldé")
                            // Si la langue source et cible deviennent identiques, en choisir une autre pour la cible
                            if (sourceLanguage == targetLanguage) {
                                targetLanguage = languages.firstOrNull { it != sourceLanguage } ?: ""
                            }
                        }
                    )

                    IconButton(onClick = {
                        if (sourceLanguage != targetLanguage) {
                            val tempLang = sourceLanguage
                            sourceLanguage = targetLanguage
                            targetLanguage = tempLang

                            val tempSourceText = sourceText // textFieldValueState.text
                            textFieldValueState = TextFieldValue(translatedText)
                            translatedText = tempSourceText

                            showSpecialKeyboard = (sourceLanguage == "Gbaya" || sourceLanguage == "Fulfuldé")
                        }
                    }) {
                        Icon(Icons.Default.SwapHoriz, contentDescription = "Échanger les langues")
                    }

                    LanguageSelector(
                        label = targetLanguage,
                        expanded = targetLanguageMenuExpanded,
                        onExpandToggle = { targetLanguageMenuExpanded = !targetLanguageMenuExpanded },
                        languages = languages.filter { it != sourceLanguage }, // Ne pas permettre la même langue que la source
                        onLanguageSelected = { lang ->
                            targetLanguage = lang
                            targetLanguageMenuExpanded = false
                        }
                    )
                }
            }
        }

        // Zone de texte source
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Texte à traduire ($sourceLanguage)",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = textFieldValueState,
                    onValueChange = {
                        textFieldValueState = it
                        translationError = null // Effacer l'erreur en tapant
                    },
                    modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 120.dp), // Hauteur minimale
                    placeholder = { Text("Entrez votre texte ici") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isSourceLangSpecial) {
                        TextButton(onClick = { showSpecialKeyboard = !showSpecialKeyboard }) {
                            Text(if (showSpecialKeyboard) "Masquer caractères" else "Afficher caractères")
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f)) // Pour pousser le bouton Clear à droite
                    }
                    IconButton(onClick = {
                        textFieldValueState = TextFieldValue("")
                        translatedText = ""
                        translationError = null
                    }) {
                        Icon(Icons.Default.Clear, contentDescription = "Effacer le texte", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }

        if (showSpecialKeyboard && isSourceLangSpecial) {
            SpecialCharactersKeyboard(
                selectedLanguage = sourceLanguage, // Doit être la langue source
                onCharacterSelected = { charStr ->
                    val currentText = textFieldValueState.text
                    val selection = textFieldValueState.selection
                    val newText = currentText.replaceRange(selection.start, selection.end, charStr)
                    val newCursorPosition = selection.start + charStr.length
                    textFieldValueState = TextFieldValue(text = newText, selection = TextRange(newCursorPosition))
                }
            )
        }

        Button(
            onClick = {
                if (sourceText.isNotBlank() && sourceLanguage != targetLanguage) {
                    isTranslating = true
                    translationError = null
                    coroutineScope.launch {
                        try {
                            // Simuler un délai de traduction (à remplacer par votre vrai service)
                            delay(800)
                            val result = TranslationService.translate(
                                text = sourceText,
                                sourceLang = sourceLanguage,
                                targetLang = targetLanguage
                            )
                            translatedText = result

                            HistoryManager.addTranslation(
                                sourceText = sourceText,
                                translatedText = translatedText,
                                sourceLanguage = sourceLanguage,
                                targetLanguage = targetLanguage
                            )
                            // Optionnel: Toast.makeText(context, "Traduction ajoutée à l'historique", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            // Gérer l'erreur de traduction (ex: service non disponible)
                            translationError = "Erreur de traduction: ${e.message}"
                            translatedText = "" // Effacer la traduction précédente en cas d'erreur
                        } finally {
                            isTranslating = false
                        }
                    }
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            enabled = sourceText.isNotBlank() && !isTranslating && sourceLanguage != targetLanguage
        ) {
            if (isTranslating) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp)
            } else {
                Icon(Icons.Outlined.Translate, contentDescription = "Icône de traduction")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Traduire")
            }
        }

        // Affichage de l'erreur de traduction
        if (translationError != null) {
            Text(
                text = translationError!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Zone de texte traduit
        if (translatedText.isNotEmpty() || isTranslating) { // Afficher la carte même pendant la traduction pour la cohérence
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Traduction ($targetLanguage):",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (isTranslating && translatedText.isEmpty()) { // Afficher un placeholder pendant la traduction initiale
                        Text(
                            text = "Traduction en cours...",
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        )
                    } else {
                        Text(
                            text = translatedText,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        )
                    }

                    if (translatedText.isNotBlank() && !isTranslating) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            TextButton(onClick = {
                                clipboardManager.setText(AnnotatedString(translatedText))
                                // Optionnel: Toast.makeText(context, "Texte copié", Toast.LENGTH_SHORT).show()
                            }) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copier", modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Copier")
                            }
                        }
                    }
                }
            }
        }


        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "TrioLex - Traduction pour le tribunal de Bertoua",
            style = MaterialTheme.typography.bodySmall, // Style du thème
            color = MaterialTheme.colorScheme.onSurfaceVariant, // Couleur du thème
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

// Composable réutilisable pour la sélection de langue
@Composable
private fun LanguageSelector(
    label: String,
    expanded: Boolean,
    onExpandToggle: () -> Unit,
    languages: List<String>,
    onLanguageSelected: (String) -> Unit
) {
    Box {
        OutlinedButton(onClick = onExpandToggle) {
            Text(label)
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Sélectionner langue")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = onExpandToggle) {
            languages.forEach { language ->
                DropdownMenuItem(
                    text = { Text(language) },
                    onClick = { onLanguageSelected(language) }
                )
            }
        }
    }
}