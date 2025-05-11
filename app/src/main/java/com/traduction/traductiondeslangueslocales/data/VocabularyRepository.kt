package com.traduction.traductiondeslangueslocales.data

/**
 * Entrée de vocabulaire dans le dictionnaire
 */
data class VocabularyEntry(
    val id: String,
    val french: String,
    val gbaya: String,
    val fulfulde: String,
    val category: String
)

/**
 * Repository pour gérer le vocabulaire juridique
 */
object VocabularyRepository {

    // Liste prédéfinie de vocabulaire juridique
    private val vocabularyEntries = listOf(
        VocabularyEntry(
            id = "1",
            french = "Audience",
            gbaya = "kiyo-kiyo",
            fulfulde = "denndangal",
            category = "Termes généraux"
        ),
        VocabularyEntry(
            id = "2",
            french = "Juge",
            gbaya = "ndengue-wala",
            fulfulde = "alkaali",
            category = "Termes généraux"
        ),
        VocabularyEntry(
            id = "3",
            french = "Témoin",
            gbaya = "zo-so-e-di",
            fulfulde = "seedee",
            category = "Procédure pénale"
        ),
        VocabularyEntry(
            id = "4",
            french = "Accusé",
            gbaya = "zo-ti-sioni",
            fulfulde = "tuumaaɗo",
            category = "Procédure pénale"
        ),
        VocabularyEntry(
            id = "5",
            french = "Plaignant",
            gbaya = "zo-kpe-tene",
            fulfulde = "ngadduɗo",
            category = "Procédure pénale"
        ),
        VocabularyEntry(
            id = "6",
            french = "Avocat",
            gbaya = "zo-kpe-tene-ako",
            fulfulde = "paraakleejo",
            category = "Termes généraux"
        ),
        VocabularyEntry(
            id = "7",
            french = "Serment",
            gbaya = "pumburu-tene",
            fulfulde = "hunayeere",
            category = "Termes généraux"
        ),
        VocabularyEntry(
            id = "8",
            french = "Jugement",
            gbaya = "tene-ngbanga",
            fulfulde = "kiite",
            category = "Termes généraux"
        ),
        VocabularyEntry(
            id = "9",
            french = "Procès",
            gbaya = "fango-tene",
            fulfulde = "hiirannde",
            category = "Procédure civile"
        ),
        VocabularyEntry(
            id = "10",
            french = "Tribunal",
            gbaya = "da-ti-tene",
            fulfulde = "suudu kiite",
            category = "Termes généraux"
        ),
        VocabularyEntry(
            id = "11",
            french = "Mise en délibéré",
            gbaya = "lango-ti-gango",
            fulfulde = "wakkati miilo",
            category = "Procédure civile"
        ),
        VocabularyEntry(
            id = "12",
            french = "Plaidoirie",
            gbaya = "tene-ti-wangango",
            fulfulde = "haala feewtuɗo",
            category = "Procédure civile"
        ),
        VocabularyEntry(
            id = "13",
            french = "Appel",
            gbaya = "tene-ti-kiri",
            fulfulde = "ɗaɓɓitirde",
            category = "Procédure civile"
        ),
        VocabularyEntry(
            id = "14",
            french = "Preuve",
            gbaya = "fe-ti-tene",
            fulfulde = "tabbitinoore",
            category = "Procédure pénale"
        ),
        VocabularyEntry(
            id = "15",
            french = "Prison",
            gbaya = "da-ti-honde",
            fulfulde = "kasu",
            category = "Procédure pénale"
        ),
        VocabularyEntry(
            id = "16",
            french = "Amende",
            gbaya = "ngbanga-kwe",
            fulfulde = "toteere",
            category = "Termes généraux"
        ),
        VocabularyEntry(
            id = "17",
            french = "Greffier",
            gbaya = "zo-te-mbeti",
            fulfulde = "binndoowo",
            category = "Termes généraux"
        ),
        VocabularyEntry(
            id = "18",
            french = "Dossier",
            gbaya = "mbeti-ti-tene",
            fulfulde = "defte ɗe",
            category = "Termes généraux"
        ),
        VocabularyEntry(
            id = "19",
            french = "Déclaration",
            gbaya = "tene-ti-zo",
            fulfulde = "filla",
            category = "Procédure pénale"
        ),
        VocabularyEntry(
            id = "20",
            french = "Ajournement",
            gbaya = "pe-ti-lakwe",
            fulfulde = "ɓaayndol",
            category = "Procédure civile"
        ),
        VocabularyEntry(
            id = "21",
            french = "Le témoin est prié de prêter serment",
            gbaya = "zo-so-e-di a lingbi ti sara pumburu-tene",
            fulfulde = "seedee oo nyaagetaama o huna hunayeere",
            category = "Phrases courantes"
        ),
        VocabularyEntry(
            id = "22",
            french = "L'audience est ajournée jusqu'à demain",
            gbaya = "kiyo-kiyo a hunzi fade lakwe",
            fulfulde = "denndangal ngal waɗɗitaama faa janngo",
            category = "Phrases courantes"
        ),
        VocabularyEntry(
            id = "23",
            french = "Veuillez vous lever",
            gbaya = "mo lingbi ti londo",
            fulfulde = "Umee",
            category = "Phrases courantes"
        ),
        VocabularyEntry(
            id = "24",
            french = "La cour vous écoute",
            gbaya = "zo-ti-tene a ye mo",
            fulfulde = "Sariya ndu na hettiito",
            category = "Phrases courantes"
        ),
        VocabularyEntry(
            id = "25",
            french = "Vous avez le droit de garder le silence",
            gbaya = "mo lingbi ti duti ngbiii",
            fulfulde = "A woodi baawɗe deƴƴude",
            category = "Phrases courantes"
        ),
        VocabularyEntry(
            id = "26",
            french = "Procès-verbal",
            gbaya = "mbeti-tene-ti-londo",
            fulfulde = "winndannde ko waɗi",
            category = "Termes généraux"
        ),
        VocabularyEntry(
            id = "27",
            french = "Condamnation",
            gbaya = "kitta-ngbanga",
            fulfulde = "jukkannde",
            category = "Procédure pénale"
        ),
        VocabularyEntry(
            id = "28",
            french = "Délibération",
            gbaya = "gango-ti-tene",
            fulfulde = "miilaare",
            category = "Procédure civile"
        ),
        VocabularyEntry(
            id = "29",
            french = "Peine",
            gbaya = "songo-ti-ngbanga",
            fulfulde = "jukkungo",
            category = "Procédure pénale"
        ),
        VocabularyEntry(
            id = "30",
            french = "Défense",
            gbaya = "kanga-ndo",
            fulfulde = "haɗitaare",
            category = "Termes généraux"
        )
    )

