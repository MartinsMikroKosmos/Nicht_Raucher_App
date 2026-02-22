# 🧠 PureProgress

Ein moderner Begleiter für jeden Abstinenz-Prozess. Die App hilft Nutzern dabei, jede Art von Sucht zu überwinden – Zigaretten, Alkohol, Cannabis, Glücksspiel und mehr – und macht Fortschritte durch positive Bestärkung sichtbar.

## 🎯 Über das Projekt

Egal ob Rauchen, Trinken oder eine andere Gewohnheit: Der Weg zur Abstinenz ist schwer. Diese App visualisiert, was man bereits erreicht hat – gespartes Geld, vermiedene Einheiten, körperliche Regeneration – und hält die Motivation hoch.

## ✨ Funktionen (Features)

* **Multi-Sucht-Tracking**: Unterstützt beliebige Substanzen und Gewohnheiten – Zigaretten, Selbstgedrehte, Bier, Wein, Schnaps, Joints, Glücksspiel und mehr.
* **Live-Timer**: Präziser Sekundencounter (Tage · Stunden · Minuten · Sekunden) mit animiertem Update.
* **Ersparnis-Ticker**: Zeigt sekundengenau das gesparte Geld auf Basis von Einheiten/Tag × Kosten/Einheit.
* **Einheiten-Statistik**: Wie viele Einheiten wurden seitdem vermieden?
* **Metallic Cards**: Jede Karte hat eine individuell wählbare metallische Farbe (Silber, Gold, Bronze, Kupfer, Rosé, Stahlblau, Lila, Smaragd, Rubin, Türkis).
* **Dashboard-Interaktion**: Karten per Drag & Drop umsortieren, per Links-Wisch löschen.
* **Gamification**: Belohnungssystem mit Meilensteinen (24h, 3 Tage, 1 Woche, 1 Monat, 100 Tage) – geplant.
* **Gesundheits-Daten**: Zeitbasierte Körper-Regenerations-Infos je nach Suchttyp – geplant.

## 🛠 Tech Stack

| Bereich | Technologie |
|---|---|
| Sprache | Kotlin 2.1 |
| UI | Jetpack Compose + Material 3 |
| Architektur | Clean Architecture + MVVM |
| Dependency Injection | Hilt / Dagger |
| Datenbank | Room (SQLite) mit Flow / StateFlow |
| Navigation | Jetpack Navigation Compose |
| Drag & Drop | sh.calvin.reorderable |
| Animation | Compose Animations + Lottie |
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
│   ├── card/
│   │   └── Card.kt
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
├── domain/
│   ├── Habit.kt
│   ├── Repository.kt
│   └── use_case/
│       ├── GetHabitsUseCase.kt
│       ├── AddHabitUseCase.kt
│       ├── DeleteHabitUseCase.kt
│       └── UpdateHabitOrderUseCase.kt
├── data/
│   ├── HabitDao.kt
│   ├── HabitDatabase.kt
│   └── HabitRepositoryImpl.kt
├── di/
│   └── AppModule.kt
└── util/
    ├── TimeUtils.kt
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