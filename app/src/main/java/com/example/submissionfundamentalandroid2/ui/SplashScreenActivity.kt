package com.example.submissionfundamentalandroid2.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.submissionfundamentalandroid2.ui.main.MainActivity
import com.example.submissionfundamentalandroid2.R

class SplashScreenActivity : AppCompatActivity() {
    companion object{
        const val MILIS_TIME : Long = 2000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, MILIS_TIME)
    }
}