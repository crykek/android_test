package com.example.myapplication.domain

import com.example.myapplication.utils.DoneHabitDeserializer
import com.example.myapplication.utils.DoneHabitSerializer
import com.example.myapplication.utils.HabitDeserializer
import com.example.myapplication.utils.HabitSerializer
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainModule {

    @Singleton
    @Provides
    fun provideHabitSerializer(): HabitSerializer {
        return HabitSerializer()
    }

    @Singleton
    @Provides
    fun provideHabitDeserializer(): HabitDeserializer {
        return HabitDeserializer()
    }

    @Singleton
    @Provides
    fun provideDoneHabitSerializer(): DoneHabitSerializer {
        return DoneHabitSerializer()
    }

    @Singleton
    @Provides
    fun provideDoneHabitDeserializer(): DoneHabitDeserializer {
        return DoneHabitDeserializer()
    }
}