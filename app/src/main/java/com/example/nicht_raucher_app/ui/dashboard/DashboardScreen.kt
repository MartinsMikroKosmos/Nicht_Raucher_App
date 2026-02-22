package com.example.nicht_raucher_app.ui.dashboard

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nicht_raucher_app.domain.Habit
import com.example.nicht_raucher_app.util.MilestoneUtils
import com.example.nicht_raucher_app.util.TimeUtils
import kotlin.math.pow
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onAddHabit: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val habits by viewModel.habits.collectAsState()
    val tickerTime by viewModel.ticker.collectAsState()

    // Lokale veränderliche Liste für sofortiges Drag-Feedback
    val localList = remember { mutableStateListOf<Habit>() }

    // Sync DB → lokale Liste (nur wenn sich IDs ändern, nicht bei reinem Reorder)
    LaunchedEffect(habits) {
        val currentIds = localList.map { it.id }.toSet()
        val newIds = habits.map { it.id }.toSet()
        if (currentIds != newIds) {
            localList.clear()
            localList.addAll(habits)
        }
    }

    val lazyListState = rememberLazyListState()
    val reorderableState = rememberReorderableLazyListState(lazyListState) { from, to ->
        localList.add(to.index, localList.removeAt(from.index))
        viewModel.updateOrder(localList.toList())
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddHabit) {
                Icon(Icons.Filled.Add, contentDescription = "Sucht hinzufügen")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Übersicht",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(vertical = 24.dp)
            )

            if (localList.isEmpty()) {
                EmptyDashboardState()
            } else {
                LazyColumn(
                    state = lazyListState,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    items(localList.size, key = { localList[it].id }) { index ->
                        val habit = localList[index]
                        ReorderableItem(reorderableState, key = habit.id) { isDragging ->
                            val dismissState = rememberSwipeToDismissBoxState(
                                confirmValueChange = { value ->
                                    if (value == SwipeToDismissBoxValue.EndToStart) {
                                        viewModel.deleteHabit(habit)
                                        localList.remove(habit)
                                        true
                                    } else false
                                }
                            )

                            SwipeToDismissBox(
                                state = dismissState,
                                enableDismissFromStartToEnd = false,
                                backgroundContent = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(vertical = 2.dp)
                                            .background(
                                                color = MaterialTheme.colorScheme.errorContainer,
                                                shape = MaterialTheme.shapes.extraLarge
                                            ),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(end = 20.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Icon(
                                                Icons.Filled.Delete,
                                                contentDescription = "Löschen",
                                                tint = MaterialTheme.colorScheme.onErrorContainer
                                            )
                                            Text(
                                                "Löschen",
                                                color = MaterialTheme.colorScheme.onErrorContainer,
                                                style = MaterialTheme.typography.labelLarge
                                            )
                                        }
                                    }
                                }
                            ) {
                                HabitCard(
                                    habit = habit,
                                    tickerTime = tickerTime,
                                    isDragging = isDragging,
                                    dragHandle = {
                                        Icon(
                                            imageVector = Icons.Filled.Menu,
                                            contentDescription = "Verschieben",
                                            modifier = Modifier.draggableHandle(),
                                            tint = Color.White.copy(alpha = 0.7f)
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun metallicBrush(baseColor: Color): Brush {
    val highlight = Color(
        red = (baseColor.red * 1.7f).coerceAtMost(1f),
        green = (baseColor.green * 1.7f).coerceAtMost(1f),
        blue = (baseColor.blue * 1.7f).coerceAtMost(1f),
        alpha = 1f
    )
    val midLight = Color(
        red = (baseColor.red * 1.25f).coerceAtMost(1f),
        green = (baseColor.green * 1.25f).coerceAtMost(1f),
        blue = (baseColor.blue * 1.25f).coerceAtMost(1f),
        alpha = 1f
    )
    val shadow = Color(
        red = baseColor.red * 0.45f,
        green = baseColor.green * 0.45f,
        blue = baseColor.blue * 0.45f,
        alpha = 1f
    )
    return Brush.linearGradient(
        colors = listOf(shadow, baseColor, highlight, midLight, baseColor, shadow),
        start = Offset(0f, 0f),
        end = Offset(900f, 600f)
    )
}

private fun contentColorFor(baseColor: Color): Color {
    // WCAG-konformer relativer Luminanzwert (gamma-korrigiert)
    fun linearize(c: Float) = if (c <= 0.03928f) c / 12.92f else ((c + 0.055f) / 1.055f).pow(2.4f)
    val r = linearize(baseColor.red)
    val g = linearize(baseColor.green)
    val b = linearize(baseColor.blue)
    val luminance = 0.2126f * r + 0.7152f * g + 0.0722f * b
    // Schwellwert 0.35 → bei metallischen Farben lieber Weiß wählen
    return if (luminance > 0.35f) Color(0xFF1A1A1A) else Color.White
}

@Composable
fun HabitCard(
    habit: Habit,
    tickerTime: Long,
    isDragging: Boolean = false,
    dragHandle: (@Composable () -> Unit)? = null
) {
    val duration = TimeUtils.getElapsedDuration(habit.startTimeMillis)
    val baseColor = Color(habit.cardColor)
    val brush = metallicBrush(baseColor)
    val textColor = contentColorFor(baseColor)

    val elapsedFractionalDays = (tickerTime - habit.startTimeMillis) / 86_400_000.0
    val unitsAvoided = (habit.unitsPerDay * elapsedFractionalDays).toInt()
    val moneySaved = habit.unitsPerDay * habit.costPerUnit * elapsedFractionalDays
    val mp = remember(tickerTime) { MilestoneUtils.calculateProgress(habit) }
    val savedHours = mp.timeSavedMinutes / 60
    val savedMins  = mp.timeSavedMinutes % 60
    val timeSavedText = if (savedHours > 0) "${savedHours}h ${savedMins}min" else "${savedMins}min"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isDragging) 12.dp else 6.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(brush = brush)
                .padding(20.dp)
        ) {
            Column {
                // Header: Label + Badge + Drag Handle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = habit.label,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        modifier = Modifier.weight(1f)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = Color.Black.copy(alpha = 0.25f)
                        ) {
                            Text(
                                text = "Aktiv",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White
                            )
                        }
                        if (dragHandle != null) dragHandle()
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Zeit-Ticker
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TimeDisplayUnit(duration.days, "Tage", textColor)
                    TimeDisplayUnit(duration.hours, "Std", textColor)
                    TimeDisplayUnit(duration.minutes, "Min", textColor)
                    TimeDisplayUnit(duration.seconds, "Sek", textColor)
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = textColor.copy(alpha = 0.4f))
                Spacer(modifier = Modifier.height(12.dp))

                // Stats – drei Spalten
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "$unitsAvoided",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = textColor
                        )
                        Text(
                            text = "${habit.unitName} vermieden",
                            style = MaterialTheme.typography.labelSmall,
                            color = textColor.copy(alpha = 0.90f)
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = timeSavedText,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = textColor
                        )
                        Text(
                            text = "Zeit gewonnen",
                            style = MaterialTheme.typography.labelSmall,
                            color = textColor.copy(alpha = 0.90f)
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "%.2f €".format(moneySaved),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = textColor
                        )
                        Text(
                            text = "gespart",
                            style = MaterialTheme.typography.labelSmall,
                            color = textColor.copy(alpha = 0.90f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = textColor.copy(alpha = 0.4f))
                Spacer(modifier = Modifier.height(8.dp))

                // Letzter erreichter Milestone → Medical Benefit
                mp.lastReachedMilestone?.let { last ->
                    Text(
                        text = "✅ ${last.title}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = textColor.copy(alpha = 0.9f)
                    )
                    Text(
                        text = last.medicalBenefit,
                        style = MaterialTheme.typography.labelSmall,
                        color = textColor.copy(alpha = 0.90f)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }

                // Nächster Milestone + Fortschrittsbalken + Motivationsspruch
                if (mp.nextMilestone != null) {
                    Text(
                        text = "🎯 Nächstes Ziel: ${mp.nextMilestone.title}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { mp.progress },
                        modifier = Modifier.fillMaxWidth(),
                        color = textColor.copy(alpha = 0.9f),
                        trackColor = textColor.copy(alpha = 0.35f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "💬 ${mp.nextMilestone.motivationQuote}",
                        style = MaterialTheme.typography.labelSmall,
                        color = textColor.copy(alpha = 0.90f)
                    )
                } else {
                    Text(
                        text = "Alle Meilensteine erreicht! 👑",
                        style = MaterialTheme.typography.labelSmall,
                        color = textColor.copy(alpha = 0.90f)
                    )
                }
            }
        }
    }
}

@Composable
fun TimeDisplayUnit(value: Long, label: String, textColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AnimatedContent(
            targetState = value,
            transitionSpec = {
                (slideInVertically { it } + fadeIn()) togetherWith (slideOutVertically { -it } + fadeOut())
            },
            label = "TimeTicker"
        ) { targetValue ->
            Text(
                text = targetValue.toString().padStart(2, '0'),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.3f),
                        offset = Offset(0f, 1f),
                        blurRadius = 4f
                    )
                ),
                color = textColor
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = textColor.copy(alpha = 0.90f)
        )
    }
}

@Composable
fun EmptyDashboardState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "Noch kein Stopp eingetragen.\nTippe auf das + um zu starten!",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.outline
        )
    }
}