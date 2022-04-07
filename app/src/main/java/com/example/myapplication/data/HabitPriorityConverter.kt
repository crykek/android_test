package com.example.myapplication.data

import androidx.room.TypeConverter

class HabitPriorityConverter {
    @TypeConverter
    fun fromPriority(priority: HabitPriority): String = priority.getCode()

    @TypeConverter
    fun toPriority(priorityString: String): HabitPriority = HabitPriority.getPriorityFromCode(priorityString)
}