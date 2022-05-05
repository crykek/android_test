package com.example.myapplication.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.myapplication.data.HabitList
import com.example.myapplication.data.HabitPriority
import com.example.myapplication.domain.HabitRecord
import com.example.myapplication.data.HabitType
import com.example.myapplication.domain.HabitRepository
import com.example.myapplication.filter.IFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ListViewModel(private val repository: HabitRepository) : ViewModel() {

    private val mutableStartEditFlag: MutableLiveData<Boolean> = MutableLiveData()
    val startEditFlag: LiveData<Boolean> = mutableStartEditFlag

    private val mutableNameFilter: MutableLiveData<String> = MutableLiveData()
    val nameFilter: LiveData<String> = mutableNameFilter

    private val mutableLowPriorityFilter: MutableLiveData<Boolean> = MutableLiveData(true)
    val lowPriorityFilter: LiveData<Boolean> = mutableLowPriorityFilter

    private val mutableMediumPriorityFilter: MutableLiveData<Boolean> = MutableLiveData(true)
    val mediumPriorityFilter: LiveData<Boolean> = mutableMediumPriorityFilter

    private val mutableHighPriorityFilter: MutableLiveData<Boolean> = MutableLiveData(true)
    val highPriorityFilter: LiveData<Boolean> = mutableHighPriorityFilter

    private val mutableHabitListLoaded: MutableLiveData<List<HabitRecord>> = MutableLiveData()
    var habitListLoaded: LiveData<List<HabitRecord>> = repository.loadHabitList().asLiveData()

    private val mutableSyncLocalList: MutableLiveData<List<HabitRecord>> = MutableLiveData()
    var syncLocalList: LiveData<List<HabitRecord>> = HabitList.getHabitList().asLiveData()

    fun startEditing(habitRecord: HabitRecord) {
        HabitList.currentHabit = habitRecord
        mutableStartEditFlag.value = true
    }

    fun startAdding(defaultName: String, defaultDescription: String, defaultTimes: String, defaultPeriod: String) {
        val newHabit = HabitRecord(
            "0",
            defaultName,
            defaultDescription,
            HabitPriority.getDefaultValue(),
            HabitType.getDefaultValue(),
            defaultTimes,
            defaultPeriod,
            0,
            //0
        )

        startEditing(newHabit)
    }

    fun setNameFilter(text: String) {
        mutableNameFilter.value = text
    }

    fun filterHabits(data: List<HabitRecord>, filters: ArrayList<IFilter>, out: ArrayList<HabitRecord>) {
        out.clear()

        data.forEach { record ->
            var filtered = false
            filters.forEach { filter ->
                if (!filter.filter(record)) {
                    filtered = true
                }
            }

            if (!filtered) {
                out.add(record)
            }
        }
    }

    fun setLowPriorityFilter(v: Boolean) {
        mutableLowPriorityFilter.value = v
    }

    fun setMediumPriorityFilter(v: Boolean) {
        mutableMediumPriorityFilter.value = v
    }

    fun setHighPriorityFilter(v: Boolean) {
        mutableHighPriorityFilter.value = v
    }

    fun getHabitList(repository: HabitRepository) {
        repository.loadHabitList().asLiveData()
        /*GlobalScope.launch(Dispatchers.IO) {
            habitListLoaded = repository.loadHabitList().asLiveData()
            //mutableHabitListLoaded.postValue(repository.loadHabitList())
        }*/
    }

    fun startSyncData() {
        HabitList.getHabitList().asLiveData()
            /*GlobalScope.launch(Dispatchers.IO) {

            //mutableSyncLocalList.postValue(HabitList.getHabitList())
        }*/
    }

    fun syncData(localList: List<HabitRecord>) {
        for (elem in localList) {
            GlobalScope.launch(Dispatchers.IO) {
                HabitList.updateHabitRemote(elem)
            }
        }
    }
}