package com.example.myapplication

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class FirstActivity : BaseActivity() {
    lateinit var switchButton: Button
    lateinit var numberText: TextView
    var number: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        switchButton = findViewById<Button>(R.id.switchButton)
        numberText = findViewById<TextView>(R.id.number)

        if (intent != null) {
            number = intent.getStringExtra("num")?.toInt() ?: 0;
        }
        numberText.text = number.toString()

        switchButton.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java).apply {
                val bundle: Bundle = Bundle().apply { putString("num", number.toString()) }
                putExtras(bundle)
            }
            startActivity(intent)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        number++
        numberText.text = number.toString()
    }
}