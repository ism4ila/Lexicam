package com.traduction.traductiondeslangueslocales.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Utilitaires pour la traduction des langues locales
 */
object TranslationUtils {

    /**
     * Vibrer pour donner un retour haptique à l'utilisateur
     */
    @RequiresPermission(Manifest.permission.VIBRATE)
    fun vibrate(context: Context, duration: Long = 50) {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(duration)
        }
    }

    /**
     * Afficher un message Toast
     */
    fun showToast(context: Context, message: String, isLong: Boolean = false) {
        Toast.makeText(
            context,
            message,
            if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        ).show()
    }

    /**
     * Partager un texte
     */
    fun shareText(context: Context, text: String, title: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, title)
        context.startActivity(shareIntent)
    }

    /**
     * Exporter l'historique des traductions en fichier texte
     */
    fun exportHistoryToTxt(context: Context, history: List<Triple<String, String, String>>) {
        try {
            val fileName = "traductions_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.txt"
            val file = File(context.getExternalFilesDir(null), fileName)
            FileOutputStream(file).use { fos ->
                fos.write("HISTORIQUE DES TRADUCTIONS\n\n".toByteArray())

                history.forEachIndexed { index, (source, translated, type) ->
                    val entry = """
                        |Traduction #${index + 1} ($type)
                        |Original: $source
                        |Traduction: $translated
                        |
                        |------------------------------
                        |
                    """.trimMargin()

                    fos.write(entry.toByteArray())
                }
            }

            // Partager le fichier
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)
                type = "text/plain"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(Intent.createChooser(shareIntent, "Partager l'historique des traductions"))

        } catch (e: Exception) {
            e.printStackTrace()
            showToast(context, "Erreur lors de l'exportation de l'historique", true)
        }
    }

    /**
     * Obtenir la liste des langues disponibles
     */
    fun getAvailableLanguages(): List<String> {
        return listOf("Français", "Gbaya", "Fulfuldé")
    }

    /**
     * Vérifier si une langue est disponible
     */
    fun isLanguageAvailable(language: String): Boolean {
        return getAvailableLanguages().contains(language)
    }

    /**
     * Obtenir les catégories du dictionnaire juridique
     */
    fun getDictionaryCategories(): List<String> {
        return listOf("Tous", "Procédure pénale", "Procédure civile", "Termes généraux")
    }
}