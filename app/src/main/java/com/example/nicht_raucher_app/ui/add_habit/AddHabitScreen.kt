package com.example.nicht_raucher_app.ui.add_habit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nicht_raucher_app.domain.SubstanceType
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddHabitViewModel = hiltViewModel()
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = viewModel.startTimeMillis
    )
    val initialCal = remember(viewModel.startTimeMillis) {
        Calendar.getInstance().apply { timeInMillis = viewModel.startTimeMillis }
    }
    val timePickerState = rememberTimePickerState(
        initialHour = initialCal.get(Calendar.HOUR_OF_DAY),
        initialMinute = initialCal.get(Calendar.MINUTE)
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { viewModel.onDateChange(it) }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Abbrechen") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text("Uhrzeit wählen") },
            text = {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                    TimePicker(state = timePickerState)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onTimeChange(timePickerState.hour, timePickerState.minute)
                    showTimePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Abbrechen") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sucht eintragen") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Zurück")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // --- Substanz wählen ---
            Text(
                text = "Was möchtest du stoppen?",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(SubstanceType.entries.toList()) { type ->
                    FilterChip(
                        selected = viewModel.substanceType == type,
                        onClick = { viewModel.onSubstanceTypeChange(type) },
                        label = { Text("${type.emoji} ${type.displayName}") }
                    )
                }
            }

            // --- Bezeichnung ---
            OutlinedTextField(
                value = viewModel.label,
                onValueChange = viewModel::onLabelChange,
                label = { Text("Bezeichnung (z.B. \"Mein Rauchstopp\")") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // --- Was wird aufgehört? ---
            OutlinedTextField(
                value = viewModel.unitName,
                onValueChange = viewModel::onUnitNameChange,
                label = { Text("Einheit (z.B. Zigaretten, Bier, Joints)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Schnell-Vorschläge
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(listOf("Zigaretten", "Selbstgedrehte", "Bier", "Wein", "Schnaps", "Joints")) { suggestion ->
                    SuggestionChip(
                        onClick = { viewModel.onUnitNameChange(suggestion) },
                        label = { Text(suggestion) }
                    )
                }
            }

            // --- Einheiten pro Tag ---
            OutlinedTextField(
                value = viewModel.unitsPerDay,
                onValueChange = viewModel::onUnitsPerDayChange,
                label = { Text("Einheiten pro Tag") },
                supportingText = { Text("Wie viele ${viewModel.unitName.ifBlank { "Einheiten" }} hast du täglich konsumiert?") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // --- Kosten pro Einheit ---
            val costHint = when {
                viewModel.unitName.contains("zigar", ignoreCase = true) ||
                viewModel.unitName.contains("selbst", ignoreCase = true) ->
                    "Tipp: Packungspreis ÷ Stück pro Packung (z.B. 7,00 ÷ 20 = 0,35)"
                viewModel.unitName.contains("bier", ignoreCase = true) ->
                    "Tipp: Preis pro Flasche / Dose (z.B. 0,89 €)"
                viewModel.unitName.contains("wein", ignoreCase = true) ->
                    "Tipp: Flaschenpreis ÷ Gläser (z.B. 8,00 ÷ 6 = 1,33)"
                viewModel.unitName.contains("schnaps", ignoreCase = true) ->
                    "Tipp: Flaschenpreis ÷ Shots (z.B. 15,00 ÷ 20 = 0,75)"
                viewModel.unitName.contains("joint", ignoreCase = true) ->
                    "Tipp: Gramm-Preis × Gramm pro Joint (z.B. 0,8 g × 10 €/g = 8,00)"
                else -> "Tipp: Kosten pro einzelner Einheit in €"
            }
            OutlinedTextField(
                value = viewModel.costPerUnit,
                onValueChange = viewModel::onCostPerUnitChange,
                label = { Text("Kosten pro Einheit (€)") },
                supportingText = { Text(costHint) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // --- Datum & Uhrzeit ---
            Text(
                text = "Zeitpunkt des Stopps",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val dateString = remember(viewModel.startTimeMillis) {
                    SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY).format(Date(viewModel.startTimeMillis))
                }
                val timeString = remember(viewModel.startTimeMillis) {
                    SimpleDateFormat("HH:mm", Locale.GERMANY).format(Date(viewModel.startTimeMillis))
                }
                OutlinedButton(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.DateRange, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(dateString)
                }
                OutlinedButton(
                    onClick = { showTimePicker = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("🕐 $timeString")
                }
            }

            // --- Zukunfts-Warnung ---
            if (viewModel.startTimeMillis > System.currentTimeMillis()) {
                Text(
                    text = "⚠️ Startzeit darf nicht in der Zukunft liegen.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelMedium
                )
            }

            // --- Kartenfarbe ---
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
                                    if (viewModel.cardColor == colorInt)
                                        Modifier.border(3.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                                    else
                                        Modifier.border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                                )
                                .clickable { viewModel.onColorChange(colorInt) }
                        )
                        Text(
                            text = name,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (viewModel.cardColor == colorInt) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- Speichern ---
            Button(
                onClick = { viewModel.saveHabit(onNavigateBack) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                enabled = viewModel.label.isNotBlank() && viewModel.unitName.isNotBlank()
                        && viewModel.startTimeMillis <= System.currentTimeMillis()
            ) {
                Text("Speichern")
            }
        }
    }
}