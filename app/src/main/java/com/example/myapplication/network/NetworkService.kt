package com.example.myapplication.network

import com.example.myapplication.data.HabitRecord
import com.example.myapplication.utils.UIDResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.PUT

interface NetworkService {

    @GET("habit")
    fun listHabits(@Header("Authorization") authorization: String): Call<List<HabitRecord>>

    @PUT("habit")
    fun createOrUpdateHabit(
        @Header("Authorization") authorization: String,
        @Body habitRecord: HabitRecord
    ): Call<UIDResponse>

    @HTTP(method = "DELETE", path = "habit", hasBody = true)
    fun deleteHabit(
        @Header("Authorization") authorization: String,
        @Body uid: String
    ): Call<String>
}