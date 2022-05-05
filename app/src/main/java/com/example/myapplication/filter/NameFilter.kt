package com.example.myapplication.filter

import com.example.myapplication.domain.HabitRecord

class NameFilter(private val name: String) : IFilter {
    override fun filter(record: HabitRecord): Boolean =
        if (name.isEmpty()) {
            true
        } else {
            record.name.lowercase().contains(name.lowercase())
        }
}