package com.example.myapplication.fragment

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.data.*
import com.example.myapplication.viewmodel.EditViewModel
import com.example.myapplication.viewmodel.EditViewModelFactory

class EditFragment : Fragment() {

    private var colorImages: ArrayList<ImageView> = ArrayList()
    private var colors: ArrayList<Int> = ArrayList()

    private lateinit var nameField: EditText
    private lateinit var descriptionField: EditText
    private lateinit var priorityField: Spinner
    private lateinit var typeField: RadioGroup
    private lateinit var typeFieldPositive: RadioButton
    private lateinit var typeFieldNegative: RadioButton
    private lateinit var timesField: EditText
    private lateinit var periodField: EditText
    private lateinit var colorPicker: HorizontalScrollView
    private lateinit var colorPickerLayout: LinearLayout
    private lateinit var colorPickerCurrentColor: ImageView
    private lateinit var colorPickerCurrentRGB: TextView
    private lateinit var colorPickerCurrentHSV: TextView
    private lateinit var addButton: Button

    private lateinit var editViewModel: EditViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.add_habit_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editViewModel =
            ViewModelProvider(activity as AppCompatActivity, EditViewModelFactory()).get(EditViewModel::class.java)

        initializeFields(view)

        addButton.setOnClickListener {
            submit()
        }

        editViewModel.rGBString.observe(activity as AppCompatActivity) {
            colorPickerCurrentRGB.text = it
        }

        editViewModel.hSVString.observe(activity as AppCompatActivity) {
            colorPickerCurrentHSV.text = it
        }

        editViewModel.currentColorIndex.observe(activity as AppCompatActivity) {
            colorPickerCurrentColor.background = colorImages[it].background
        }
    }

    private fun initializeFields(view: View) {
        addButton = view.findViewById<Button>(R.id.add_habit_button)

        nameField = view.findViewById<EditText>(R.id.habit_name_field)
        descriptionField = view.findViewById<EditText>(R.id.habit_description_field)
        priorityField = view.findViewById<Spinner>(R.id.habit_priority_field)
        typeField = view.findViewById<RadioGroup>(R.id.habit_type_radio_group)
        typeFieldPositive = view.findViewById<RadioButton>(R.id.habit_type_positive)
        typeFieldNegative = view.findViewById<RadioButton>(R.id.habit_type_negative)
        timesField = view.findViewById<EditText>(R.id.habit_times_field)
        periodField = view.findViewById<EditText>(R.id.habit_period_field)
        colorPicker = view.findViewById<HorizontalScrollView>(R.id.color_picker)
        colorPickerLayout = view.findViewById<LinearLayout>(R.id.color_picker_layout)
        colorPickerCurrentColor = view.findViewById<ImageView>(R.id.current_color_image)
        colorPickerCurrentRGB = view.findViewById<TextView>(R.id.current_color_rgb)
        colorPickerCurrentHSV = view.findViewById<TextView>(R.id.current_color_hsv)

        val gradientDrawable: GradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(
                0XFFFF0000.toInt(),
                0XFFFFFF00.toInt(),
                0XFF00FF00.toInt(),
                0XFF00FFFF.toInt(),
                0XFF0000FF.toInt(),
                0XFFFF00FF.toInt(),
                0XFFFF0000.toInt()
            )
        )

        colorPickerLayout.background = gradientDrawable

        for (i in 1 until 17) {
            val id = resources.getIdentifier("color${i}", "id", activity?.packageName)
            val colorView: ImageView = view.findViewById<ImageView>(id)
            colorImages.add(colorView)

            val colorDrawable: ColorDrawable = colorView.background as ColorDrawable
            colors.add(colorDrawable.color)

            colorView.setOnClickListener {
                editViewModel.setCurrentColor(i - 1, colorDrawable.color)
            }
        }

        editViewModel.setCurrentColor(0, Colors.colors[0])
    }

    private fun setValues(
        name: String, description: String, priority: HabitPriority, type: HabitType,
        times: String, period: String, colorIndex: Int
    ) {
        nameField.setText(name)
        descriptionField.setText(description)

        priorityField.setSelection(priority.getIndex())
        typeField.check(
            if (type == HabitType.POSITIVE) {
                typeFieldPositive.id
            } else {
                typeFieldNegative.id
            }
        )

        timesField.setText(times)
        periodField.setText(period)

        editViewModel.setCurrentColor(colorIndex, colors[colorIndex])
    }

    private fun submit() {
        editViewModel.submit(
            HabitList.currentHabit.uid,
            nameField.text.toString(),
            descriptionField.text.toString(),
            priorityField.selectedItemPosition,
            if (typeField.checkedRadioButtonId == typeFieldPositive.id) {
                HabitType.POSITIVE
            } else {
                HabitType.NEGATIVE
            },
            timesField.text.toString(),
            periodField.text.toString(),
            colors[editViewModel.currentColorIndex.value ?: 0]
        )
    }

    fun setValues(habitRecord: HabitRecord) {
        setValues(
            habitRecord.name,
            habitRecord.description,
            habitRecord.priority,
            habitRecord.type,
            habitRecord.times,
            habitRecord.period,
            habitRecord.colorIndex
        )
    }

}