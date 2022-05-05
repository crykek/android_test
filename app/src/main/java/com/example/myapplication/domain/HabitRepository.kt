package com.example.myapplication.domain

import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    fun loadHabit(uid: String): Flow<HabitRecord>

    fun loadHabitList(): Flow<List<HabitRecord>>

    fun addOrUpdateHabit(habit: HabitRecord): Unit
}