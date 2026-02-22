# 🧠 PureProgress

Ein moderner Begleiter für jeden Abstinenz-Prozess. Die App hilft Nutzern dabei, jede Art von Sucht zu überwinden – Zigaretten, Alkohol, Cannabis, Glücksspiel und mehr – und macht Fortschritte durch positive Bestärkung sichtbar.

## 🎯 Über das Projekt

Egal ob Rauchen, Trinken oder eine andere Gewohnheit: Der Weg zur Abstinenz ist schwer. Diese App visualisiert, was man bereits erreicht hat – gespartes Geld, vermiedene Einheiten, gewonnene Lebenszeit, körperliche Regeneration – und hält die Motivation hoch.

## ✨ Funktionen (Features)

* **SubstanceType-System**: 10 vordefinierte Suchttypen (Zigaretten, Selbstgedrehte, Alkohol, Cannabis, Kaffee, Zucker, Energy Drinks, Glücksspiel, Social Media, Eigene Eingabe) mit jeweils substanzspezifischen Meilensteinen.
* **Live-Timer**: Präziser Sekundencounter (Tage · Stunden · Minuten · Sekunden) mit animiertem Update und Textschatten für optimale Lesbarkeit.
* **3-Spalten-Statistik**: Vermiedene Einheiten · Gewonnene Lebenszeit · Gespartes Geld – sekundengenau berechnet.
* **Substanzspezifische Meilensteine**: Medizinische Benefits und Motivationssprüche je nach Suchttyp (z. B. andere Milestones für Zigaretten vs. Alkohol vs. Cannabis).
* **Milestone-Fortschrittsbalken**: Letzter erreichter Meilenstein mit medizinischem Benefit + Fortschrittsbalken zum nächsten Ziel + Motivationsspruch direkt auf der Card.
* **Push-Notifications**: WorkManager löst automatisch Benachrichtigungen bei jedem Meilenstein aus.
* **Metallic Cards**: Jede Karte hat eine individuell wählbare metallische Farbe (Silber, Gold, Bronze, Kupfer, Rosé, Stahlblau, Lila, Smaragd, Rubin, Türkis) mit WCAG-konformem Kontrast.
* **Dashboard-Interaktion**: Karten per Drag & Drop umsortieren, per Links-Wisch löschen.
* **Datumvalidierung**: Startzeit in der Zukunft wird mit Warnung blockiert.
* **Dynamischer Kostentipp**: Hint im Formular passt sich je nach gewählter Einheit an.

## 🛠 Tech Stack

| Bereich | Technologie |
| --- | --- |
| Sprache | Kotlin 2.1 |
| UI | Jetpack Compose + Material 3 |
| Architektur | Clean Architecture + MVVM |
| Dependency Injection | Hilt / Dagger |
| Datenbank | Room (SQLite) v4 mit Flow / StateFlow + Migration |
| Navigation | Jetpack Navigation Compose |
| Drag & Drop | sh.calvin.reorderable 2.4.0 |
| Animation | Compose Animations |
| Notifications | WorkManager + Hilt Worker |
| Min SDK | 26 (Android 8.0) |
| Target SDK | 35 (Android 15) |

## 🏗 Projektstruktur

```
app/src/main/java/com/example/nicht_raucher_app/
├── ui/
│   ├── MainActivity.kt
│   ├── navigation/
│   │   └── AppNavigation.kt
│   ├── dashboard/
│   │   ├── DashboardScreen.kt
│   │   └── DashboardViewModel.kt
│   ├── add_habit/
│   │   ├── AddHabitScreen.kt
│   │   └── AddHabitViewModel.kt
│   └── theme/
│       ├── Color.kt          (Teal/Indigo-Schema)
│       ├── Theme.kt
│       └── Type.kt
├── domain/
│   ├── Habit.kt              (inkl. substanceType)
│   ├── SubstanceType.kt      (Enum: 10 Suchttypen)
│   ├── Repository.kt
│   └── use_case/
│       ├── GetHabitsUseCase.kt
│       ├── AddHabitUseCase.kt
│       ├── DeleteHabitUseCase.kt
│       └── UpdateHabitOrderUseCase.kt
├── data/
│   ├── HabitDao.kt
│   ├── HabitDatabase.kt      (v4 + MIGRATION_3_4)
│   └── HabitRepositoryImpl.kt
├── milestones/
│   ├── Milestone.kt          (medicalBenefit + motivationQuote)
│   ├── MilestoneData.kt      (substanzspezifische Listen)
│   └── MilestoneScheduler.kt
├── worker/
│   └── MilestoneWorker.kt
├── di/
│   └── AppModule.kt
└── util/
    ├── TimeUtils.kt
    ├── MilestoneUtils.kt     (calculateProgress + MilestoneProgress)
    └── AppConfig.kt
```

## 🚀 Installation & Nutzung

1. Repository klonen:
   ```bash
   git clone https://github.com/MartinsMikroKosmos/Nicht_Raucher_App.git
   ```
2. Projekt in **Android Studio** (Hedgehog oder neuer) öffnen.
3. Gradle sync abwarten.
4. App auf Emulator oder Gerät (Android 8.0+) starten:
   ```bash
   ./gradlew installDebug
   ```

## 🤝 Mitwirken

Beiträge sind willkommen! Wenn du Ideen für neue Features hast oder einen Bug findest:
1. Forke das Repository.
2. Erstelle einen neuen Branch (`git checkout -b feature/NeuesFeature`).
3. Committe deine Änderungen (`git commit -m 'Feature hinzugefügt'`).
4. Pushe den Branch (`git push origin feature/NeuesFeature`).
5. Erstelle einen Pull Request.

## 📄 Lizenz

Dieses Projekt ist unter der [MIT License](LICENSE) lizenziert.