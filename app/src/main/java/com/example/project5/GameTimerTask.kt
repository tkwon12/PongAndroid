package com.example.project5

import android.os.Handler
import android.os.Looper

class GameTimerTask(private val updateInterval: Long, private val updateTask: () -> Unit) {

    private val handler = Handler(Looper.getMainLooper())
    private val gameLoop = object : Runnable {
        override fun run() {
            updateTask()
            handler.postDelayed(this, updateInterval)
        }
    }

    fun start() {
        handler.post(gameLoop)
    }

    fun stop() {
        handler.removeCallbacks(gameLoop)
    }
}