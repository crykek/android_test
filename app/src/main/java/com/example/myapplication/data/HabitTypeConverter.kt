package com.example.myapplication.data

import androidx.room.TypeConverter

class HabitTypeConverter {
    @TypeConverter
    fun fromType(type: HabitType): String = type.getCode()

    @TypeConverter
    fun toType(typeString: String): HabitType = HabitType.getTypeFromCode(typeString)
}