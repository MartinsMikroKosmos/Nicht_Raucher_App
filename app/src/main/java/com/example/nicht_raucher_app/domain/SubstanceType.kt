package com.example.nicht_raucher_app.domain

enum class SubstanceType(val displayName: String, val emoji: String) {
    CIGARETTES("Zigaretten",       "🚬"),
    TOBACCO("Selbstgedrehte",      "🍂"),
    ALCOHOL("Alkohol",             "🍺"),
    CANNABIS("Cannabis",           "🌿"),
    COFFEE("Kaffee",               "☕"),
    SUGAR("Zucker / Süßigkeiten",  "🍬"),
    ENERGY_DRINKS("Energy Drinks", "⚡"),
    GAMBLING("Glücksspiel",        "🎰"),
    SOCIAL_MEDIA("Social Media",   "📱"),
    CUSTOM("Eigene Eingabe",       "✏️")
}