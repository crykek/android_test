package com.example.myapplication.utils

import java.util.Calendar

object IdGenerator {
    fun generateId(): String = Calendar.getInstance().timeInMillis.toString()
}