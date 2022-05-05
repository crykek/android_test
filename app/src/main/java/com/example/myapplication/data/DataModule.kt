package com.example.myapplication.data

import com.example.myapplication.BuildConfig
import com.example.myapplication.data.network.NetworkManager
import com.example.myapplication.data.network.NetworkService
import com.example.myapplication.domain.DoneHabit
import com.example.myapplication.domain.HabitRecord
import com.example.myapplication.domain.HabitRepository
import com.example.myapplication.utils.DoneHabitDeserializer
import com.example.myapplication.utils.DoneHabitSerializer
import com.example.myapplication.utils.HabitDeserializer
import com.example.myapplication.utils.HabitSerializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun provideHabitRepository(): HabitRepository {
        return HabitRepositoryImpl()
    }

    @Singleton
    @Provides
    fun provideNetworkService(retrofit: Retrofit): NetworkService {
        return retrofit.create()
    }

    @Singleton
    @Provides
    fun provideNetworkManager(service: NetworkService): NetworkManager {
        return NetworkManager(service)
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(logging)
        }
        val okHttpClient = builder.build()

        return Retrofit.Builder()
            .baseUrl("https://droid-test-server.doubletapp.ru/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideGson(
        habitSerializer: HabitSerializer,
        habitDeserializer: HabitDeserializer,
        doneHabitSerializer: DoneHabitSerializer,
        doneHabitDeserializer: DoneHabitDeserializer
    ): Gson {
        return GsonBuilder()
            .registerTypeAdapter(HabitRecord::class.java, habitSerializer)
            .registerTypeAdapter(HabitRecord::class.java, habitDeserializer)
            .registerTypeAdapter(DoneHabit::class.java, doneHabitSerializer)
            .registerTypeAdapter(DoneHabit::class.java, doneHabitDeserializer)
            .create()
    }
}