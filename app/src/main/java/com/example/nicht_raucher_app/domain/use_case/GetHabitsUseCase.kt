package com.example.nicht_raucher_app.domain.use_case

import com.example.nicht_raucher_app.domain.Habit
import com.example.nicht_raucher_app.domain.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHabitsUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(): Flow<List<Habit>> {
        return repository.getAllHabits()
    }
}