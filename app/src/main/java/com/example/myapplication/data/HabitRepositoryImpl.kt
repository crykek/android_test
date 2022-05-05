package com.example.myapplication.data

import com.example.myapplication.domain.HabitRecord
import com.example.myapplication.domain.HabitRepository
import kotlinx.coroutines.flow.Flow

class HabitRepositoryImpl : HabitRepository {
    override fun loadHabit(uid: String): Flow<HabitRecord> {
        return HabitList.getHabitWithUid(uid)
    }

    override fun loadHabitList(): Flow<List<HabitRecord>> {
        return HabitList.getHabitList()
    }

    override fun addOrUpdateHabit(habit: HabitRecord) {
        HabitList.updateHabit(habit)
    }
}