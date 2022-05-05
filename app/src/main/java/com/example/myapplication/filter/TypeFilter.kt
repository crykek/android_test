package com.example.myapplication.filter

import com.example.myapplication.domain.HabitRecord
import com.example.myapplication.data.HabitType

class TypeFilter(private val habitType: HabitType) : IFilter {
    override fun filter(record: HabitRecord): Boolean = record.type == habitType
}