package com.example.nicht_raucher_app.ui.dashboard

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nicht_raucher_app.domain.Habit
import com.example.nicht_raucher_app.util.TimeUtils

/**
 * Das Dashboard zeigt die Liste deiner aktiven Timer.
 * Es nutzt das ViewModel, um Daten persistent aus Room zu laden.
 */
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val habits by viewModel.habits.collectAsState()
    val tickerTime by viewModel.ticker.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Übersicht",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(vertical = 24.dp)
        )

        if (habits.isEmpty()) {
            EmptyDashboardState()
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 100.dp) // Platz für den FAB
            ) {
                items(habits, key = { it.id }) { habit ->
                    HabitCard(habit = habit, tickerTime = tickerTime)
                }
            }
        }
    }
}

@Composable
fun HabitCard(habit: Habit, tickerTime: Long) {
    // Nutzt jetzt korrekt getElapsedDuration aus deinen TimeUtils
    val duration = TimeUtils.getElapsedDuration(habit.startTimeMillis)

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = habit.label,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                // Status Badge
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "Aktiv",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Der Zeit-Ticker mit den Daten aus TimeDisplay
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TimeDisplayUnit(duration.days, "Tage")
                TimeDisplayUnit(duration.hours, "Std")
                TimeDisplayUnit(duration.minutes, "Min")
                TimeDisplayUnit(duration.seconds, "Sek")
            }
        }
    }
}

@Composable
fun TimeDisplayUnit(value: Long, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AnimatedContent(
            targetState = value,
            transitionSpec = {
                (slideInVertically { it } + fadeIn()) togetherWith (slideOutVertically { -it } + fadeOut())
            }, label = "TimeTicker"
        ) { targetValue ->
            Text(
                text = targetValue.toString().padStart(2, '0'),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
fun EmptyDashboardState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            "Keine Timer vorhanden.\nDrücke auf das Plus!",
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            color = MaterialTheme.colorScheme.outline
        )
    }
}