package com.example.project5

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var gameView: GameView
    private lateinit var gameTimerTask: GameTimerTask

    companion object {
        private const val FRAME_RATE = 16L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        gameView = GameView(this)
        setContentView(gameView)


        gameView.pongGame.loadBestScore()


        gameTimerTask = GameTimerTask(FRAME_RATE) {
        }
    }


    override fun onResume() {
        super.onResume()
        gameTimerTask.start()
    }


    override fun onPause() {
        super.onPause()
        gameTimerTask.stop()
    }


    override fun onDestroy() {
        super.onDestroy()
        gameView.pongGame.saveBestScore()
    }

    private fun resetBestScore() {
        val sharedPref = getSharedPreferences("PongGamePreferences", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("BestScore", 0)
            apply()
        }
        gameView.pongGame.bestScore = 0

    }
}