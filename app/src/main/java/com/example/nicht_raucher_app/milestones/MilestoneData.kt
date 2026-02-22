package com.example.nicht_raucher_app.milestones

import com.example.nicht_raucher_app.domain.SubstanceType

object MilestoneData {

    private val cigaretteMilestones = listOf(
        Milestone(
            id = "20min",
            durationMillis = 20 * 60 * 1000L,
            title = "20 Minuten frei! 🎉",
            medicalBenefit = "Blutdruck und Puls normalisieren sich bereits.",
            motivationQuote = "Jede Minute zählt. Du hast den ersten Schritt gemacht! 💪"
        ),
        Milestone(
            id = "8h",
            durationMillis = 8 * 60 * 60 * 1000L,
            title = "8 Stunden frei! ⭐",
            medicalBenefit = "Kohlenmonoxid-Spiegel halbiert. Mehr Sauerstoff für dein Gehirn.",
            motivationQuote = "Dein Körper arbeitet bereits für dich. Weiter so! 🧠"
        ),
        Milestone(
            id = "24h",
            durationMillis = 24 * 60 * 60 * 1000L,
            title = "1 Tag frei! 🏆",
            medicalBenefit = "Kohlenmonoxid vollständig abgebaut. Herzinfarktrisiko sinkt.",
            motivationQuote = "Ein ganzer Tag – das ist keine Kleinigkeit. Sei stolz! 🎉"
        ),
        Milestone(
            id = "3d",
            durationMillis = 3 * 24 * 60 * 60 * 1000L,
            title = "3 Tage frei! 🌟",
            medicalBenefit = "Nikotin vollständig aus dem Körper. Bronchien entspannen sich.",
            motivationQuote = "Das Schlimmste liegt hinter dir. Dein Körper ist nikotinfrei! 🌬️"
        ),
        Milestone(
            id = "1w",
            durationMillis = 7 * 24 * 60 * 60 * 1000L,
            title = "1 Woche frei! 🎊",
            medicalBenefit = "Geruchs- und Geschmackssinn verbessern sich spürbar.",
            motivationQuote = "Eine Woche Freiheit. Du bist stärker als du dachtest! 🏆"
        ),
        Milestone(
            id = "2w",
            durationMillis = 14 * 24 * 60 * 60 * 1000L,
            title = "2 Wochen frei! 🚀",
            medicalBenefit = "Durchblutung verbessert. Lunge beginnt sich zu reinigen.",
            motivationQuote = "Zwei Wochen – dein Körper dankt es dir jeden Tag mehr! 💚"
        ),
        Milestone(
            id = "1m",
            durationMillis = 30 * 24 * 60 * 60 * 1000L,
            title = "1 Monat frei! 🥇",
            medicalBenefit = "Husten nimmt ab. Flimmerhärchen in der Lunge regenerieren.",
            motivationQuote = "Ein Monat rauchfrei! Das ist ein echter Meilenstein. 🌟"
        ),
        Milestone(
            id = "3m",
            durationMillis = 90 * 24 * 60 * 60 * 1000L,
            title = "3 Monate frei! 🏅",
            medicalBenefit = "Lungenfunktion um bis zu 30% verbessert.",
            motivationQuote = "3 Monate – deine Lunge atmet tiefer als je zuvor! 🫁"
        ),
        Milestone(
            id = "100d",
            durationMillis = 100 * 24 * 60 * 60 * 1000L,
            title = "100 Tage frei! 💎",
            medicalBenefit = "Weniger Atemwegsinfekte. Immunsystem kämpft effektiver.",
            motivationQuote = "100 Tage – du bist unaufhaltbar! 🦾"
        ),
        Milestone(
            id = "1y",
            durationMillis = 365 * 24 * 60 * 60 * 1000L,
            title = "1 Jahr frei! 👑",
            medicalBenefit = "Herzerkrankungsrisiko auf die Hälfte gesunken.",
            motivationQuote = "Ein Jahr! Das ist pure Willenskraft. Unfassbar! 🎊"
        ),
        Milestone(
            id = "5y",
            durationMillis = 5L * 365 * 24 * 60 * 60 * 1000L,
            title = "5 Jahre frei! 🌍",
            medicalBenefit = "Schlaganfallrisiko auf Niveau eines Nichtrauchers gesunken.",
            motivationQuote = "5 Jahre frei. Du hast dein Leben zurückgewonnen! 🏅"
        )
    )

