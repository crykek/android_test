package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName

@Entity
data class HabitRecord(

    @PrimaryKey
    var uid: String,

    var name: String,
    var description: String,

    @TypeConverters(HabitPriorityConverter::class)
    var priority: HabitPriority,

    @TypeConverters(HabitTypeConverter::class)
    var type: HabitType,

    var times: String,
    var period: String,
   // var color: Int,
    var colorIndex: Int
)