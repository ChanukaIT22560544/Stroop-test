package com.example.strooptest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Start : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val restartButton: Button = findViewById(R.id.restartButton)
        val highScoreButton: Button = findViewById(R.id.highScoreButton)
        val exitButton: Button = findViewById(R.id.exitButton)

        restartButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        highScoreButton.setOnClickListener {
            val intent = Intent(this, HighScoreActivity::class.java)
            startActivity(intent)
        }

        exitButton.setOnClickListener {
            finishAffinity() // Closes the app
        }
    }
}