    private val alcoholMilestones = listOf(
        Milestone(
            id = "12h",
            durationMillis = 12 * 60 * 60 * 1000L,
            title = "12 Stunden klar! 💧",
            medicalBenefit = "Blutzucker stabilisiert sich. Zittern und Übelkeit nehmen ab.",
            motivationQuote = "Der schwerste Moment liegt hinter dir. Durchatmen! 💙"
        ),
        Milestone(
            id = "24h",
            durationMillis = 24 * 60 * 60 * 1000L,
            title = "1 Tag klar! 🏆",
            medicalBenefit = "Dehydrierung klingt ab. Herz schlägt ruhiger und gleichmäßiger.",
            motivationQuote = "Ein Tag klar – dein Körper erholt sich schneller als du denkst! 💙"
        ),
        Milestone(
            id = "3d",
            durationMillis = 3 * 24 * 60 * 60 * 1000L,
            title = "3 Tage klar! 🧘",
            medicalBenefit = "Schlaf wird tiefer. Gehirnchemie beginnt sich zu normalisieren.",
            motivationQuote = "3 Tage! Der Kopf wird klarer, die Gedanken ruhiger. 🧘"
        ),
        Milestone(
            id = "1w",
            durationMillis = 7 * 24 * 60 * 60 * 1000L,
            title = "1 Woche klar! 🎊",
            medicalBenefit = "Leber beginnt sich zu erholen. Haut sieht frischer aus.",
            motivationQuote = "Eine Woche nüchtern – dein Spiegelbild verändert sich! 🪞"
        ),
        Milestone(
            id = "2w",
            durationMillis = 14 * 24 * 60 * 60 * 1000L,
            title = "2 Wochen klar! ☀️",
            medicalBenefit = "Blutdruck sinkt spürbar. Stabilere Stimmung, weniger Angstzustände.",
            motivationQuote = "Zwei Wochen! Dein Geist ist freier als er es lange war. ☀️"
        ),
        Milestone(
            id = "1m",
            durationMillis = 30 * 24 * 60 * 60 * 1000L,
            title = "1 Monat klar! 🥇",
            medicalBenefit = "Leber deutlich erholt. Konzentration und Gedächtnis verbessern sich.",
            motivationQuote = "Ein Monat! Du hast bewiesen, dass du es kannst. 🌈"
        ),
        Milestone(
            id = "3m",
            durationMillis = 90 * 24 * 60 * 60 * 1000L,
            title = "3 Monate klar! 📖",
            medicalBenefit = "Risiko für Leberschäden und Herzerkrankungen sinkt messbar.",
            motivationQuote = "3 Monate – ein neues Kapitel in deinem Leben! 📖"
        ),
        Milestone(
            id = "1y",
            durationMillis = 365 * 24 * 60 * 60 * 1000L,
            title = "1 Jahr klar! 👑",
            medicalBenefit = "Leber vollständig regeneriert. Immunsystem deutlich stärker.",
            motivationQuote = "Ein Jahr nüchtern. Das ist Lebenskunst pur! 🏆"
        )
    )

