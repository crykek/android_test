package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.*

class SecondActivity : BaseActivity() {
    private lateinit var nameField: EditText
    private lateinit var descriptionField: EditText
    private lateinit var priorityField: Spinner
    private lateinit var typeField: RadioGroup
    private lateinit var timesField: EditText
    private lateinit var periodField: EditText
    private lateinit var colorPicker: HorizontalScrollView
    private lateinit var colorPickerLayout: LinearLayout
    private lateinit var colorPickerCurrentColor: ImageView
    private lateinit var colorPickerCurrentRGB: TextView
    private lateinit var colorPickerCurrentHSV: TextView

    private var colorImages: ArrayList<ImageView> = ArrayList()

    private var currentColor: Int = 0

    private lateinit var addButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        addButton = findViewById<Button>(R.id.add_habit_button)
        initializeFields()

        if (intent.extras != null) {
            fillFieldsWithData(intent)
        } else {
            setCurrentColor(currentColor)
        }

        addButton.setOnClickListener {
            val intent = Intent(this, FirstActivity::class.java).apply {
                val bundle: Bundle = Bundle().apply {
                    val colorDrawable: ColorDrawable = colorImages[currentColor].background as ColorDrawable
                    putString("name", nameField.text.toString())
                    putString("description", descriptionField.text.toString())
                    putString("priority", priorityField.selectedItem.toString())
                    putString("type", typeField.checkedRadioButtonId.toString())
                    putString("times", timesField.text.toString())
                    putString("period", periodField.text.toString())
                    putString("color", colorDrawable.color.toString())
                    putString("colorIndex", currentColor.toString())
                }
                putExtras(bundle)
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun fillFieldsWithData(intent: Intent?) {
        val name: String = intent?.getStringExtra("name") ?: ""
        val description: String = intent?.getStringExtra("description") ?: ""

        val priority: HabitPriority = when (intent?.getStringExtra("priority")) {
            HabitPriority.HIGH.toString() -> HabitPriority.HIGH
            HabitPriority.MEDIUM.toString() -> HabitPriority.MEDIUM
            else -> HabitPriority.LOW
        }

        val type: HabitType = if (intent?.getStringExtra("type") == HabitType.POSITIVE.toString()) {
            HabitType.POSITIVE
        } else {
            HabitType.NEGATIVE
        }

        val times: String = intent?.getStringExtra("times") ?: ""
        val period: String = intent?.getStringExtra("period") ?: ""

        nameField.setText(name)
        descriptionField.setText(description)

        priorityField.setSelection(when (priority) {
            HabitPriority.HIGH -> 0
            HabitPriority.MEDIUM -> 1
            else -> 2
        })

        typeField.check(if (type == HabitType.POSITIVE) {
            R.id.habit_type_button_positive
        } else {
            R.id.habit_type_button_negative
        })

        timesField.setText(times)
        periodField.setText(period)

        val colorIndex: Int = intent?.getStringExtra("colorIndex")?.toInt() ?: 0
        setCurrentColor(colorIndex)
    }

    private fun initializeFields() {
        nameField = findViewById<EditText>(R.id.habit_name_field)
        descriptionField = findViewById<EditText>(R.id.habit_description_field)
        priorityField = findViewById<Spinner>(R.id.habit_priority_field)
        typeField = findViewById<RadioGroup>(R.id.habit_type_radio_group)
        timesField = findViewById<EditText>(R.id.habit_times_field)
        periodField = findViewById<EditText>(R.id.habit_period_field)
        colorPicker = findViewById<HorizontalScrollView>(R.id.color_picker)
        colorPickerLayout = findViewById<LinearLayout>(R.id.color_picker_layout)
        colorPickerCurrentColor = findViewById<ImageView>(R.id.current_color_image)
        colorPickerCurrentRGB = findViewById<TextView>(R.id.current_color_rgb)
        colorPickerCurrentHSV = findViewById<TextView>(R.id.current_color_hsv)

        val gradientDrawable: GradientDrawable = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(
                0XFFFF0000.toInt(),
                0XFFFFFF00.toInt(),
                0XFF00FF00.toInt(),
                0XFF00FFFF.toInt(),
                0XFF0000FF.toInt(),
                0XFFFF00FF.toInt(),
                0XFFFF0000.toInt())
        )

        colorPickerLayout.background = gradientDrawable

        findViewById<RadioButton>(R.id.habit_type_button_positive).isChecked = true

        for (i in 1 until 17) {
            val id = resources.getIdentifier("color${i}", "id", packageName)
            val colorView: ImageView = findViewById<ImageView>(id)
            colorImages.add(colorView)
            colorView.setOnClickListener {
                currentColor = i - 1
                setCurrentColor(currentColor)
            }
        }
    }

    private fun setCurrentColor(position: Int) {
        currentColor = position
        val colorDrawable: ColorDrawable = colorImages[currentColor].background as ColorDrawable
        val color = colorDrawable.color
        colorPickerCurrentColor.background = colorImages[currentColor].background

        val r = color shr 16 and 0xFF
        val g = color shr 8 and 0xFF
        val b = color shr 0 and 0xFF
        colorPickerCurrentRGB.text = String.format(getString(R.string.color_picker_current_rgb), r, g, b)

        val hsv = floatArrayOf(0f, 0f, 0f)
        Color.RGBToHSV(r, g, b, hsv)
        colorPickerCurrentHSV.text = String.format(getString(R.string.color_picker_current_hsv), hsv[0], hsv[1], hsv[2])
    }
}