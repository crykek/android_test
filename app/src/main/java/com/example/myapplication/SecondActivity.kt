package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class SecondActivity : BaseActivity() {
    lateinit var switchButton: Button
    lateinit var numberText: TextView
    private var number: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        switchButton = findViewById<Button>(R.id.switchButton2)
        numberText = findViewById<TextView>(R.id.number2)

        number = intent.getStringExtra("num")?.toInt() ?: 0
        numberText.text = (number * number).toString()

        switchButton.setOnClickListener {
            val intent = Intent(this, FirstActivity::class.java).apply {
                val bundle: Bundle = Bundle().apply { putString("num", number.toString()) }
                putExtras(bundle)
            }
            startActivity(intent)
        }
    }
}