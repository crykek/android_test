package com.example.myapplication

data class HabitRecord(val name: String, val description: String, val priority: HabitPriority,
                       val type: HabitType, val times: String, val period: String, val color: Int, val colorIndex: Int)