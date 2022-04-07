package com.example.myapplication.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.myapplication.utils.Action

object HabitList {
    private lateinit var db: HabitDataBase

    lateinit var currentHabit: HabitRecord

    private var mutableCurrentHabitRead: MutableLiveData<HabitRecord> = MutableLiveData()
    var currentHabitRead: LiveData<HabitRecord> = mutableCurrentHabitRead

    var currentCount: Int = 0

    fun initDatabase(context: Context) {
        db = Room.databaseBuilder(context, HabitDataBase::class.java, "HabitDB").allowMainThreadQueries().build()
        currentCount = getHabitCount()
    }

    fun getHabitList() = db.habitRecordDao().getAll()

    fun addHabit(habit: HabitRecord) {
        db.habitRecordDao().insert(habit)
        currentCount += 1
    }

    fun updateHabit(habit: HabitRecord) {
        db.habitRecordDao().update(habit)
    }

    fun getHabitWithPosition(position: Int) {
        mutableCurrentHabitRead.value = db.habitRecordDao().getHabitWithPosition(position)
    }

    private fun getHabitCount() = db.habitRecordDao().getHabitCount()
}