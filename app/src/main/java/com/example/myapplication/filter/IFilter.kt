package com.example.myapplication.filter

import com.example.myapplication.domain.HabitRecord

interface IFilter {
    fun filter(record: HabitRecord) : Boolean
}