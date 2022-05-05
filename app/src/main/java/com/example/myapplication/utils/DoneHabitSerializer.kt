package com.example.myapplication.utils

import com.example.myapplication.domain.DoneHabit
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class DoneHabitSerializer: JsonSerializer<DoneHabit> {
    override fun serialize(src: DoneHabit?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement = JsonObject().apply {
        addProperty("date", src?.date)
        addProperty("habit_uid", src?.uid)
    }
}