    /**
     * Obtenir la liste des catégories disponibles
     */
    fun getCategories(): List<String> {
        val categories = vocabularyEntries.map { it.category }.distinct().sorted().toMutableList()
        categories.add(0, "Tous")
        return categories
    }

    /**
     * Récupérer tout le vocabulaire ou filtrer par catégorie
     */
    fun getVocabulary(category: String = "Tous"): List<VocabularyEntry> {
        return if (category == "Tous") {
            vocabularyEntries
        } else {
            vocabularyEntries.filter { it.category == category }
        }
    }

    /**
     * Filtrer le vocabulaire par terme de recherche et catégorie
     */
    fun getFilteredVocabulary(searchQuery: String, category: String): List<VocabularyEntry> {
        return if (searchQuery.isEmpty() && category == "Tous") {
            vocabularyEntries
        } else {
            vocabularyEntries.filter { entry ->
                val matchesSearch = searchQuery.isEmpty() ||
                        entry.french.contains(searchQuery, ignoreCase = true) ||
                        entry.gbaya.contains(searchQuery, ignoreCase = true) ||
                        entry.fulfulde.contains(searchQuery, ignoreCase = true)

                val matchesCategory = category == "Tous" || entry.category == category

                matchesSearch && matchesCategory
            }
        }
    }

    /**
     * Rechercher un terme spécifique
     */
    fun searchTerm(term: String): VocabularyEntry? {
        return vocabularyEntries.find {
            it.french.equals(term, ignoreCase = true) ||
                    it.gbaya.equals(term, ignoreCase = true) ||
                    it.fulfulde.equals(term, ignoreCase = true)
        }
    }
}