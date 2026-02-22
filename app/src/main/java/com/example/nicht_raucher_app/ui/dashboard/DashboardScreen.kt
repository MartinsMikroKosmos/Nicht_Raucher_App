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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nicht_raucher_app.domain.Habit
import com.example.nicht_raucher_app.util.MilestoneUtils
import com.example.nicht_raucher_app.util.TimeUtils
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
    val luminance = 0.299f * baseColor.red + 0.587f * baseColor.green + 0.114f * baseColor.blue
    return if (luminance > 0.45f) Color(0xFF1A1A1A) else Color.White
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
    val nextMilestone = remember(tickerTime) { MilestoneUtils.getNextMilestone(habit.startTimeMillis) }
    val milestoneProgress = remember(tickerTime) { MilestoneUtils.getProgressToNext(habit.startTimeMillis) }

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

                HorizontalDivider(color = textColor.copy(alpha = 0.3f))

                Spacer(modifier = Modifier.height(12.dp))

                // Stats
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
                            color = textColor.copy(alpha = 0.75f)
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
                            color = textColor.copy(alpha = 0.75f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = textColor.copy(alpha = 0.3f))
                Spacer(modifier = Modifier.height(8.dp))

                if (nextMilestone != null) {
                    Text(
                        text = "Nächster Meilenstein: ${nextMilestone.title}",
                        style = MaterialTheme.typography.labelSmall,
                        color = textColor.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { milestoneProgress },
                        modifier = Modifier.fillMaxWidth(),
                        color = textColor.copy(alpha = 0.9f),
                        trackColor = textColor.copy(alpha = 0.25f)
                    )
                } else {
                    Text(
                        text = "Alle Meilensteine erreicht! 👑",
                        style = MaterialTheme.typography.labelSmall,
                        color = textColor.copy(alpha = 0.8f)
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
                    fontWeight = FontWeight.ExtraBold
                ),
                color = textColor
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = textColor.copy(alpha = 0.75f)
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