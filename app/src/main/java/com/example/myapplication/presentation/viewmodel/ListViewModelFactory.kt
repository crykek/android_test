package com.example.myapplication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.domain.HabitRepository

class ListViewModelFactory(private val repository: HabitRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ListViewModel(repository) as T
    }

}