    private val cannabisMilestones = listOf(
        Milestone(
            id = "24h",
            durationMillis = 24 * 60 * 60 * 1000L,
            title = "1 Tag frei! 🌱",
            medicalBenefit = "THC-Spiegel im Blut sinkt. Gedanken werden klarer.",
            motivationQuote = "Der erste Tag ist der mutigste. Gut gemacht! 🌱"
        ),
        Milestone(
            id = "3d",
            durationMillis = 3 * 24 * 60 * 60 * 1000L,
            title = "3 Tage frei! ✨",
            medicalBenefit = "Motivationssystem des Gehirns beginnt sich zu erholen.",
            motivationQuote = "3 Tage! Du spürst, wie du wieder du selbst wirst. ✨"
        ),
        Milestone(
            id = "1w",
            durationMillis = 7 * 24 * 60 * 60 * 1000L,
            title = "1 Woche frei! 🌙",
            medicalBenefit = "Schlafqualität verbessert sich deutlich. Träume kehren zurück.",
            motivationQuote = "Eine Woche! Dein Gehirn träumt wieder. 🌙"
        ),
        Milestone(
            id = "1m",
            durationMillis = 30 * 24 * 60 * 60 * 1000L,
            title = "1 Monat frei! 🧠",
            medicalBenefit = "Kurzzeitgedächtnis und Konzentration deutlich besser.",
            motivationQuote = "Ein Monat klar! Dein Kopf arbeitet wieder auf Hochtouren. 🧠"
        ),
        Milestone(
            id = "3m",
            durationMillis = 90 * 24 * 60 * 60 * 1000L,
            title = "3 Monate frei! 🌈",
            medicalBenefit = "Dopaminsystem normalisiert. Freude an Alltäglichem kehrt zurück.",
            motivationQuote = "3 Monate! Das Leben in Farbe – ohne Hilfsmittel. 🌈"
        ),
        Milestone(
            id = "1y",
            durationMillis = 365 * 24 * 60 * 60 * 1000L,
            title = "1 Jahr frei! 👑",
            medicalBenefit = "Gehirn vollständig erholt. Kognitive Fähigkeiten auf Höchststand.",
            motivationQuote = "Ein Jahr frei! Das größte Geschenk an dich selbst. 🎁"
        )
    )

    private val coffeeMilestones = listOf(
        Milestone(
            id = "12h",
            durationMillis = 12 * 60 * 60 * 1000L,
            title = "12 Stunden koffeinfrei! ☕❌",
            medicalBenefit = "Entzugssymptome beginnen – dein Körper reagiert auf die Veränderung.",
            motivationQuote = "Der Anfang ist der härteste Teil. Bleib stark! 💪"
        ),
        Milestone(
            id = "24h",
            durationMillis = 24 * 60 * 60 * 1000L,
            title = "1 Tag koffeinfrei! ❤️",
            medicalBenefit = "Blutdruck beginnt zu sinken. Herzrhythmus wird ruhiger.",
            motivationQuote = "Ein Tag ohne Koffein – dein Herz dankt es dir! ❤️"
        ),
        Milestone(
            id = "3d",
            durationMillis = 3 * 24 * 60 * 60 * 1000L,
            title = "3 Tage koffeinfrei! 🌅",
            medicalBenefit = "Schlafqualität verbessert sich. Natürliche Energie kehrt zurück.",
            motivationQuote = "3 Tage! Dein natürlicher Rhythmus kommt wieder. 🌅"
        ),
        Milestone(
            id = "1w",
            durationMillis = 7 * 24 * 60 * 60 * 1000L,
            title = "1 Woche koffeinfrei! 🌞",
            medicalBenefit = "Adenosin-Rezeptoren normalisieren sich. Natürliche Wachheit kehrt zurück.",
            motivationQuote = "Eine Woche koffeinfrei! Du wachst jetzt natürlich auf. 🌞"
        ),
        Milestone(
            id = "1m",
            durationMillis = 30 * 24 * 60 * 60 * 1000L,
            title = "1 Monat koffeinfrei! 😁",
            medicalBenefit = "Magensäure normalisiert sich. Zahnverfärbungen gehen zurück.",
            motivationQuote = "Ein Monat! Dein Lächeln strahlt heller. 😁"
        )
    )

