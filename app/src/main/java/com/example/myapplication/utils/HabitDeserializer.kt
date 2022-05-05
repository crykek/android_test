package com.example.myapplication.utils

import com.example.myapplication.data.HabitPriority
import com.example.myapplication.domain.HabitRecord
import com.example.myapplication.data.HabitType
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class HabitDeserializer : JsonDeserializer<HabitRecord> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): HabitRecord = HabitRecord(
        json!!.asJsonObject.get("uid").asString,
        json.asJsonObject.get("title").asString,
        json.asJsonObject.get("description").asString,
        HabitPriority.getPriorityFromIndex(json.asJsonObject.get("priority").asInt),
        HabitType.getHabitFromIndex(json.asJsonObject.get("type").asInt),
        json.asJsonObject.get("frequency").asString,
        json.asJsonObject.get("count").asString,
        json.asJsonObject.get("color").asInt,
    )
}