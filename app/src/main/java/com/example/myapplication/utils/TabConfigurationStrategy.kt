package com.example.myapplication.utils

import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TabConfigurationStrategy(val activity: AppCompatActivity): TabLayoutMediator.TabConfigurationStrategy {
    override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
        when (position) {
            0 -> tab.text = activity.getString(R.string.all_positive)
            else -> tab.text = activity.getString(R.string.all_negative)
        }
    }
}