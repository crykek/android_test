package com.example.myapplication.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.data.HabitType
import com.example.myapplication.fragment.HabitListFragment
import com.example.myapplication.viewmodel.ListViewModel
import com.example.myapplication.viewmodel.ListViewModelFactory
import kotlin.math.acos

class TypePagerAdapter(private val activity: AppCompatActivity): FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> HabitListFragment(HabitType.POSITIVE)
            else -> HabitListFragment(HabitType.NEGATIVE)
        }
}