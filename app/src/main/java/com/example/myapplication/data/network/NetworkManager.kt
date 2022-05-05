package com.example.myapplication.data.network

import com.example.myapplication.domain.DoneHabit
import com.example.myapplication.domain.HabitRecord
import com.example.myapplication.utils.UIDResponse

class NetworkManager(private val service: NetworkService) {

    private val authToken: String = "be6303c7-456f-4250-87b1-93536f00b14a"

    suspend fun getHabitList(): List<HabitRecord> {
        return service.listHabits(authToken)
    }

    suspend fun createOrUpdateHabit(habitRecord: HabitRecord): UIDResponse {
        return service.createOrUpdateHabit(authToken, habitRecord)
    }

    suspend fun deleteHabit(habitRecord: HabitRecord): String {
        return deleteHabit(habitRecord.uid)
    }

    suspend fun doneHabit(doneHabit: DoneHabit): Unit {
        service.doneHabit(authToken, doneHabit)
    }

    private suspend fun deleteHabit(uid: String): String {
        return service.deleteHabit(authToken, uid)
    }
}