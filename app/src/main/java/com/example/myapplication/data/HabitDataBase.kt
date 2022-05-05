package com.example.myapplication.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.domain.HabitRecord

@Database(entities = [HabitRecord::class], version = 7)
abstract class HabitDataBase : RoomDatabase() {
    abstract fun habitRecordDao(): HabitRecordDao
}