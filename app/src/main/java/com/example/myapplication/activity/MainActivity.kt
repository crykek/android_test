package com.example.myapplication.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commitNow
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.utils.Constants
import com.example.myapplication.R
import com.example.myapplication.data.HabitList
import com.example.myapplication.fragment.*
import com.example.myapplication.viewmodel.EditViewModel
import com.example.myapplication.viewmodel.EditViewModelFactory
import com.example.myapplication.viewmodel.ListViewModel
import com.example.myapplication.viewmodel.ListViewModelFactory
import com.google.android.material.navigation.NavigationView

class MainActivity : BaseActivity() {

    private lateinit var currentFragment: Fragment
    private var editFragment: EditFragment = EditFragment()
    private var infoFragment: InfoFragment = InfoFragment()
    private var allListsFragment: AllListsFragment = AllListsFragment()

    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var listViewModel: ListViewModel
    private lateinit var editViewModel: EditViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        HabitList.initDatabase(this)

        listViewModel = ViewModelProvider(this, ListViewModelFactory()).get(ListViewModel::class.java)
        editViewModel = ViewModelProvider(this, EditViewModelFactory()).get(EditViewModel::class.java)

        if (savedInstanceState == null) {
            currentFragment = allListsFragment
            replaceFragment(allListsFragment)
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    replaceFragmentWithClass(Constants.FRAGMENT_ALL_LISTS)
                }
                R.id.nav_info -> {
                    replaceFragmentWithClass(Constants.FRAGMENT_INFO)
                }
            }
            true
        }

        setupObservers()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.commitNow {
            replace(R.id.fragment_layout, fragment)
        }
        currentFragment = fragment
    }

    private fun replaceFragmentWithClass(fragmentClass: String) {
        when (fragmentClass) {
            Constants.FRAGMENT_ALL_LISTS -> replaceFragment(allListsFragment)
            Constants.FRAGMENT_EDIT -> replaceFragment(editFragment)
            Constants.FRAGMENT_INFO -> replaceFragment(infoFragment)
            else -> return
        }
    }

    private fun setupObservers() {
        listViewModel.startEditFlag.observe(this) {
            if (it) {
                replaceFragmentWithClass(Constants.FRAGMENT_EDIT)
                editFragment.setValues(HabitList.currentHabit)
            }
        }

        editViewModel.habitChangedFlag.observe(this) {
            //replaceFragmentWithClass(Constants.FRAGMENT_ALL_LISTS)
            allListsFragment.filter()
        }

        editViewModel.habitSubmitted.observe(this) {
            replaceFragmentWithClass(Constants.FRAGMENT_ALL_LISTS)
        }
    }
}