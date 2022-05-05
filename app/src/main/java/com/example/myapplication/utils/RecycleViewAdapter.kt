package com.example.myapplication.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.activity.MainActivity
import com.example.myapplication.data.*
import com.example.myapplication.domain.HabitRecord

class RecycleViewAdapter(private var habitList: List<HabitRecord>, private val context: Context,
                         private val listener: OnItemClickListener, private val habitDoneContext: MainActivity
): RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), OnClickListener {
        val nameView: TextView = itemView.findViewById<TextView>(R.id.item_name)
        val descriptionView: TextView = itemView.findViewById<TextView>(R.id.item_description)
        val priorityView: TextView = itemView.findViewById<TextView>(R.id.item_priority)
        val typeView: TextView = itemView.findViewById<TextView>(R.id.item_type)
        val timesView: TextView = itemView.findViewById<TextView>(R.id.item_times)
        val periodView: TextView = itemView.findViewById<TextView>(R.id.item_period)
        val doneButton: Button = itemView.findViewById<Button>(R.id.done_button)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_list_item, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = habitList[position]

        holder.nameView.text = currentItem.name
        holder.descriptionView.text = currentItem.description

        holder.typeView.text = if (currentItem.type == HabitType.NEGATIVE) {
            context.getString(R.string.type_negative)
        } else {
            context.getString(R.string.type_positive)
        }

        holder.priorityView.text = if (currentItem.priority == HabitPriority.HIGH) {
            context.getString(R.string.priority_high)
        } else if (currentItem.priority == HabitPriority.MEDIUM) {
            context.getString(R.string.priority_medium)
        } else {
            context.getString(R.string.priority_low)
        }

        holder.timesView.text = currentItem.times
        holder.periodView.text = currentItem.period
        holder.itemView.setBackgroundColor(Colors.colors[currentItem.colorIndex])

        holder.doneButton.setOnClickListener {
            habitDoneContext.habitDone(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return habitList.size
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}