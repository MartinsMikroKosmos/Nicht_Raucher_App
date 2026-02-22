package com.example.nicht_raucher_app.ui.add_habit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nicht_raucher_app.domain.SubstanceType
import com.example.nicht_raucher_app.domain.use_case.AddHabitUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

// Voreingestellte metallische Farben (ARGB)
val metallicPresets = listOf(
    0xFFC0C0C0.toInt() to "Silber",
    0xFFFFD700.toInt() to "Gold",
    0xFFCD7F32.toInt() to "Bronze",
    0xFFB87333.toInt() to "Kupfer",
    0xFFB76E79.toInt() to "Rosé",
    0xFF4682B4.toInt() to "Stahlblau",
    0xFF7B68EE.toInt() to "Lila",
    0xFF50C878.toInt() to "Smaragd",
    0xFFCC3333.toInt() to "Rubin",
    0xFF20B2AA.toInt() to "Türkis",
)

@HiltViewModel
class AddHabitViewModel @Inject constructor(
    private val addHabitUseCase: AddHabitUseCase
) : ViewModel() {

    var label by mutableStateOf("")
        private set

    var unitName by mutableStateOf("Zigaretten")
        private set

    var unitsPerDay by mutableStateOf("20")
        private set

    var costPerUnit by mutableStateOf("0,35")
        private set

    var cardColor by mutableIntStateOf(metallicPresets.first().first)
        private set

    var startTimeMillis by mutableLongStateOf(System.currentTimeMillis())
        private set

    var substanceType by mutableStateOf(SubstanceType.CUSTOM)
        private set

    fun onLabelChange(value: String) { label = value }
    fun onUnitNameChange(value: String) { unitName = value }
    fun onUnitsPerDayChange(value: String) { unitsPerDay = value }
    fun onCostPerUnitChange(value: String) { costPerUnit = value }
    fun onColorChange(color: Int) { cardColor = color }

    fun onSubstanceTypeChange(type: SubstanceType) {
        substanceType = type
        if (type != SubstanceType.CUSTOM) {
            onUnitNameChange(type.displayName)
        }
    }

    fun onDateChange(dateMillis: Long) {
        val existing = Calendar.getInstance().apply { timeInMillis = startTimeMillis }
        val newCal = Calendar.getInstance().apply { timeInMillis = dateMillis }
        newCal.set(Calendar.HOUR_OF_DAY, existing.get(Calendar.HOUR_OF_DAY))
        newCal.set(Calendar.MINUTE, existing.get(Calendar.MINUTE))
        newCal.set(Calendar.SECOND, 0)
        startTimeMillis = newCal.timeInMillis
    }

    fun onTimeChange(hour: Int, minute: Int) {
        val cal = Calendar.getInstance().apply { timeInMillis = startTimeMillis }
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        cal.set(Calendar.SECOND, 0)
        startTimeMillis = cal.timeInMillis
    }

    fun saveHabit(onSuccess: () -> Unit) {
        val units = unitsPerDay.replace(",", ".").toDoubleOrNull() ?: return
        val cost = costPerUnit.replace(",", ".").toDoubleOrNull() ?: return
        if (label.isBlank() || unitName.isBlank()) return
        if (startTimeMillis > System.currentTimeMillis()) return

        viewModelScope.launch {
            addHabitUseCase(
                label = label.trim(),
                unitsPerDay = units,
                costPerUnit = cost,
                unitName = unitName.trim(),
                cardColor = cardColor,
                startTimeMillis = startTimeMillis,
                substanceType = substanceType.name
            )
            onSuccess()
        }
    }
}