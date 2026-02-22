package com.example.nicht_raucher_app.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nicht_raucher_app.domain.Habit
import com.example.nicht_raucher_app.domain.use_case.DeleteHabitUseCase
import com.example.nicht_raucher_app.domain.use_case.GetHabitsUseCase
import com.example.nicht_raucher_app.domain.use_case.UpdateHabitOrderUseCase
import com.example.nicht_raucher_app.util.AppConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getHabitsUseCase: GetHabitsUseCase,
    private val deleteHabitUseCase: DeleteHabitUseCase,
    private val updateHabitOrderUseCase: UpdateHabitOrderUseCase
) : ViewModel() {

    val habits: StateFlow<List<Habit>> = getHabitsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _ticker = MutableStateFlow(System.currentTimeMillis())
    val ticker = _ticker.asStateFlow()

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
}