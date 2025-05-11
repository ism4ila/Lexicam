package com.traduction.traductiondeslangueslocales

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.traduction.traductiondeslangueslocales.ui.DictionaryApp
import com.traduction.traductiondeslangueslocales.ui.theme.TraductionDesLanguesLocalesTheme

/**
 * Point d'entrée de l'application dictionnaire de vocabulaire
 * Français, Fulfuldé et Gbaya pour le tribunal de Bertoua
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TraductionDesLanguesLocalesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DictionaryApp()
                }
            }
        }
    }
}