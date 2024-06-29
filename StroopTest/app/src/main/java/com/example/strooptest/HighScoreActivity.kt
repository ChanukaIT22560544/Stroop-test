package com.example.strooptest

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HighScoreActivity : AppCompatActivity() {

    private lateinit var highScoreTextView: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_score)

        highScoreTextView = findViewById(R.id.highScoreTextView)
        sharedPreferences = getSharedPreferences("stroop_test_prefs", MODE_PRIVATE)

        val highScore = sharedPreferences.getInt("high_score", 0)
        val highScoreName = sharedPreferences.getString("high_score_name", "No name")

        highScoreTextView.text = "High Score: $highScore\nName: $highScoreName"
    }
}
