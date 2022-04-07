package com.example.myapplication.filter

import com.example.myapplication.data.HabitRecord

interface IFilter {
    fun filter(record: HabitRecord) : Boolean
}