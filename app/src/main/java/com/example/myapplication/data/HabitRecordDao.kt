package com.example.myapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface HabitRecordDao {

    @Query("SELECT * FROM HabitRecord")
    fun getAll(): List<HabitRecord>

    @Query("SELECT * FROM HabitRecord WHERE type = :habitType")
    fun getAllWithType(habitType: HabitType): List<HabitRecord>

    @Insert
    fun insert(habitRecord: HabitRecord)

    @Update
    fun update(habitRecord: HabitRecord)

    @Query("SELECT * FROM HabitRecord WHERE position = :position")
    fun getHabitWithPosition(position: Int): HabitRecord

    @Query("SELECT COUNT(position) FROM HabitRecord")
    fun getHabitCount(): Int
}