package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity
data class HabitRecord(

    @PrimaryKey
    val position: Int,

    var name: String,
    var description: String,

    @TypeConverters(HabitPriorityConverter::class)
    var priority: HabitPriority,

    @TypeConverters(HabitTypeConverter::class)
    var type: HabitType,

    var times: String,
    var period: String,
    var color: Int,
    var colorIndex: Int
)