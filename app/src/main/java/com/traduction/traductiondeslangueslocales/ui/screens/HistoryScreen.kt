package com.traduction.traductiondeslangueslocales.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.SentimentDissatisfied // Nouvelle importation pour une icône d'état vide
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.traduction.traductiondeslangueslocales.data.HistoryEntry
import com.traduction.traductiondeslangueslocales.data.HistoryManager
import com.traduction.traductiondeslangueslocales.data.HistoryType

/**
 * Écran d'historique pour afficher les traductions et recherches précédentes
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = listOf("Tout", "Traductions", "Recherches")

    // Ajout d'un déclencheur pour forcer la recomposition de la liste d'historique
    var historyUpdateTrigger by remember { mutableStateOf(0) }

    // Récupérer l'historique selon l'onglet sélectionné et le déclencheur
    // 'remember' avec des clés (selectedTab, historyUpdateTrigger)
    // garantit que la liste est rechargée si l'onglet change OU si l'historique est modifié.
    val historyEntries by remember(selectedTab, historyUpdateTrigger) {
        derivedStateOf { // Utiliser derivedStateOf pour optimiser les recompositions
            when (selectedTab) {
                0 -> HistoryManager.getHistoryByType()
                1 -> HistoryManager.getHistoryByType(HistoryType.TRANSLATION)
                2 -> HistoryManager.getHistoryByType(HistoryType.SEARCH)
                else -> emptyList()
            }
        }
    }

    var showClearDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historique") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                },
                actions = {
                    // Bouton pour effacer l'historique
                    IconButton(onClick = { showClearDialog = true }) {
                        Icon(Icons.Default.DeleteSweep, contentDescription = "Effacer l'historique")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Onglets pour filtrer l'historique
            TabRow(selectedTabIndex = selectedTab) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) },
                        icon = {
                            when (index) {
                                0 -> Icon(Icons.Default.History, contentDescription = null)
                                1 -> Icon(Icons.Default.Translate, contentDescription = null)
                                2 -> Icon(Icons.Default.Search, contentDescription = null)
                            }
                        }
                    )
                }
            }

            if (historyEntries.isEmpty()) {
                // Message si l'historique est vide
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            // Remplacement de Icons.Default.NoHistory
                            imageVector = Icons.Outlined.SentimentDissatisfied,
                            contentDescription = "Aucun historique",
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Aucun historique disponible",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            } else {
                // Liste des entrées d'historique
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(
                        items = historyEntries,
                        key = { it.id } // Clé unique pour chaque élément, important pour les performances
                    ) { entry ->
                        HistoryEntryItem(
                            entry = entry,
                            onDelete = {
                                HistoryManager.deleteEntry(entry.id)
                                // Incrémenter le déclencheur pour actualiser la liste
                                historyUpdateTrigger++
                            }
                        )
                    }
                }
            }
        }

        // Dialogue de confirmation pour effacer l'historique
        if (showClearDialog) {
            AlertDialog(
                onDismissRequest = { showClearDialog = false },
                title = { Text("Effacer l'historique") },
                text = { Text("Êtes-vous sûr de vouloir effacer tout l'historique ?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            HistoryManager.clearHistory()
                            // Incrémenter le déclencheur pour actualiser la liste
                            historyUpdateTrigger++
                            showClearDialog = false
                        }
                    ) {
                        Text("Effacer")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showClearDialog = false }) {
                        Text("Annuler")
                    }
                }
            )
        }
    }
}

/**
 * Composant pour afficher une entrée d'historique
 */
@Composable
fun HistoryEntryItem(entry: HistoryEntry, onDelete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth()
        // onClick peut être ajouté ici si vous voulez que toute la carte soit cliquable pour étendre
        // onClick = { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Type d'entrée (Traduction ou Recherche) et Date
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    // Rendre cette partie cliquable pour étendre/réduire peut être une alternative
                    // modifier = Modifier.weight(1f).clickable { expanded = !expanded }
                ) {
                    Icon(
                        imageVector = if (entry.type == HistoryType.TRANSLATION)
                            Icons.Default.Translate
                        else
                            Icons.Default.Search,
                        contentDescription = entry.type.name, // Bon usage du nom de l'enum pour la description
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Date formatée
                    Text(
                        text = HistoryManager.formatDate(entry.timestamp), // Assurez-vous que cette fonction est robuste
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Bouton de suppression
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(28.dp) // La taille du IconButton peut être ajustée
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Supprimer",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(18.dp) // La taille de l'icône elle-même
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Texte source
            Text(
                text = entry.sourceText,
                fontWeight = FontWeight.Medium,
                maxLines = if (expanded) Int.MAX_VALUE else 1, // Logique d'expansion correcte
                overflow = TextOverflow.Ellipsis
                // modifier = Modifier.clickable { expanded = !expanded } // Peut aussi être cliquable
            )

            // Informations supplémentaires pour les traductions
            if (entry.type == HistoryType.TRANSLATION && entry.translatedText.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${entry.sourceLanguage} → ${entry.targetLanguage}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (expanded) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider() // Bon usage du Divider pour séparer visuellement
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = entry.translatedText,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Bouton pour afficher plus/moins de détails
            // Ce bouton est bien placé, mais considérez si cliquer sur le texte lui-même serait plus intuitif
            TextButton(
                onClick = { expanded = !expanded },
                modifier = Modifier.align(Alignment.End) // Aligne bien le bouton à droite
            ) {
                Text(
                    text = if (expanded) "Voir moins" else "Voir plus"
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Voir moins" else "Voir plus",
                    modifier = Modifier.size(18.dp) // Ajuster la taille si besoin
                )
            }
        }
    }
}