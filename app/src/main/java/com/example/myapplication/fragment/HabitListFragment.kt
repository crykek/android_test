package com.example.myapplication.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.*
import com.example.myapplication.activity.MainActivity
import com.example.myapplication.application.MainApp
import com.example.myapplication.data.HabitList
import com.example.myapplication.data.HabitPriority
import com.example.myapplication.domain.HabitRecord
import com.example.myapplication.data.HabitType
import com.example.myapplication.domain.HabitRepository
import com.example.myapplication.filter.NameFilter
import com.example.myapplication.filter.PriorityFilter
import com.example.myapplication.filter.TypeFilter
import com.example.myapplication.utils.RecycleViewAdapter
import com.example.myapplication.presentation.viewmodel.ListViewModel
import com.example.myapplication.presentation.viewmodel.ListViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class HabitListFragment(private val habitType: HabitType) : Fragment(), RecycleViewAdapter.OnItemClickListener {

    private lateinit var addButton: FloatingActionButton
    private lateinit var habitsRecycleView: RecyclerView
    private lateinit var adapter: RecycleViewAdapter

    private var allData: List<HabitRecord> = ArrayList()
    private var filteredData: ArrayList<HabitRecord> = ArrayList()
    private val filteredPriorities: ArrayList<HabitPriority> = arrayListOf()
    private var filteredName: String = ""

    private lateinit var listViewModel: ListViewModel

    @Inject
    lateinit var habitRepository: HabitRepository

    @Inject
    lateinit var listViewModelFactory: ListViewModelFactory

    private val mutableLoadedHabit: MutableLiveData<HabitRecord> = MutableLiveData()
    private var loadedHabit: LiveData<HabitRecord> = mutableLoadedHabit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.list_fragment, container, false)

        (requireActivity().application as MainApp).appComponent.inject(this)

        addButton = view.findViewById<FloatingActionButton>(R.id.add_fab)
        habitsRecycleView = view.findViewById<RecyclerView>(R.id.recycler_view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listViewModel =
            ViewModelProvider(activity as AppCompatActivity, listViewModelFactory).get(ListViewModel::class.java)

        adapter =
            RecycleViewAdapter(filteredData, activity as Context, this, activity as MainActivity)

        habitsRecycleView.adapter = adapter
        habitsRecycleView.layoutManager = LinearLayoutManager(activity as Context)
        habitsRecycleView.setHasFixedSize(true)

        listViewModel.getHabitList(habitRepository)

        addButton.setOnClickListener {
            listViewModel.startAdding(
                getString(R.string.name_default),
                getString(R.string.description_default),
                getString(R.string.times_default),
                getString(R.string.period_default)
            )
        }

        listViewModel.nameFilter.observe(activity as AppCompatActivity) {
            filteredName = it
            filterAndUpdateList()
        }

        listViewModel.lowPriorityFilter.observe(activity as AppCompatActivity) {
            setPriorityFilterStatus(HabitPriority.LOW, it)
            filterAndUpdateList()
        }

        listViewModel.mediumPriorityFilter.observe(activity as AppCompatActivity) {
            setPriorityFilterStatus(HabitPriority.MEDIUM, it)
            filterAndUpdateList()
        }

        listViewModel.highPriorityFilter.observe(activity as AppCompatActivity) {
            setPriorityFilterStatus(HabitPriority.HIGH, it)
            filterAndUpdateList()
        }

        loadedHabit.observe(activity as AppCompatActivity) {
            listViewModel.startEditing(it)
        }

        listViewModel.habitListLoaded.observe(activity as AppCompatActivity) {
            allData = it;
            filterAndUpdateList()
        }

        /*HabitList.currentHabitList.observe(activity as AppCompatActivity) {
            allData = it;
            filterAndUpdateList()
        }*/
    }

    fun filterAndUpdateList() {
        listViewModel.filterHabits(
            allData,
            arrayListOf(NameFilter(filteredName), TypeFilter(habitType), PriorityFilter(filteredPriorities)),
            filteredData
        )
        adapter.notifyDataSetChanged()
    }

    private fun setPriorityFilterStatus(habitPriority: HabitPriority, enable: Boolean) {
        if (enable) {
            filteredPriorities.add(habitPriority)
        } else {
            filteredPriorities.remove(habitPriority)
        }
    }

    override fun onItemClick(position: Int) {
        mutableLoadedHabit.value = filteredData[position]
    }
}