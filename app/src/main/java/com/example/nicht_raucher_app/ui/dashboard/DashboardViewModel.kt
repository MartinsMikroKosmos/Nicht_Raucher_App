package com.example.nicht_raucher_app.ui.dashboard

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nicht_raucher_app.domain.Habit
import com.example.nicht_raucher_app.domain.use_case.DeleteHabitUseCase
import com.example.nicht_raucher_app.domain.use_case.GetHabitsUseCase
import com.example.nicht_raucher_app.domain.use_case.ImportHabitsUseCase
import com.example.nicht_raucher_app.domain.use_case.UpdateHabitOrderUseCase
import com.example.nicht_raucher_app.domain.use_case.UpdateHabitUseCase
import com.example.nicht_raucher_app.util.AppConfig
import com.example.nicht_raucher_app.util.BackupManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getHabitsUseCase: GetHabitsUseCase,
    private val deleteHabitUseCase: DeleteHabitUseCase,
    private val updateHabitOrderUseCase: UpdateHabitOrderUseCase,
    private val updateHabitUseCase: UpdateHabitUseCase,
    private val importHabitsUseCase: ImportHabitsUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val habits: StateFlow<List<Habit>> = getHabitsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _ticker = MutableStateFlow(System.currentTimeMillis())
    val ticker = _ticker.asStateFlow()

    private val _backupMessage = MutableStateFlow<String?>(null)
    val backupMessage = _backupMessage.asStateFlow()

    init {
        startTicker()
    }

    private fun startTicker() {
        viewModelScope.launch {
            while (true) {
                _ticker.value = System.currentTimeMillis()
                delay(AppConfig.TIMER_UPDATE_INTERVAL_MS)
            }
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch { deleteHabitUseCase(habit) }
    }

    fun updateOrder(orderedHabits: List<Habit>) {
        viewModelScope.launch { updateHabitOrderUseCase(orderedHabits) }
    }

    fun updateHabit(habit: Habit) {
        viewModelScope.launch { updateHabitUseCase(habit) }
    }

    fun exportBackup() {
        viewModelScope.launch {
            val success = BackupManager.exportToJson(context, habits.value)
            _backupMessage.value = if (success) "Backup in Downloads gespeichert" else "Export fehlgeschlagen"
        }
    }

    fun importBackup(uri: Uri) {
        viewModelScope.launch {
            val imported = BackupManager.importFromJson(context, uri)
            if (imported != null) {
                importHabitsUseCase(imported)
                _backupMessage.value = "${imported.size} Einträge importiert"
            } else {
                _backupMessage.value = "Import fehlgeschlagen"
            }
        }
    }

    fun clearBackupMessage() {
        _backupMessage.value = null
    }
}