package com.traduction.traductiondeslangueslocales.data

/**
 * Service de traduction pour l'application
 */
object TranslationService {

    // Dictionnaires de traduction
    private val frenchToGbaya = mapOf(
        "audience" to "kiyo-kiyo",
        "juge" to "ndengue-wala",
        "témoin" to "zo-so-e-di",
        "accusé" to "zo-ti-sioni",
        "plaignant" to "zo-kpe-tene",
        "avocat" to "zo-kpe-tene-ako",
        "serment" to "pumburu-tene",
        "jugement" to "tene-ngbanga",
        "procès" to "fango-tene",
        "tribunal" to "da-ti-tene",
        "mise en délibéré" to "lango-ti-gango",
        "plaidoirie" to "tene-ti-wangango",
        "appel" to "tene-ti-kiri",
        "preuve" to "fe-ti-tene",
        "prison" to "da-ti-honde",
        "amende" to "ngbanga-kwe",
        "greffier" to "zo-te-mbeti",
        "dossier" to "mbeti-ti-tene",
        "déclaration" to "tene-ti-zo",
        "ajournement" to "pe-ti-lakwe",
        "procès-verbal" to "mbeti-tene-ti-londo",
        "condamnation" to "kitta-ngbanga",
        "délibération" to "gango-ti-tene",
        "peine" to "songo-ti-ngbanga",
        "défense" to "kanga-ndo",
        "le témoin est prié de prêter serment" to "zo-so-e-di a lingbi ti sara pumburu-tene",
        "l'audience est ajournée jusqu'à demain" to "kiyo-kiyo a hunzi fade lakwe",
        "veuillez vous lever" to "mo lingbi ti londo",
        "la cour vous écoute" to "zo-ti-tene a ye mo",
        "vous avez le droit de garder le silence" to "mo lingbi ti duti ngbiii"
    )

    private val frenchToFulfulde = mapOf(
        "audience" to "denndangal",
        "juge" to "alkaali",
        "témoin" to "seedee",
        "accusé" to "tuumaaɗo",
        "plaignant" to "ngadduɗo",
        "avocat" to "paraakleejo",
        "serment" to "hunayeere",
        "jugement" to "kiite",
        "procès" to "hiirannde",
        "tribunal" to "suudu kiite",
        "mise en délibéré" to "wakkati miilo",
        "plaidoirie" to "haala feewtuɗo",
        "appel" to "ɗaɓɓitirde",
        "preuve" to "tabbitinoore",
        "prison" to "kasu",
        "amende" to "toteere",
        "greffier" to "binndoowo",
        "dossier" to "defte ɗe",
        "déclaration" to "filla",
        "ajournement" to "ɓaayndol",
        "procès-verbal" to "winndannde ko waɗi",
        "condamnation" to "jukkannde",
        "délibération" to "miilaare",
        "peine" to "jukkungo",
        "défense" to "haɗitaare",
        "le témoin est prié de prêter serment" to "seedee oo nyaagetaama o huna hunayeere",
        "l'audience est ajournée jusqu'à demain" to "denndangal ngal waɗɗitaama faa janngo",
        "veuillez vous lever" to "Umee",
        "la cour vous écoute" to "Sariya ndu na hettiito",
        "vous avez le droit de garder le silence" to "A woodi baawɗe deƴƴude"
    )

    // Inverse des dictionnaires pour les traductions inversées
    private val gbayaToFrench = frenchToGbaya.entries.associate { (k, v) -> v to k }
    private val fulfuldeToFrench = frenchToFulfulde.entries.associate { (k, v) -> v to k }

    /**
     * Traduire un texte d'une langue à une autre
     */
    fun translate(text: String, sourceLang: String, targetLang: String): String {
        // Essayer de trouver une correspondance directe pour le texte entier
        val directMatches = when {
            sourceLang == "Français" && targetLang == "Gbaya" -> mapOf(text.lowercase() to frenchToGbaya[text.lowercase()])
            sourceLang == "Français" && targetLang == "Fulfuldé" -> mapOf(text.lowercase() to frenchToFulfulde[text.lowercase()])
            sourceLang == "Gbaya" && targetLang == "Français" -> mapOf(text.lowercase() to gbayaToFrench[text.lowercase()])
            sourceLang == "Fulfuldé" && targetLang == "Français" -> mapOf(text.lowercase() to fulfuldeToFrench[text.lowercase()])
            sourceLang == "Gbaya" && targetLang == "Fulfuldé" -> {
                // Traduction via le français
                val toFrench = gbayaToFrench[text.lowercase()]
                mapOf(text.lowercase() to toFrench?.let { frenchToFulfulde[it.lowercase()] })
            }
            sourceLang == "Fulfuldé" && targetLang == "Gbaya" -> {
                // Traduction via le français
                val toFrench = fulfuldeToFrench[text.lowercase()]
                mapOf(text.lowercase() to toFrench?.let { frenchToGbaya[it.lowercase()] })
            }
            else -> emptyMap()
        }

        val directMatch = directMatches[text.lowercase()]
        if (directMatch != null) {
            return directMatch
        }

        // Si pas de correspondance directe, essayer de traduire mot par mot
        val words = text.split(" ", ",", ".", ";", ":", "!", "?")
        val translatedWords = words.map { word ->
            val lowerWord = word.lowercase()
            when {
                sourceLang == "Français" && targetLang == "Gbaya" -> frenchToGbaya[lowerWord] ?: word
                sourceLang == "Français" && targetLang == "Fulfuldé" -> frenchToFulfulde[lowerWord] ?: word
                sourceLang == "Gbaya" && targetLang == "Français" -> gbayaToFrench[lowerWord] ?: word
                sourceLang == "Fulfuldé" && targetLang == "Français" -> fulfuldeToFrench[lowerWord] ?: word
                sourceLang == "Gbaya" && targetLang == "Fulfuldé" -> {
                    val frenchWord = gbayaToFrench[lowerWord]
                    if (frenchWord != null) frenchToFulfulde[frenchWord.lowercase()] ?: word else word
                }
                sourceLang == "Fulfuldé" && targetLang == "Gbaya" -> {
                    val frenchWord = fulfuldeToFrench[lowerWord]
                    if (frenchWord != null) frenchToGbaya[frenchWord.lowercase()] ?: word else word
                }
                else -> word
            }
        }

        // Vérifier si au moins un mot a été traduit
        val hasTranslatedWords = translatedWords.any { it != words[translatedWords.indexOf(it)] }
        if (!hasTranslatedWords) {
            return "Aucune traduction disponible pour ce texte"
        }

        return translatedWords.joinToString(" ")
    }
}