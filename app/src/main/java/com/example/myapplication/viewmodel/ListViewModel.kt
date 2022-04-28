package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.HabitList
import com.example.myapplication.data.HabitPriority
import com.example.myapplication.data.HabitRecord
import com.example.myapplication.data.HabitType
import com.example.myapplication.filter.IFilter

class ListViewModel : ViewModel() {

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
}