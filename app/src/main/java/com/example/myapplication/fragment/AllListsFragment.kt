package com.example.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.*
import com.example.myapplication.application.MainApp
import com.example.myapplication.data.HabitPriority
import com.example.myapplication.utils.TabConfigurationStrategy
import com.example.myapplication.utils.TypePagerAdapter
import com.example.myapplication.presentation.viewmodel.ListViewModel
import com.example.myapplication.presentation.viewmodel.ListViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

class AllListsFragment: Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var typePagerAdapter: TypePagerAdapter

    private lateinit var filterNameField: EditText
    private lateinit var filterHighPriority: CheckBox
    private lateinit var filterMediumPriority: CheckBox
    private lateinit var filterLowPriority: CheckBox

    @Inject
    lateinit var listViewModelFactory: ListViewModelFactory

    private lateinit var listViewModel: ListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.all_lists_fragment, container, false)

        filterNameField = view.findViewById(R.id.filter_name_field)
        tabLayout = view.findViewById(R.id.tab_layout)
        viewPager = view.findViewById(R.id.view_pager)
        filterHighPriority = view.findViewById(R.id.filter_priority_checkbox_high)
        filterMediumPriority = view.findViewById(R.id.filter_priority_checkbox_medium)
        filterLowPriority = view.findViewById(R.id.filter_priority_checkbox_low)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity?.application as MainApp).appComponent.inject(this)

        typePagerAdapter = TypePagerAdapter(activity as AppCompatActivity)
        viewPager.adapter = typePagerAdapter

        TabLayoutMediator(tabLayout, viewPager, TabConfigurationStrategy(activity as AppCompatActivity)).attach()

        listViewModel = ViewModelProvider(activity as AppCompatActivity, listViewModelFactory).get(ListViewModel::class.java)

        filterNameField.addTextChangedListener {
            listViewModel.setNameFilter(it.toString())
        }

        filterLowPriority.setOnCheckedChangeListener { compoundButton, b ->
            listViewModel.setLowPriorityFilter(b)
        }

        filterMediumPriority.setOnCheckedChangeListener { compoundButton, b ->
            listViewModel.setMediumPriorityFilter(b)
        }

        filterHighPriority.setOnCheckedChangeListener { compoundButton, b ->
            listViewModel.setHighPriorityFilter(b)
        }
    }

    fun filter() {
        val fragment: HabitListFragment = activity?.supportFragmentManager?.findFragmentByTag("f" + viewPager.currentItem) as HabitListFragment
        fragment.filterAndUpdateList()
    }
}