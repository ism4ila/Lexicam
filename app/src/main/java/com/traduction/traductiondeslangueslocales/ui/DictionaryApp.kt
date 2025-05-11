package com.traduction.traductiondeslangueslocales.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History // Conservé
import androidx.compose.material.icons.filled.Search   // Utilisé pour Dictionnaire
import androidx.compose.material.icons.filled.Translate // Utilisé pour Traduction
// Alternative pour Traduction: import androidx.compose.material.icons.outlined.Translate
// Alternative pour Dictionnaire: import androidx.compose.material.icons.filled.Book ou MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector // Pour type d'icône plus générique
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
// NavType et navArgument ne sont pas utilisés ici, peuvent être enlevés si pas prévus ailleurs.
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.traduction.traductiondeslangueslocales.ui.screens.DictionaryScreen
import com.traduction.traductiondeslangueslocales.ui.screens.HistoryScreen
import com.traduction.traductiondeslangueslocales.ui.screens.TranslationScreen

// Utilisation de ImageVector pour plus de flexibilité si vous utilisez des icônes outlined/filled.
sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Dictionary : BottomNavItem(
        "dictionary",
        Icons.Default.Search, // Suggestion: Search pour Dictionnaire
        "Dictionnaire"
    )

    object Translation : BottomNavItem(
        "translation",
        Icons.Default.Translate, // Suggestion: Translate pour Traduction
        "Traduction"
    )

    object History : BottomNavItem(
        "history",
        Icons.Default.History,
        "Historique"
    )
}

/**
 * Composant principal de l'application TrioLex
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DictionaryApp() {
    val navController = rememberNavController()

    val items = listOf(
        BottomNavItem.Dictionary,
        BottomNavItem.Translation,
        BottomNavItem.History
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) }, // item.label est un bon contentDescription
                        label = { Text(item.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Dictionary.route, // Ou la route que vous préférez comme départ
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Dictionary.route) {
                DictionaryScreen() // Pas besoin de navController si l'écran n'initie pas de navigation
            }
            composable(BottomNavItem.Translation.route) {
                TranslationScreen() // Idem
            }
            composable(BottomNavItem.History.route) {
                HistoryScreen(navController) // navController est bien passé ici pour le bouton retour
            }
        }
    }
}