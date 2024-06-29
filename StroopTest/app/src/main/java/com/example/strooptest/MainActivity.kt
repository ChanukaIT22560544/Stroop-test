package com.example.strooptest

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private lateinit var wordTextView: TextView
    private lateinit var scoreTextView: TextView
    private lateinit var timerTextView: TextView

    private lateinit var redButton: Button
    private lateinit var blueButton: Button
    private lateinit var greenButton: Button
    private lateinit var yellowButton: Button
    private lateinit var magentaButton: Button
    private lateinit var brownButton: Button
    private lateinit var blackButton: Button
    private lateinit var whiteButton: Button
    private lateinit var grayButton: Button

    private val words = listOf("RED", "BLUE", "GREEN", "YELLOW", "MAGENTA", "BROWN", "BLACK", "WHITE", "GRAY")
    private val colors = listOf(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.rgb(165, 42, 42), Color.BLACK, Color.WHITE, Color.GRAY)

    private val soundMap = mapOf(
        "RED" to R.raw.red,
        "BLUE" to R.raw.blue,
        "GREEN" to R.raw.green,
        "YELLOW" to R.raw.yellow,
        "MAGENTA" to R.raw.magenta,
        "BROWN" to R.raw.brown,
        "BLACK" to R.raw.black,
        "WHITE" to R.raw.white,
        "GRAY" to R.raw.gray
    )

    private var score = 0
    private var highScore = 0
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var timer: CountDownTimer
    private var gameStarted = false
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wordTextView = findViewById(R.id.wordTextView)
        scoreTextView = findViewById(R.id.scoreTextView)
        timerTextView = findViewById(R.id.timerTextView)

        redButton = findViewById(R.id.redButton)
        blueButton = findViewById(R.id.blueButton)
        greenButton = findViewById(R.id.greenButton)
        yellowButton = findViewById(R.id.yellowButton)
        magentaButton = findViewById(R.id.magentaButton)
        brownButton = findViewById(R.id.brownButton)
        blackButton = findViewById(R.id.blackButton)
        whiteButton = findViewById(R.id.whiteButton)
        grayButton = findViewById(R.id.grayButton)

        sharedPreferences = getSharedPreferences("stroop_test_prefs", MODE_PRIVATE)
        highScore = sharedPreferences.getInt("high_score", 0)

        redButton.setOnClickListener { checkAnswer(Color.RED) }
        blueButton.setOnClickListener { checkAnswer(Color.BLUE) }
        greenButton.setOnClickListener { checkAnswer(Color.GREEN) }
        yellowButton.setOnClickListener { checkAnswer(Color.YELLOW) }
        magentaButton.setOnClickListener { checkAnswer(Color.MAGENTA) }
        brownButton.setOnClickListener { checkAnswer(Color.rgb(165, 42, 42)) }
        blackButton.setOnClickListener { checkAnswer(Color.BLACK) }
        whiteButton.setOnClickListener { checkAnswer(Color.WHITE) }
        grayButton.setOnClickListener { checkAnswer(Color.GRAY) }

        startGame()
    }

    private fun startGame() {
        score = 0
        updateScore()
        startTimer()
        gameStarted = true
        nextRound()
    }

    private fun startTimer() {
        timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                timerTextView.text = "Time: $secondsRemaining"
            }

            override fun onFinish() {
                endGame()
            }
        }.start()
    }

    private fun nextRound() {
        val randomWord = words.random()
        val randomColor = colors.random()

        wordTextView.text = randomWord
        wordTextView.setTextColor(randomColor)

        // Play the sound for the displayed word
        playSound(randomWord)
    }

    private fun playSound(word: String) {
        val soundResId = soundMap[word]
        if (soundResId != null) {
            if (::mediaPlayer.isInitialized) {
                mediaPlayer.release()
            }
            mediaPlayer = MediaPlayer.create(this, soundResId)
            mediaPlayer.start()
        }
    }

    private fun checkAnswer(selectedColor: Int) {
        if (!gameStarted) return

        val correctColor = wordTextView.currentTextColor
        if (selectedColor == correctColor) {
            score++
        } else {
            score -= 2
        }
        updateScore()
        nextRound()
    }

    private fun updateScore() {
        scoreTextView.text = "Score: $score"
    }

    private fun endGame() {
        gameStarted = false
        timer.cancel()

        if (score > highScore) {
            highScore = score
            showHighScoreDialog()
        } else {
            showEndGameDialog()
        }
    }

    private fun showHighScoreDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_high_score, null)
        val playerNameInput = dialogView.findViewById<TextInputEditText>(R.id.playerNameInput)

        val alertDialog = AlertDialog.Builder(this)
            .setTitle("                       Congratulations!\n You got the high score")

            .setMessage("Your score: $score")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val playerName = playerNameInput.text.toString()
                val editor = sharedPreferences.edit()
                editor.putInt("high_score", highScore)
                editor.putString("high_score_name", playerName)
                editor.apply()
                navigateToRestartActivity()
            }
            .setNegativeButton("Cancel") { _, _ ->
                navigateToRestartActivity()
            }
            .setCancelable(false)
            .create()

        alertDialog.show()
    }

    private fun showEndGameDialog() {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Game Over")
            .setMessage("Your score: $score\nHigh Score: $highScore")
            .setPositiveButton("OK") { _, _ ->
                navigateToRestartActivity()
            }
            .setCancelable(false)
            .create()

        alertDialog.show()
    }

    private fun navigateToRestartActivity() {
        val intent = Intent(this, RestartActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }
}
