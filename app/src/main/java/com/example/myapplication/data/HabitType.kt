package com.example.myapplication.data

import com.example.myapplication.utils.Constants

enum class HabitType {
    POSITIVE {
        override fun getCode(): String = Constants.TYPE_POSITIVE
        override fun getIndex(): Int = 0
    },
    NEGATIVE {
        override fun getCode(): String = Constants.TYPE_NEGATIVE
        override fun getIndex(): Int = 1
    };

    abstract fun getCode(): String
    abstract fun getIndex(): Int

    companion object {
        fun getDefaultValue() = POSITIVE

        fun getTypeFromCode(code: String) =
            when (code) {
                Constants.TYPE_POSITIVE -> POSITIVE
                else -> NEGATIVE
            }

        fun getHabitFromIndex(index: Int) =
            if (index == 0) {
                POSITIVE
            } else {
                NEGATIVE
            }
    }
}