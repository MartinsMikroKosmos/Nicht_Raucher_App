package com.example.nicht_raucher_app.ui.dashboard

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nicht_raucher_app.domain.Habit
import com.example.nicht_raucher_app.domain.SubstanceType
import com.example.nicht_raucher_app.ui.add_habit.metallicPresets
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
    val backupMessage by viewModel.backupMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.importBackup(it) }
    }

    LaunchedEffect(backupMessage) {
        backupMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearBackupMessage()
        }
    }

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

    var habitToDelete by remember { mutableStateOf<Habit?>(null) }
    var habitToEdit by remember { mutableStateOf<Habit?>(null) }
    var expandedIds by remember { mutableStateOf(setOf<Int>()) }

    // --- Lösch-Bestätigungsdialog ---
    habitToDelete?.let { habit ->
        AlertDialog(
            onDismissRequest = { habitToDelete = null },
            icon = { Text("🗑️", style = MaterialTheme.typography.headlineMedium) },
            title = { Text("Eintrag löschen?") },
            text = {
                Text(
                    "\"${habit.label}\" und alle zugehörigen Daten werden " +
                    "unwiderruflich gelöscht. Fortschritt und Meilensteine gehen verloren."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteHabit(habit)
                        localList.remove(habit)
                        habitToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) { Text("Löschen") }
            },
            dismissButton = {
                OutlinedButton(onClick = { habitToDelete = null }) {
                    Text("Abbrechen")
                }
            }
        )
    }

    // --- Edit-BottomSheet ---
    habitToEdit?.let { habit ->
        var editLabel by remember(habit.id) { mutableStateOf(habit.label) }
        var editColor by remember(habit.id) { mutableIntStateOf(habit.cardColor) }

        ModalBottomSheet(onDismissRequest = { habitToEdit = null }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Eintrag bearbeiten",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = editLabel,
                    onValueChange = { editLabel = it },
                    label = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Kartenfarbe",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(metallicPresets) { (colorInt, name) ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color(colorInt))
                                    .then(
                                        if (editColor == colorInt)
                                            Modifier.border(3.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                                        else
                                            Modifier.border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                                    )
                                    .clickable { editColor = colorInt }
                            )
                            Text(
                                text = name,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = if (editColor == colorInt) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                        if (editLabel.isNotBlank()) {
                            val updated = habit.copy(label = editLabel.trim(), cardColor = editColor)
                            viewModel.updateHabit(updated)
                            val idx = localList.indexOfFirst { it.id == habit.id }
                            if (idx >= 0) localList[idx] = updated
                            habitToEdit = null
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = editLabel.isNotBlank()
                ) { Text("Speichern") }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Übersicht",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                },
                actions = {
                    IconButton(onClick = { viewModel.exportBackup() }) {
                        Icon(
                            imageVector = Icons.Filled.FileDownload,
                            contentDescription = "Backup exportieren"
                        )
                    }
                    IconButton(onClick = { importLauncher.launch("application/json") }) {
                        Icon(
                            imageVector = Icons.Filled.FileUpload,
                            contentDescription = "Backup importieren"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddHabit) {
                Icon(Icons.Filled.Add, contentDescription = "Sucht hinzufügen")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            if (localList.isEmpty()) {
                EmptyDashboardState()
            } else {
                LazyColumn(
                    state = lazyListState,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(top = 8.dp, bottom = 100.dp)
                ) {
                    items(localList.size, key = { localList[it].id }) { index ->
                        val habit = localList[index]
                        ReorderableItem(reorderableState, key = habit.id) { isDragging ->
                            val dismissState = rememberSwipeToDismissBoxState(
                                confirmValueChange = { value ->
                                    if (value == SwipeToDismissBoxValue.EndToStart) {
                                        habitToDelete = habit
                                        false
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
                                    isExpanded = habit.id in expandedIds,
                                    onToggleExpand = {
                                        expandedIds = if (habit.id in expandedIds)
                                            expandedIds - habit.id
                                        else
                                            expandedIds + habit.id
                                    },
                                    isDragging = isDragging,
                                    onEditRequest = { habitToEdit = habit },
                                    dragHandle = {
                                        Icon(
                                            imageVector = Icons.Filled.Menu,
                                            contentDescription = "Verschieben",
                                            modifier = Modifier.draggableHandle(),
                                            tint = contentColorFor(Color(habit.cardColor)).copy(alpha = 0.7f)
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
    // Helligkeit berechnen um Highlight-Intensität anzupassen
    val lum = 0.2126f * baseColor.red + 0.7152f * baseColor.green + 0.0722f * baseColor.blue
    // Helle Farben (Gold, Silber): weniger aggressiver Highlight, mehr Tiefe
    // Dunkle Farben (Nachtblau): stärkerer Highlight für sichtbaren Glanz
    val highlightFactor = if (lum > 0.5f) 1.35f else 1.7f
    val shadowFactor    = if (lum > 0.5f) 0.55f else 0.45f

    val highlight = Color(
        red   = (baseColor.red   * highlightFactor).coerceAtMost(1f),
        green = (baseColor.green * highlightFactor).coerceAtMost(1f),
        blue  = (baseColor.blue  * highlightFactor).coerceAtMost(1f),
        alpha = 1f
    )
    val midLight = Color(
        red   = (baseColor.red   * 1.15f).coerceAtMost(1f),
        green = (baseColor.green * 1.15f).coerceAtMost(1f),
        blue  = (baseColor.blue  * 1.15f).coerceAtMost(1f),
        alpha = 1f
    )
    val shadow = Color(
        red   = baseColor.red   * shadowFactor,
        green = baseColor.green * shadowFactor,
        blue  = baseColor.blue  * shadowFactor,
        alpha = 1f
    )
    return Brush.linearGradient(
        colors = listOf(shadow, baseColor, highlight, midLight, baseColor, shadow),
        start = Offset(0f, 0f),
        end   = Offset(900f, 600f)
    )
}

private fun contentColorFor(baseColor: Color): Color {
    fun linearize(c: Float) = if (c <= 0.03928f) c / 12.92f else ((c + 0.055f) / 1.055f).pow(2.4f)
    val r = linearize(baseColor.red)
    val g = linearize(baseColor.green)
    val b = linearize(baseColor.blue)
    val luminance = 0.2126f * r + 0.7152f * g + 0.0722f * b
    // WCAG: Vergleiche Kontrastverhältnis Weiß vs. Schwarz – nimm den besseren Wert
    val contrastWithWhite = 1.05f / (luminance + 0.05f)
    val contrastWithBlack = (luminance + 0.05f) / 0.05f
    return if (contrastWithWhite >= contrastWithBlack) Color.White else Color(0xFF1A1A1A)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HabitCard(
    habit: Habit,
    tickerTime: Long,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    isDragging: Boolean = false,
    dragHandle: (@Composable () -> Unit)? = null,
    onEditRequest: () -> Unit = {}
) {
    val duration = TimeUtils.getElapsedDuration(habit.startTimeMillis)
    val baseColor = Color(habit.cardColor)
    val brush = metallicBrush(baseColor)
    val textColor = contentColorFor(baseColor)

    val chevronRotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "chevron"
    )

    val substanceEmoji = try {
        SubstanceType.valueOf(habit.substanceType).emoji
    } catch (_: IllegalArgumentException) { "✏️" }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {},
                onLongClick = { onEditRequest() }
            ),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isDragging) 12.dp else 4.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(brush = brush)
                .padding(horizontal = 16.dp, vertical = 18.dp)
        ) {
            Column {

                // ── COLLAPSED HEADER (immer sichtbar) ────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Emoji + Name
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = substanceEmoji,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = habit.label,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = textColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Kompakt-Zeit (nur wenn collapsed)
                    if (!isExpanded) {
                        Text(
                            text = "${duration.days}T " +
                                   "${duration.hours.toString().padStart(2, '0')}h " +
                                   "${duration.minutes.toString().padStart(2, '0')}m",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = textColor.copy(alpha = 0.90f),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }

                    // Chevron + Drag Handle
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        IconButton(
                            onClick = onToggleExpand,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowDown,
                                contentDescription = if (isExpanded) "Einklappen" else "Aufklappen",
                                tint = textColor.copy(alpha = 0.80f),
                                modifier = Modifier.rotate(chevronRotation)
                            )
                        }
                        if (dragHandle != null) dragHandle()
                    }
                }

                // ── EXPANDED INHALT (animiert) ───────────────────────────
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically(animationSpec = tween(300)) + fadeIn(tween(300)),
                    exit  = shrinkVertically(animationSpec = tween(300)) + fadeOut(tween(200))
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = textColor.copy(alpha = 0.3f))
                        Spacer(modifier = Modifier.height(12.dp))

                        // Ticker
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TimeDisplayUnit(duration.days,    "Tage", textColor)
                            TimeDisplayUnit(duration.hours,   "Std",  textColor)
                            TimeDisplayUnit(duration.minutes, "Min",  textColor)
                            TimeDisplayUnit(duration.seconds, "Sek",  textColor)
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = textColor.copy(alpha = 0.3f))
                        Spacer(modifier = Modifier.height(10.dp))

                        // Stats
                        val elapsedDays = (tickerTime - habit.startTimeMillis) / 86_400_000.0
                        val unitsAvoided = (habit.unitsPerDay * elapsedDays).toInt()
                        val moneySaved   = habit.unitsPerDay * habit.costPerUnit * elapsedDays
                        val mp = MilestoneUtils.calculateProgress(habit)
                        val timeSavedText = run {
                            val d = mp.timeSavedMinutes / (60 * 24)
                            val h = (mp.timeSavedMinutes % (60 * 24)) / 60
                            val m = mp.timeSavedMinutes % 60
                            when {
                                d >= 1  -> "${d}T ${h}h"
                                h >= 1  -> "${h}h ${m}min"
                                else    -> "${m}min"
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "$unitsAvoided",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = textColor
                                )
                                Text(
                                    text = "${habit.unitName} vermieden",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = textColor.copy(alpha = 0.90f)
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = timeSavedText,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = textColor
                                )
                                Text(
                                    text = "Zeit gewonnen",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = textColor.copy(alpha = 0.90f)
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "%.2f €".format(moneySaved),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = textColor
                                )
                                Text(
                                    text = "gespart",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = textColor.copy(alpha = 0.90f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = textColor.copy(alpha = 0.3f))
                        Spacer(modifier = Modifier.height(8.dp))

                        // Letzter erreichter Milestone
                        mp.lastReachedMilestone?.let { last ->
                            Text(
                                text = "✅ ${last.title}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = textColor.copy(alpha = 0.9f)
                            )
                            Text(
                                text = last.medicalBenefit,
                                style = MaterialTheme.typography.bodySmall,
                                color = textColor.copy(alpha = 0.85f)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                        }

                        // Nächster Milestone + Fortschrittsbalken + Motivationsspruch
                        mp.nextMilestone?.let { next ->
                            Text(
                                text = "🎯 Nächstes Ziel: ${next.title}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { mp.progress },
                                modifier = Modifier.fillMaxWidth(),
                                color = textColor.copy(alpha = 0.9f),
                                trackColor = textColor.copy(alpha = 0.30f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "💬 ${next.motivationQuote}",
                                style = MaterialTheme.typography.bodySmall,
                                color = textColor.copy(alpha = 0.85f),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        } ?: Text(
                            text = "Alle Meilensteine erreicht! 👑",
                            style = MaterialTheme.typography.bodySmall,
                            color = textColor.copy(alpha = 0.85f)
                        )
                    }
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
                    fontSize = 34.sp,
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
            style = MaterialTheme.typography.labelMedium,
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