    private val sugarMilestones = listOf(
        Milestone(
            id = "24h",
            durationMillis = 24 * 60 * 60 * 1000L,
            title = "1 Tag zuckerfrei! 🍎",
            medicalBenefit = "Insulinspiegel beginnt sich zu stabilisieren.",
            motivationQuote = "Ein Tag ohne Zucker – der erste Schritt zu mehr Energie! 🍎"
        ),
        Milestone(
            id = "3d",
            durationMillis = 3 * 24 * 60 * 60 * 1000L,
            title = "3 Tage zuckerfrei! 💪",
            medicalBenefit = "Heißhungerattacken nehmen ab. Energie wird gleichmäßiger.",
            motivationQuote = "3 Tage! Die Zuckergier wird schwächer. Du bist stärker! 💪"
        ),
        Milestone(
            id = "1w",
            durationMillis = 7 * 24 * 60 * 60 * 1000L,
            title = "1 Woche zuckerfrei! ✨",
            medicalBenefit = "Haut sieht frischer aus. Entzündungswerte im Körper sinken.",
            motivationQuote = "Eine Woche! Dein Körper strahlt von innen. ✨"
        ),
        Milestone(
            id = "1m",
            durationMillis = 30 * 24 * 60 * 60 * 1000L,
            title = "1 Monat zuckerfrei! 🏃",
            medicalBenefit = "Gewicht, Blutdruck und Cholesterin verbessern sich messbar.",
            motivationQuote = "Ein Monat zuckerfrei! Dein Körper ist auf Kurs. 🏃"
        ),
        Milestone(
            id = "3m",
            durationMillis = 90 * 24 * 60 * 60 * 1000L,
            title = "3 Monate zuckerfrei! 🛡️",
            medicalBenefit = "Risiko für Typ-2-Diabetes und Herzerkrankungen deutlich gesunken.",
            motivationQuote = "3 Monate! Du hast deine Gesundheit zurückerobert. 🛡️"
        )
    )

    // Für CUSTOM, GAMBLING, SOCIAL_MEDIA und alles ohne eigene Liste
    private val genericMilestones = listOf(
        Milestone(
            id = "24h",
            durationMillis = 24 * 60 * 60 * 1000L,
            title = "1 Tag frei! 🎉",
            medicalBenefit = "Dein Körper und Geist beginnen sich zu erholen.",
            motivationQuote = "Ein Tag geschafft! Jeder Tag macht dich stärker. 💪"
        ),
        Milestone(
            id = "1w",
            durationMillis = 7 * 24 * 60 * 60 * 1000L,
            title = "1 Woche frei! 🌟",
            medicalBenefit = "Merkliche Verbesserungen in Körper und Geist.",
            motivationQuote = "Eine Woche! Du beweist dir selbst, dass du es kannst. 🌟"
        ),
        Milestone(
            id = "1m",
            durationMillis = 30 * 24 * 60 * 60 * 1000L,
            title = "1 Monat frei! 🏆",
            medicalBenefit = "Ein Monat Veränderung hinterlässt echte Spuren.",
            motivationQuote = "30 Tage! Das ist echter, nachhaltiger Fortschritt. 🏆"
        ),
        Milestone(
            id = "3m",
            durationMillis = 90 * 24 * 60 * 60 * 1000L,
            title = "3 Monate frei! 🌈",
            medicalBenefit = "Deine neue Gewohnheit ist fest im Alltag verankert.",
            motivationQuote = "3 Monate – aus einer Entscheidung wurde eine Lebensweise! 🌈"
        ),
        Milestone(
            id = "1y",
            durationMillis = 365 * 24 * 60 * 60 * 1000L,
            title = "1 Jahr frei! 👑",
            medicalBenefit = "Ein Jahr konsequenter Veränderung – messbare Auswirkungen auf deine Gesundheit.",
            motivationQuote = "Ein Jahr! Du bist nicht mehr dieselbe Person wie vor 365 Tagen. 🎊"
        )
    )

    fun getMilestones(substanceType: SubstanceType): List<Milestone> = when (substanceType) {
        SubstanceType.CIGARETTES,
        SubstanceType.TOBACCO       -> cigaretteMilestones
        SubstanceType.ALCOHOL       -> alcoholMilestones
        SubstanceType.CANNABIS      -> cannabisMilestones
        SubstanceType.COFFEE        -> coffeeMilestones
        SubstanceType.SUGAR,
        SubstanceType.ENERGY_DRINKS -> sugarMilestones
        else                        -> genericMilestones
    }

    // Rückwärtskompatibilität für MilestoneUtils (nutzt noch keine SubstanceType)
    val milestones: List<Milestone> get() = genericMilestones
}