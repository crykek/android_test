package com.example.myapplication.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.myapplication.data.*
import com.google.gson.annotations.SerializedName

@Entity
data class HabitRecord(

    @PrimaryKey
    var uid: String,

    var name: String,
    var description: String,

    @field:TypeConverters(HabitPriorityConverter::class)
    var priority: HabitPriority,

    @field:TypeConverters(HabitTypeConverter::class)
    var type: HabitType,

    var times: String,
    var period: String,
    var colorIndex: Int,

    @field:TypeConverters(HabitDonesConverter::class)
    var dones: ArrayList<Long> = arrayListOf()
)