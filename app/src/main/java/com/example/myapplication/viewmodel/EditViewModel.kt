package com.example.myapplication.viewmodel

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.HabitList
import com.example.myapplication.data.HabitPriority
import com.example.myapplication.data.HabitRecord
import com.example.myapplication.data.HabitType
import com.example.myapplication.utils.Action
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditViewModel : ViewModel() {

    private val mutableRGBString: MutableLiveData<String> = MutableLiveData()
    val rGBString: LiveData<String> = mutableRGBString

    private val mutableHSVString: MutableLiveData<String> = MutableLiveData()
    val hSVString: LiveData<String> = mutableHSVString

    private val mutableCurrentColorIndex: MutableLiveData<Int> = MutableLiveData()
    val currentColorIndex: LiveData<Int> = mutableCurrentColorIndex

    private val mutableHabitChangedFlag: MutableLiveData<Boolean> = MutableLiveData()
    val habitChangedFlag: LiveData<Boolean> = mutableHabitChangedFlag

    private val mutableHabitSubmitted: MutableLiveData<Boolean> = MutableLiveData()
    val habitSubmitted: LiveData<Boolean> = mutableHabitSubmitted

    fun setCurrentColor(position: Int, color: Int) {
        mutableCurrentColorIndex.value = position
        setColorStrings(color)
    }

    private fun setColorStrings(color: Int) {
        val r = color shr 16 and 0xFF
        val g = color shr 8 and 0xFF
        val b = color shr 0 and 0xFF
        mutableRGBString.value = String.format("R: %d G: %d B: %d", r, g, b)

        val hsv = floatArrayOf(0f, 0f, 0f)
        Color.RGBToHSV(r, g, b, hsv)
        mutableHSVString.value = String.format("H: %.0f S: %.2f V: %.2f", hsv[0], hsv[1], hsv[2])
    }

    fun submit(
        nameFieldValue: String,
        descriptionFieldValue: String,
        priorityFieldValue: Int,
        typeFieldValue: HabitType,
        timesFieldValue: String,
        periodFieldValue: String,
        color: Int
    ) {

        val currentHabit = HabitList.currentHabit

        currentHabit.name = nameFieldValue
        currentHabit.description = descriptionFieldValue
        currentHabit.priority = HabitPriority.getPriorityFromIndex(priorityFieldValue)
        currentHabit.type = typeFieldValue
        currentHabit.times = timesFieldValue
        currentHabit.period = periodFieldValue
        currentHabit.color = color
        currentHabit.colorIndex = mutableCurrentColorIndex.value ?: 0

        GlobalScope.launch(Dispatchers.IO) {

            if (currentHabit.position >= HabitList.currentCount) {
                HabitList.addHabit(currentHabit)
            } else {
                HabitList.updateHabit(currentHabit)
            }

            mutableHabitChangedFlag.postValue(true)
        }

        mutableHabitSubmitted.value = true
    }
}