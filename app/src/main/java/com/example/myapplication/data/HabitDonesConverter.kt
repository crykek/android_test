package com.example.myapplication.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class HabitDonesConverter {

    @TypeConverter
    fun fromString(data: String?): ArrayList<Long> {
        if (data == null) {
            return ArrayList()
        }

        val listType: Type = object :
            TypeToken<ArrayList<Long>>() {}.type
        return Gson().fromJson<ArrayList<Long>>(data, listType)
    }

    @TypeConverter
    fun fromArrayList(dones: ArrayList<Long>): String {
        return Gson().toJson(dones)
    }

}