package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // hiring title bar of this activity
        window.requestFeature(Window.FEATURE_NO_TITLE)
        // full screen activity
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)
        // 5 seconds splash
        Handler().postDelayed({
            // start onboard one activity
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            finish()

        }, 5000)


    }
}