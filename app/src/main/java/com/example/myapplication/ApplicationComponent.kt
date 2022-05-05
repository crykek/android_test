package com.example.myapplication

import com.example.myapplication.activity.MainActivity
import com.example.myapplication.data.DataModule
import com.example.myapplication.data.network.NetworkService
import com.example.myapplication.domain.DomainModule
import com.example.myapplication.domain.HabitRepository
import com.example.myapplication.fragment.AllListsFragment
import com.example.myapplication.fragment.HabitListFragment
import com.example.myapplication.presentation.viewmodel.*
import com.google.gson.Gson
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class, PresentationModule::class, DomainModule::class])
interface ApplicationComponent {

    fun inject(activity: MainActivity)

    fun inject(fragment: HabitListFragment)

    fun inject(fragment: AllListsFragment)

    fun getHabitRepository(): HabitRepository

    fun getRetrofit(): Retrofit

    fun getGson(): Gson
}