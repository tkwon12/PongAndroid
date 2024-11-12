package com.example.project5

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.media.SoundPool
import android.view.MotionEvent
import android.view.View

class GameView(context: Context) : View(context) {

    private val paint = Paint()
    val pongGame = Pong(context)
    private var soundPool: SoundPool? = null
    private var hitSoundId: Int = -1
    private var gameStarted = false


    init {
        paint.color = BALL_COLOR
        soundPool = SoundPool.Builder().setMaxStreams(1).build()
        hitSoundId = soundPool?.load(context, R.raw.hit, 1) ?: -1
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        paint.color = BALL_COLOR
        canvas.drawCircle(pongGame.ballX, pongGame.ballY, pongGame.ballRadius, paint)

        paint.color = PADDLE_COLOR
        canvas.drawRect(
            pongGame.paddleX, pongGame.paddleY,
            pongGame.paddleX + pongGame.paddleWidth, pongGame.paddleY + pongGame.paddleHeight,
            paint
        )

        if (gameStarted && !pongGame.isGameOver) {
            pongGame.updateBallPosition()

            if (pongGame.checkCollision(width, height)) {
                soundPool?.play(hitSoundId, 1f, 1f, 0, 0, 1f)
            }


        }

        if (gameStarted&&!pongGame.isGameOver) {
            paint.color = SCORE_COLOR
            paint.textSize = 50f
            canvas.drawText("Score: ${pongGame.score}", 50f, 100f, paint)
            canvas.drawText("Best: ${pongGame.bestScore}", 50f, 200f, paint)
        }

        if (pongGame.isGameOver) {
            paint.color = TEXT_COLOR
            paint.textSize = 80f
            paint.textSize = 50f
            canvas.drawText("Score: ${pongGame.score}", width / 2f - 100, height / 2f, paint)
            canvas.drawText(
                "Best Score: ${pongGame.bestScore}",
                width / 2f - 150,
                height / 2f + 100,
                paint
            )
            gameStarted = false
        } else if (!gameStarted) {
            paint.color = Color.BLACK
            paint.textSize = 50f
            canvas.drawText("Tap to Start", width / 2f - 100, height / 2f, paint)
        }

        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        pongGame.resetGame(w, h)

        pongGame.paddleX = (w - pongGame.paddleWidth) / 2f  // 화면 가로 중앙
        pongGame.paddleY = h - pongGame.paddleHeight * 10

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (pongGame.isGameOver) {

                    pongGame.resetGame(width,height)
                    gameStarted = true
                    invalidate()
                } else if (!gameStarted) {

                    gameStarted = true
                    pongGame.resetGame(width,height)
                    invalidate()
                }
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                if (gameStarted && !pongGame.isGameOver) {

                    pongGame.movePaddle(event.x,width)
                    invalidate()
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    companion object {
        private const val TEXT_COLOR = Color.BLACK
        private const val BALL_COLOR = Color.BLACK
        private const val PADDLE_COLOR = Color.BLACK
        private const val SCORE_COLOR = Color.BLACK

    }
}