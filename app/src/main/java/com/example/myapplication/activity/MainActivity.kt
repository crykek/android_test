package com.example.myapplication.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commitNow
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.myapplication.utils.Constants
import com.example.myapplication.R
import com.example.myapplication.application.MainApp
import com.example.myapplication.data.HabitList
import com.example.myapplication.data.HabitType
import com.example.myapplication.fragment.*
import com.example.myapplication.data.network.NetworkManager
import com.example.myapplication.domain.HabitRecord
import com.example.myapplication.domain.HabitRepository
import com.example.myapplication.presentation.viewmodel.EditViewModel
import com.example.myapplication.presentation.viewmodel.EditViewModelFactory
import com.example.myapplication.presentation.viewmodel.ListViewModel
import com.example.myapplication.presentation.viewmodel.ListViewModelFactory
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

class MainActivity : BaseActivity() {

    private lateinit var currentFragment: Fragment
    private var editFragment: EditFragment = EditFragment()
    private var infoFragment: InfoFragment = InfoFragment()
    private var allListsFragment: AllListsFragment = AllListsFragment()

    private lateinit var toggle: ActionBarDrawerToggle

    @Inject
    lateinit var listViewModelFactory: ListViewModelFactory
    @Inject
    lateinit var editViewModelFactory: EditViewModelFactory

    private lateinit var listViewModel: ListViewModel
    private lateinit var editViewModel: EditViewModel

    @Inject
    lateinit var networkManager: NetworkManager

    @Inject
    lateinit var habitRepository: HabitRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        (application as MainApp).appComponent.inject(this)

        HabitList.initDatabase(this, networkManager)

        listViewModel = ViewModelProvider(this, listViewModelFactory).get(ListViewModel::class.java)
        editViewModel = ViewModelProvider(this, editViewModelFactory).get(EditViewModel::class.java)

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

        setupAvatarImage(navigationView)

        setupObservers()

        listViewModel.startSyncData()
    }

    private fun setupAvatarImage(navigationView: NavigationView) {
        val avatar: ImageView = navigationView.getHeaderView(0).findViewById(R.id.avatar)
        Glide.with(this)
            .load("https://img.icons8.com/color/480/berserk.png")
            .override(80, 80)
            .placeholder(R.drawable.avatar_placeholder)
            .error(R.drawable.avatar_error)
            .centerCrop()
            .into(avatar);
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
            listViewModel.getHabitList(habitRepository)
            //HabitList.getHabitList()
        }

       /* HabitList.currentHabitList.observe(this) {
            allListsFragment.filter()
        }*/

        listViewModel.habitListLoaded.observe(this) {
            allListsFragment.filter()
        }

        editViewModel.habitSubmitted.observe(this) {
            replaceFragmentWithClass(Constants.FRAGMENT_ALL_LISTS)
        }

        listViewModel.syncLocalList.observe(this) {
            listViewModel.syncData(it)
        }

        editViewModel.habitId.observe(this) {
            GlobalScope.launch(Dispatchers.IO) {
                editViewModel.curHabit.uid = it.uid
                habitRepository.addOrUpdateHabit(editViewModel.curHabit)
                listViewModel.getHabitList(habitRepository)
            }
        }
    }

    fun habitDone(habitRecord: HabitRecord) {
        HabitList.doneHabit(habitRecord)

        val period = habitRecord.period.toLong()
        val amount = habitRecord.times.toInt()

        val dones = HabitList.getHabitDoneTimes(habitRecord)
        val fromTime = Calendar.getInstance().timeInMillis - period * 24 * 60 * 60 * 1000

        var executedTimes = 0

        for (done in dones) {
            if (done >= fromTime) {
                executedTimes += 1
            }
        }

        val text: String = when (habitRecord.type) {
            HabitType.POSITIVE -> {
                if (executedTimes >= amount) {
                    getString(R.string.positive_more)
                } else {
                    String.format(getString(R.string.positive_less), amount - executedTimes)
                }
            }
            else -> {
                if (executedTimes >= amount) {
                    getString(R.string.negative_more)
                } else {
                    String.format(getString(R.string.negative_less), amount - executedTimes)
                }
            }
        }
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}