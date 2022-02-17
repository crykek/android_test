package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

open class BaseActivity: AppCompatActivity() {
    private val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
    private lateinit var logFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        val logFilePath: String = "${this.applicationContext.filesDir.absolutePath}/log.txt"
        logFile = File(logFilePath)
        if (!logFile.exists()) {
            logFile.createNewFile()
        }
        logFile.appendText("${sdf.format(Date())}: ${this.localClassName} is created\n")
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        logFile.appendText("${sdf.format(Date())}: ${this.localClassName} is started\n")
        super.onStart()
    }

    override fun onPause() {
        logFile.appendText("${sdf.format(Date())}: ${this.localClassName} is paused\n")
        super.onPause()
    }

    override fun onResume() {
        logFile.appendText("${sdf.format(Date())}: ${this.localClassName} is resumed\n")
        super.onResume()
    }

    override fun onStop() {
        logFile.appendText("${sdf.format(Date())}: ${this.localClassName} is stopped\n")
        super.onStop()
    }

    override fun onRestart() {
        logFile.appendText("${sdf.format(Date())}: ${this.localClassName} is restarted\n")
        super.onRestart()
    }

    override fun onDestroy() {
        logFile.appendText("${sdf.format(Date())}: ${this.localClassName} is destroyed\n")
        super.onDestroy()
    }
}