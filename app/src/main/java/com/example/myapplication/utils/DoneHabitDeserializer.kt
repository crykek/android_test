package com.example.myapplication.utils

import com.example.myapplication.domain.DoneHabit
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class DoneHabitDeserializer: JsonDeserializer<DoneHabit> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): DoneHabit = DoneHabit(
        json!!.asJsonObject.get("date").asLong,
        json.asJsonObject.get("habit_uid").asString
    )
}