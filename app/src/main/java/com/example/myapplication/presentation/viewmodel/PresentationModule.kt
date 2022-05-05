package com.example.myapplication.presentation.viewmodel

import com.example.myapplication.domain.HabitRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PresentationModule {

    @Singleton
    @Provides
    fun provideEditViewModelFactory(): EditViewModelFactory {
        return EditViewModelFactory()
    }

    @Singleton
    @Provides
    fun provideListViewModelFactory(repository: HabitRepository): ListViewModelFactory {
        return ListViewModelFactory(repository)
    }
}