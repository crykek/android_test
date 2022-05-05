package com.example.myapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.domain.HabitRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitRecordDao {

    @Query("SELECT * FROM HabitRecord")
    fun getAll(): Flow<List<HabitRecord>>

    @Query("SELECT * FROM HabitRecord WHERE type = :habitType")
    fun getAllWithType(habitType: HabitType): Flow<List<HabitRecord>>

    @Insert
    fun insert(habitRecord: HabitRecord)

    @Update
    fun update(habitRecord: HabitRecord)

    @Query("SELECT * FROM HabitRecord WHERE uid = :uid")
    fun getHabitWithUid(uid: String): Flow<HabitRecord>

    @Query("SELECT EXISTS (SELECT 1 FROM HabitRecord WHERE uid = :uid)")
    fun exists(uid: String): Boolean

    @Query("SELECT COUNT(uid) FROM HabitRecord")
    fun getHabitCount(): Flow<Int>
}