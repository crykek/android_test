package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FirstActivity : BaseActivity(), RecycleViewAdapter.OnItemClickListener {

    private lateinit var addButton: FloatingActionButton
    private lateinit var habitsRecycleView: RecyclerView
    private var habitsList: ArrayList<HabitRecord> = ArrayList()
    private var adapter: RecycleViewAdapter = RecycleViewAdapter(habitsList, this, this)
    private var currentPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_first)
        addButton = findViewById<FloatingActionButton>(R.id.add_fab)
        habitsRecycleView = findViewById<RecyclerView>(R.id.recycler_view)

        habitsRecycleView.adapter = adapter
        habitsRecycleView.layoutManager = LinearLayoutManager(this)
        habitsRecycleView.setHasFixedSize(true)

        addButton.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivityForResult(intent, RequestCode.ADD.getCode())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RequestCode.ADD.getCode()) {
                habitsList.add(getRecordFromData(data))
                adapter = RecycleViewAdapter(habitsList, this, this)
                habitsRecycleView.adapter = adapter
            } else if (requestCode == RequestCode.EDIT.getCode()) {
                editRecord(currentPosition, data)
                adapter = RecycleViewAdapter(habitsList, this, this)
                habitsRecycleView.adapter = adapter
            }
        }
    }

    private fun editRecord(position: Int, intent: Intent?) {
        if (position == -1) {
            return
        }

        val newRecord = getRecordFromData(intent)
        habitsList[position] = newRecord
    }

    private fun getRecordFromData(intent: Intent?): HabitRecord {
        val name: String = intent?.getStringExtra("name") ?: ""
        val description: String = intent?.getStringExtra("description") ?: ""

        val priority: HabitPriority = if (intent?.getStringExtra("priority") == getString(R.string.priority_high)) {
            HabitPriority.HIGH
        } else if (intent?.getStringExtra("priority") == getString(R.string.priority_medium)) {
            HabitPriority.MEDIUM
        } else {
            HabitPriority.LOW
        }

        val type: HabitType = if (intent?.getStringExtra("type") == R.id.habit_type_button_positive.toString()) {
            HabitType.POSITIVE
        } else {
            HabitType.NEGATIVE
        }

        val times: String = intent?.getStringExtra("times") ?: ""
        val period: String = intent?.getStringExtra("period") ?: ""
        val color: Int = intent?.getStringExtra("color")?.toInt() ?: 0
        val colorIndex: Int = intent?.getStringExtra("colorIndex")?.toInt() ?: 0

        return HabitRecord(name, description, priority, type, times, period, color, colorIndex)
    }

    override fun onItemClick(position: Int) {
        currentPosition = position
        val habit = habitsList[position]

        val intent = Intent(this, SecondActivity::class.java)
        val bundle: Bundle = Bundle().apply {
            putString("name", habit.name)
            putString("description", habit.description)
            putString("priority", habit.priority.toString())
            putString("type", habit.type.toString())
            putString("times", habit.times)
            putString("period", habit.period)
            putString("colorIndex", habit.colorIndex.toString())
        }
        intent.putExtras(bundle)
        startActivityForResult(intent, RequestCode.EDIT.getCode())
    }
}