package com.traduction.traductiondeslangueslocales.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Clavier pour les caractères spéciaux des langues Gbaya et Fulfuldé
 */
@Composable
fun SpecialCharactersKeyboard(
    onCharacterSelected: (String) -> Unit,
    selectedLanguage: String
) {
    // Définir les caractères spéciaux pour chaque langue
    val specialCharacters = when (selectedLanguage) {
        "Gbaya" -> listOf(
            'ɓ', 'ɗ', 'ɛ', 'ɔ', 'ŋ', 'ɨ',
            'Ɓ', 'Ɗ', 'Ɛ', 'Ɔ', 'Ŋ', 'Ɨ',
            'á', 'à', 'â', 'é', 'è', 'ê',
            'í', 'ì', 'î', 'ó', 'ò', 'ô',
            'ú', 'ù', 'û'
        ).map { it.toString() }  // Convertir tous les Char en String
        "Fulfuldé" -> listOf(
            "ɓ", "ɗ", "ƴ", "ŋ", "ɲ", "ɠ",
            "Ɓ", "Ɗ", "Ƴ", "Ŋ", "Ɲ", "Ɠ",
            "á", "à", "â", "é", "è", "ê",
            "í", "ì", "î", "ó", "ò", "ô",
            "ú", "ù", "û", "ɗ", "ɗo", "ɗa",
            "ɗe", "ɗi", "ɗu"
        )
        else -> emptyList()
    }

    // Ne rien afficher si aucune langue nécessitant des caractères spéciaux n'est sélectionnée
    if (specialCharacters.isEmpty()) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "Caractères spéciaux $selectedLanguage",
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 40.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 180.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(4.dp)
        ) {
            items(specialCharacters) { char ->
                SpecialCharacterKey(
                    char = char.toString(),
                    onClick = { onCharacterSelected(char) }
                )
            }
        }
    }
}

/**
 * Touche individuelle pour le clavier de caractères spéciaux
 */
@Composable
fun SpecialCharacterKey(char: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable(onClick = onClick)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = char,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}