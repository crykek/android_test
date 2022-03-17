package com.example.myapplication

enum class RequestCode {
    ADD  {
        override fun getCode() : Int = 1
    },
    EDIT {
        override fun getCode(): Int = 2
    };

    abstract fun getCode(): Int
}