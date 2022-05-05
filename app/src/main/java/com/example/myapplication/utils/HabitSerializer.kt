package com.example.myapplication.utils

import com.example.myapplication.domain.HabitRecord
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import org.json.JSONArray
import java.lang.reflect.Type
import java.util.Calendar

class HabitSerializer : JsonSerializer<HabitRecord> {
    override fun serialize(src: HabitRecord?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement = JsonObject().apply {
        if (src?.uid != "0") {
            addProperty("uid", src?.uid)
        }
        addProperty("title", src?.name)
        addProperty("description", src?.description)
        addProperty("priority", src?.priority?.getIndex())
        addProperty("type", src?.type?.getIndex())
        addProperty("frequency", src?.times?.toInt())
        addProperty("count", src?.period?.toInt())
        addProperty("color", src?.colorIndex)
        addProperty("date", Calendar.getInstance().timeInMillis)
    }
}