package com.example.myapplication.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HabitRecord::class], version = 1)
abstract class HabitDataBase : RoomDatabase() {
    abstract fun habitRecordDao(): HabitRecordDao
}