package com.traduction.traductiondeslangueslocales.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.traduction.traductiondeslangueslocales.data.VocabularyEntry // Assurez-vous que cette classe a un 'id'
import com.traduction.traductiondeslangueslocales.data.VocabularyRepository
import com.traduction.traductiondeslangueslocales.ui.components.SpecialCharactersKeyboard

/**
 * Écran principal du dictionnaire trilingue avec clavier de caractères spéciaux
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DictionaryScreen() {
    var textFieldValueState by remember { mutableStateOf(TextFieldValue("")) }
    var selectedCategory by remember { mutableStateOf("Tous") }
    var expandedItemId by remember { mutableStateOf<String?>(null) } // Supposant que VocabularyEntry.id est un String

    var showSpecialKeyboard by remember { mutableStateOf(false) }
    var keyboardLanguage by remember { mutableStateOf("Gbaya") } // Langue par défaut pour le clavier

    val searchQuery = textFieldValueState.text

    // Il serait préférable que VocabularyRepository expose des Flows ou StateFlows si les données changent
    // ou si leur chargement est asynchrone. Pour l'instant, on suppose des appels synchrones rapides.
    val categories = remember { VocabularyRepository.getCategories() }
    var categoryMenuExpanded by remember { mutableStateOf(false) }

    // 'remember' avec searchQuery et selectedCategory pour recalculer uniquement si nécessaire
    val filteredVocabulary = remember(searchQuery, selectedCategory) {
        VocabularyRepository.getFilteredVocabulary(searchQuery, selectedCategory)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Dictionnaire Juridique",
            style = MaterialTheme.typography.headlineSmall, // Utiliser les styles du thème
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Français - Gbaya - Fulfuldé",
            style = MaterialTheme.typography.titleMedium, // Utiliser les styles du thème
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = textFieldValueState,
            onValueChange = { textFieldValueState = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Rechercher un terme") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Icône de recherche") },
            singleLine = true,
            trailingIcon = { // Ajout d'un bouton pour effacer la recherche
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { textFieldValueState = TextFieldValue("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Effacer la recherche")
                    }
                }
            }
        )

        // Boutons pour le clavier de caractères spéciaux
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp) // Espacement entre les boutons
        ) {
            // Logique simplifiée pour les boutons du clavier
            SpecialLangButton(
                langName = "Gbaya",
                currentKeyboardLang = keyboardLanguage,
                isKeyboardVisible = showSpecialKeyboard,
                onClick = {
                    if (showSpecialKeyboard && keyboardLanguage == "Gbaya") {
                        showSpecialKeyboard = false
                    } else {
                        keyboardLanguage = "Gbaya"
                        showSpecialKeyboard = true
                    }
                }
            )

            SpecialLangButton(
                langName = "Fulfuldé",
                currentKeyboardLang = keyboardLanguage,
                isKeyboardVisible = showSpecialKeyboard,
                onClick = {
                    if (showSpecialKeyboard && keyboardLanguage == "Fulfuldé") {
                        showSpecialKeyboard = false
                    } else {
                        keyboardLanguage = "Fulfuldé"
                        showSpecialKeyboard = true
                    }
                }
            )
        }

        if (showSpecialKeyboard) {
            SpecialCharactersKeyboard(
                selectedLanguage = keyboardLanguage,
                onCharacterSelected = { char ->
                    val currentText = textFieldValueState.text
                    val selection = textFieldValueState.selection
                    val newText = currentText.replaceRange(selection.start, selection.end, char)
                    val newCursorPosition = selection.start + char.length
                    textFieldValueState = TextFieldValue(
                        text = newText,
                        selection = TextRange(newCursorPosition)
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Catégorie:", modifier = Modifier.padding(end = 8.dp))
            Box {
                OutlinedButton(onClick = { categoryMenuExpanded = true }) {
                    Text(selectedCategory)
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Sélectionner une catégorie")
                }
                DropdownMenu(
                    expanded = categoryMenuExpanded,
                    onDismissRequest = { categoryMenuExpanded = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                selectedCategory = category
                                categoryMenuExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "${filteredVocabulary.size} ${if (filteredVocabulary.size <= 1) "terme trouvé" else "termes trouvés"}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (filteredVocabulary.isEmpty() && searchQuery.isNotEmpty()) {
            Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                Text("Aucun terme ne correspond à votre recherche.", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f) // Important pour que LazyColumn prenne l'espace restant
            ) {
                items(
                    items = filteredVocabulary,
                    key = { it.id } // Assurez-vous que VocabularyEntry a un 'id' unique et stable
                ) { entry ->
                    VocabularyCard(
                        entry = entry, // Passer l'objet entier peut être plus propre
                        isExpanded = expandedItemId == entry.id,
                        onExpandToggle = {
                            expandedItemId = if (expandedItemId == entry.id) null else entry.id
                        }
                    )
                }
            }
        }
    }
}

// Composable de bouton de langue réutilisable
@Composable
private fun SpecialLangButton(
    langName: String,
    currentKeyboardLang: String,
    isKeyboardVisible: Boolean,
    onClick: () -> Unit
) {
    val isSelected = isKeyboardVisible && currentKeyboardLang == langName
    Button( // Utiliser Button pour un meilleur feedback visuel de sélection
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Text("Caractères $langName")
    }
}


/**
 * Carte affichant un terme du vocabulaire avec ses traductions
 */
@Composable
fun VocabularyCard(
    entry: VocabularyEntry, // Attendre l'objet complet
    isExpanded: Boolean,
    onExpandToggle: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onExpandToggle // Rendre toute la carte cliquable pour l'expansion
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = entry.french,
                    style = MaterialTheme.typography.titleMedium, // Styles du thème
                    fontWeight = FontWeight.Bold, // Peut être redondant si déjà dans titleMedium
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f) // Permet au texte de prendre l'espace avant l'icône
                )
                // L'icône est maintenant décorative si la carte entière est cliquable
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (isExpanded) "Réduire" else "Voir plus"
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Category, // Assurez-vous d'avoir cette icône ou choisissez une autre
                    contentDescription = "Catégorie",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = entry.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))

                TranslationDetailItem(label = "Gbaya", text = entry.gbaya, containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer)
                Spacer(modifier = Modifier.height(8.dp))
                TranslationDetailItem(label = "Fulfuldé", text = entry.fulfulde, containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer)
            }
        }
    }
}

// Petit composable pour afficher un détail de traduction
@Composable
private fun TranslationDetailItem(label: String, text: String, containerColor: Color, contentColor: Color) {
    Column {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary // Ou une autre couleur distinctive
        )
        Spacer(modifier = Modifier.height(4.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = containerColor)
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(12.dp), // Un peu plus de padding
                color = contentColor,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}