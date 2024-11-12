package com.example.project5

import android.content.Context

class Pong(private val context: Context) {
    var ballX = 0f
    var ballY = 0f
    var ballSpeedX = 10f
    var ballSpeedY = 10f
    val ballRadius = 20f

    var paddleX = 0f
    var paddleY = 0f
    val paddleWidth = 200f
    val paddleHeight = 30f


    var score = 0
    var bestScore = 0
    var isGameOver = false

    private set
    init {
        loadBestScore()
    }

    fun resetGame(screenWidth: Int, screenHeight: Int) {
        ballX = screenWidth / 2f
        ballY = 100f
        ballSpeedX = 10f
        ballSpeedY = 10f
        score = 0
        isGameOver = false

        paddleX = (screenWidth - paddleWidth) / 2f
        paddleY = screenHeight - paddleHeight * 10

    }

    fun updateBallPosition() {
        ballX += ballSpeedX
        ballY += ballSpeedY
    }

    fun movePaddle(newX: Float,screenWidth: Int) {
        paddleX = newX - paddleWidth / 2


        if (paddleX < 0) {
            paddleX = 0f
        }

        if (paddleX + paddleWidth > screenWidth) {
            paddleX = screenWidth - paddleWidth
        }

    }

    fun checkCollision(width: Int, height: Int): Boolean {

        if (ballX <= ballRadius || ballX >= width - ballRadius) {
            ballSpeedX = -ballSpeedX
        }
        if (ballY <= ballRadius) {
            ballSpeedY = -ballSpeedY
        }


        if (ballY + ballRadius >= paddleY &&
            ballY < paddleY + paddleHeight &&  // 공이 패들의 위에 있는 경우에만 충돌
            ballX >= paddleX && ballX <= paddleX + paddleWidth) {
            ballSpeedY = -ballSpeedY
            score++
            return true
        }


        if (ballY > height) {
            if (score > bestScore) {
                bestScore = score
                saveBestScore()
            }
            isGameOver = true
        }
            return false
    }

   fun saveBestScore() {
        val sharedPref = context.getSharedPreferences("PongGamePreferences", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("BestScore", bestScore)
            apply()
        }
    }

    fun loadBestScore() {
        val sharedPref = context.getSharedPreferences("PongGamePreferences", Context.MODE_PRIVATE)
        bestScore = sharedPref.getInt("BestScore", 0)
    }

}