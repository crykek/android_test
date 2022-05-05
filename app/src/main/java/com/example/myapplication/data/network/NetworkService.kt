package com.example.myapplication.data.network

import com.example.myapplication.domain.DoneHabit
import com.example.myapplication.domain.HabitRecord
import com.example.myapplication.utils.UIDResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface NetworkService {

    @GET("habit")
    suspend fun listHabits(@Header("Authorization") authorization: String): List<HabitRecord>

    @PUT("habit")
    suspend fun createOrUpdateHabit(
        @Header("Authorization") authorization: String,
        @Body habitRecord: HabitRecord
    ): UIDResponse

    @HTTP(method = "DELETE", path = "habit", hasBody = true)
    suspend fun deleteHabit(
        @Header("Authorization") authorization: String,
        @Body uid: String
    ): String

    @POST("habit_done")
    suspend fun doneHabit(@Header("Authorization") authorization: String, @Body doneHabit: DoneHabit): Unit
}