package com.example.myapplication.data

import android.content.Context
import androidx.room.Room
import com.example.myapplication.domain.HabitRecord
import com.example.myapplication.data.network.NetworkManager
import com.example.myapplication.domain.DoneHabit
import com.example.myapplication.utils.UIDResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar

object HabitList {
    private lateinit var db: HabitDataBase
    private lateinit var networkManager: NetworkManager

    lateinit var currentHabit: HabitRecord

    fun initDatabase(context: Context, networkManager: NetworkManager) {
        db = Room.databaseBuilder(context, HabitDataBase::class.java, "HabitDB").fallbackToDestructiveMigration()
            .allowMainThreadQueries().build()

        this.networkManager = networkManager
    }

    suspend fun updateHabitRemote(habit: HabitRecord): UIDResponse {
        return networkManager.createOrUpdateHabit(habit)
    }

    fun getHabitList(): Flow<List<HabitRecord>> {
        return db.habitRecordDao().getAll()
    }

    fun updateHabit(habit: HabitRecord) {
        if (habitWithUidExist(habit.uid)) {
            db.habitRecordDao().update(habit)
        } else {
            db.habitRecordDao().insert(habit)
        }
    }

    fun doneHabit(habit: HabitRecord) {
        val time = Calendar.getInstance().timeInMillis
        habit.dones.add(time)
        GlobalScope.launch(Dispatchers.IO) {
            db.habitRecordDao().update(habit)
            networkManager.doneHabit(DoneHabit(time, habit.uid))
        }
    }

    fun getHabitDoneTimes(habit: HabitRecord): ArrayList<Long> {
        return habit.dones
    }

    fun getHabitWithUid(uid: String): Flow<HabitRecord> {
        return db.habitRecordDao().getHabitWithUid(uid)
    }

    private fun habitWithUidExist(uid: String): Boolean = db.habitRecordDao().exists(uid)
}