package com.traduction.traductiondeslangueslocales.data

import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Entrée d'historique de traduction
 */
data class HistoryEntry(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sourceText: String,
    val translatedText: String,
    val sourceLanguage: String,
    val targetLanguage: String,
    val timestamp: Date = Date(),
    val type: HistoryType = HistoryType.TRANSLATION
)

/**
 * Type d'entrée d'historique
 */
enum class HistoryType {
    TRANSLATION,
    SEARCH
}

/**
 * Gestionnaire d'historique pour l'application
 */
object HistoryManager {
    // Historique des traductions et recherches
    private val _historyEntries = MutableStateFlow<List<HistoryEntry>>(emptyList())
    val historyEntries: StateFlow<List<HistoryEntry>> = _historyEntries.asStateFlow()

    // Format de date pour l'affichage
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    /**
     * Ajouter une nouvelle entrée à l'historique
     */
    fun addEntry(entry: HistoryEntry) {
        // Ajouter l'entrée en tête de liste (la plus récente en premier)
        _historyEntries.value = listOf(entry) + _historyEntries.value.take(49) // Garder seulement les 50 entrées les plus récentes
    }

    /**
     * Ajouter une traduction à l'historique
     */
    fun addTranslation(sourceText: String, translatedText: String, sourceLanguage: String, targetLanguage: String) {
        if (sourceText.isBlank() || translatedText.isBlank()) return

        addEntry(
            HistoryEntry(
                sourceText = sourceText,
                translatedText = translatedText,
                sourceLanguage = sourceLanguage,
                targetLanguage = targetLanguage,
                type = HistoryType.TRANSLATION
            )
        )
    }

    /**
     * Ajouter une recherche à l'historique
     */
    fun addSearch(searchTerm: String) {
        if (searchTerm.isBlank()) return

        addEntry(
            HistoryEntry(
                sourceText = searchTerm,
                translatedText = "", // Pas de traduction pour une recherche
                sourceLanguage = "Recherche",
                targetLanguage = "",
                type = HistoryType.SEARCH
            )
        )
    }

    /**
     * Supprimer une entrée de l'historique
     */
    fun deleteEntry(id: String) {
        _historyEntries.value = _historyEntries.value.filter { it.id != id }
    }

    /**
     * Effacer tout l'historique
     */
    fun clearHistory() {
        _historyEntries.value = emptyList()
    }

    /**
     * Obtenir l'historique filtré par type
     */
    fun getHistoryByType(type: HistoryType? = null): List<HistoryEntry> {
        return if (type == null) {
            _historyEntries.value
        } else {
            _historyEntries.value.filter { it.type == type }
        }
    }

    /**
     * Formater une date pour l'affichage
     */
    fun formatDate(date: Date): String {
        val now = Date()
        val differenceInMillis = now.time - date.time
        val differenceInHours = differenceInMillis / (1000 * 60 * 60)

        return when {
            differenceInHours < 1 -> "Il y a ${differenceInMillis / (1000 * 60)} minutes"
            differenceInHours < 24 -> "Il y a ${differenceInHours} heures"
            differenceInHours < 48 -> "Hier"
            else -> dateFormat.format(date)
        }
    }
}