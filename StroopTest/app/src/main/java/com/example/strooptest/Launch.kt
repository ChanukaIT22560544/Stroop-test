package com.example.strooptest

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class Launch: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        // Delay for 3 seconds before moving to MainActivity
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, Start::class.java)
            startActivity(intent)
            finish()
        }, 1500) // 3000 milliseconds = 3 seconds
    }
}
