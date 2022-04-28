package com.example.myapplication.data

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.myapplication.network.NetworkManager
import com.example.myapplication.utils.Action
import com.example.myapplication.utils.IdGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object HabitList {
    private lateinit var db: HabitDataBase
    private lateinit var networkManager: NetworkManager

    lateinit var currentHabit: HabitRecord

    private var mutableCurrentHabitRead: MutableLiveData<HabitRecord> = MutableLiveData()
    var currentHabitRead: LiveData<HabitRecord> = mutableCurrentHabitRead

    private var mutableHabitList: MutableLiveData<List<HabitRecord>> = MutableLiveData()
    var currentHabitList: LiveData<List<HabitRecord>> = mutableHabitList

    private var readLocal: Boolean = false
    private var readRemote: Boolean = false

    private lateinit var habitWithoutId: HabitRecord

    val colors: ArrayList<Int> = arrayListOf(
        -47104, -24576, -1792, -5243136, -10944768, -16711904, -16711819, -16711732, -16721409,
        -16743681, -16765697, -12779265, -7077633, -1507073, -65363, -65452
    )

    fun initDatabase(context: Context, networkManager: NetworkManager) {
        db = Room.databaseBuilder(context, HabitDataBase::class.java, "HabitDB").fallbackToDestructiveMigration()
            .allowMainThreadQueries().build()

        this.networkManager = networkManager
        this.networkManager.initialize()

        this.networkManager.habitList.observe(context as AppCompatActivity) {
            readRemote = true
            if (readLocal) {
                readLocal = false
                readRemote = false
                syncData()
            }
        }

        this.mutableHabitList.observe(context as AppCompatActivity) {
            readLocal = true
            if (readRemote) {
                readRemote = false
                readLocal = false
                syncData()
            }
        }

        this.networkManager.createdId.observe(context as AppCompatActivity) {
            if (this::habitWithoutId.isInitialized) {
                if (it == "0") {
                    db.habitRecordDao().update(habitWithoutId)
                } else {
                    habitWithoutId.uid = it
                    db.habitRecordDao().insert(habitWithoutId)
                }
                getHabitList()
            }
        }
    }

    private fun syncData() {
        for (elem in networkManager.habitList.value!!) {
            val localElem = db.habitRecordDao().getHabitWithUid(elem.uid)
            networkManager.createOrUpdateHabit(localElem)
        }
    }

    fun getInitialHabitList() {
        GlobalScope.launch(Dispatchers.IO) {
            networkManager.getHabitList()
            mutableHabitList.postValue(db.habitRecordDao().getAll())
        }
    }

    fun getHabitList() {
        mutableHabitList.postValue(db.habitRecordDao().getAll())
    }

    suspend fun addHabit(habit: HabitRecord) {
        habitWithoutId = habit

        networkManager.createOrUpdateHabit(habit)
    }

    suspend fun updateHabit(habit: HabitRecord) {
        //db.habitRecordDao().update(habit)
        habitWithoutId = habit
        networkManager.createOrUpdateHabit(habit)
    }

    fun getHabitWithUid(uid: String) {
        mutableCurrentHabitRead.value = db.habitRecordDao().getHabitWithUid(uid)
    }

    fun habitWithUidExist(uid: String): Boolean = db.habitRecordDao().exists(uid)

    private fun getHabitCount() = db.habitRecordDao().getHabitCount()
}