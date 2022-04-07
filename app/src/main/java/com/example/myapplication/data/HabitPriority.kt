package com.example.myapplication.data

import com.example.myapplication.utils.Constants

enum class HabitPriority {
    HIGH {
        override fun getCode(): String = Constants.PRIORITY_HIGH
        override fun getIndex(): Int = 0
    },
    MEDIUM {
        override fun getCode(): String = Constants.PRIORITY_MEDIUM
        override fun getIndex(): Int = 1
    },
    LOW {
        override fun getCode(): String = Constants.PRIORITY_LOW
        override fun getIndex(): Int = 2
    };

    abstract fun getCode(): String
    abstract fun getIndex(): Int

    companion object {
        fun getDefaultValue() = HIGH

        fun getPriorityFromCode(code: String) =
            when (code) {
                Constants.PRIORITY_HIGH -> HIGH
                Constants.PRIORITY_MEDIUM -> MEDIUM
                else -> LOW
            }

        fun getPriorityFromIndex(index: Int) =
            when (index) {
                0 -> HIGH
                1 -> MEDIUM
                else -> LOW
            }
    }
}