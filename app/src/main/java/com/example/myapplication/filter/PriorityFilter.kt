package com.example.myapplication.filter

import com.example.myapplication.data.HabitPriority
import com.example.myapplication.data.HabitRecord

class PriorityFilter(private val priorities: ArrayList<HabitPriority>) : IFilter {
    override fun filter(record: HabitRecord): Boolean {
        for (priority in priorities) {
            if (record.priority == priority) {
                return true
            }
        }
        return false
    }
}