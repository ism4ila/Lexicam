package com.traduction.traductiondeslangueslocales.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * Composant pour sélectionner une langue parmi les options disponibles
 */
@Composable
fun LanguageSelector(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val languages = listOf("Français", "Gbaya", "Fulfuldé")
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedButton(onClick = { expanded = true }) {
            Text(selectedLanguage)
            Icon(
                Icons.Default.ArrowDropDown,
                contentDescription = "Sélectionner une langue"
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            languages.forEach { language ->
                DropdownMenuItem(
                    text = { Text(language) },
                    onClick = {
                        onLanguageSelected(language)
                        expanded = false
                    }
                )
            }
        }
    }
}

/**
 * Carte contenant les sélecteurs de langues source et cible
 */
@Composable
fun LanguageSelectorCard(
    sourceLanguage: String,
    targetLanguage: String,
    onSourceLanguageChange: (String) -> Unit,
    onTargetLanguageChange: (String) -> Unit,
    onSwapLanguages: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Langue source
                LanguageSelector(
                    selectedLanguage = sourceLanguage,
                    onLanguageSelected = onSourceLanguageChange
                )

                // Bouton d'échange
                FilledIconButton(onClick = onSwapLanguages) {
                    Icon(
                        Icons.Default.SwapHoriz,
                        contentDescription = "Échanger les langues"
                    )
                }

                // Langue cible
                LanguageSelector(
                    selectedLanguage = targetLanguage,
                    onLanguageSelected = onTargetLanguageChange
                )
            }
        }
    }
}

/**
 * Composant pour les entrées du dictionnaire juridique
 */
@Composable
fun DictionaryEntryCard(
    term: String,
    definition: String,
    category: String,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = term,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "Réduire" else "Voir plus"
                    )
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = definition)

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AssistChip(
                        onClick = { },
                        label = { Text(category) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Category,
                                contentDescription = "Catégorie",
                                Modifier.size(18.dp)
                            )
                        }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Traductions
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Traductions:",
                            style = MaterialTheme.typography.bodySmall
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        AssistChip(
                            onClick = { },
                            label = { Text("Gbaya") }
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        AssistChip(
                            onClick = { },
                            label = { Text("Fulfuldé") }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Élément d'entrée d'historique de traduction
 */
@Composable
fun HistoryEntryItem(
    sourceText: String,
    translatedText: String,
    sourceLanguage: String,
    targetLanguage: String,
    date: String,
    type: String,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Type icon
                Icon(
                    when(type) {
                        "Texte" -> Icons.Default.TextFields
                        "Document" -> Icons.Default.Description
                        "Conversation" -> Icons.Default.Forum
                        else -> Icons.Default.TextFields
                    },
                    contentDescription = type,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = sourceText,
                        fontWeight = FontWeight.Medium,
                        maxLines = if (expanded) Int.MAX_VALUE else 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "$sourceLanguage → $targetLanguage",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "Réduire" else "Voir plus"
                    )
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))

                Divider()

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = translatedText)

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = date,
                        style = MaterialTheme.typography.bodySmall
                    )

                    Row {
                        IconButton(
                            onClick = { /* TODO: Réutiliser cette traduction */ },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = "Réutiliser",
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        IconButton(
                            onClick = onDelete,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Supprimer",
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}