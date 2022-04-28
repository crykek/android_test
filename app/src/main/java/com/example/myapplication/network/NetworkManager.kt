package com.example.myapplication.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.BuildConfig
import com.example.myapplication.data.*
import com.example.myapplication.utils.HabitDeserializer
import com.example.myapplication.utils.HabitSerializer
import com.example.myapplication.utils.UIDResponse
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkManager {

    private val authToken: String = "be6303c7-456f-4250-87b1-93536f00b14a"

    private lateinit var retrofit: Retrofit
    private lateinit var service: NetworkService

    private val mutableHabitList: MutableLiveData<List<HabitRecord>> = MutableLiveData()
    val habitList: LiveData<List<HabitRecord>> = mutableHabitList

    private val mutableCreatedId: MutableLiveData<String> = MutableLiveData()
    val createdId: LiveData<String> = mutableCreatedId

    fun initialize() {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(logging)
        }
        val okHttpClient = builder.build()

        val gson = GsonBuilder()
            .registerTypeAdapter(HabitRecord::class.java, HabitSerializer())
            .registerTypeAdapter(HabitRecord::class.java, HabitDeserializer())
            .create()

        retrofit = Retrofit.Builder()
            .baseUrl("https://droid-test-server.doubletapp.ru/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
        service = retrofit.create(NetworkService::class.java)
    }

    fun getHabitList(): Unit {
        val call: Call<List<HabitRecord>> = service.listHabits(authToken)
        call.enqueue(object : Callback<List<HabitRecord>> {
            override fun onResponse(call: Call<List<HabitRecord>>, response: Response<List<HabitRecord>>) {
                val responseBody = response.body()!!
                val list: ArrayList<HabitRecord> = ArrayList()

                for (data in responseBody) {
                    data.type = HabitType.POSITIVE
                    data.priority = HabitPriority.LOW;
                    list.add(data)
                }

                mutableHabitList.postValue(list)
            }

            override fun onFailure(call: Call<List<HabitRecord>>, t: Throwable) {
            }
        })
    }

    fun createOrUpdateHabit(habitRecord: HabitRecord) {
        val call: Call<UIDResponse> = service.createOrUpdateHabit(authToken, habitRecord)
        call.enqueue(object : Callback<UIDResponse> {
            override fun onResponse(call: Call<UIDResponse>, response: Response<UIDResponse>) {
                if (habitRecord.uid == "0") {
                    mutableCreatedId.postValue(response.body()?.uid)
                } else {
                    mutableCreatedId.postValue("0")
                }
            }

            override fun onFailure(call: Call<UIDResponse>, t: Throwable) {
            }
        })
    }

    fun deleteHabit(habitRecord: HabitRecord) {
        deleteHabit(habitRecord.uid)
    }

    private fun deleteHabit(uid: String) {
        val call: Call<String> = service.deleteHabit(authToken, uid)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
            }
        })
    }
}