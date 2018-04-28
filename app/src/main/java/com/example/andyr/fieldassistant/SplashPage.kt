package com.andy.fieldassistant

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window

class SplashPage : AppCompatActivity() {

    val SPLASH_TIME_OUT = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_splash_page)

        Handler().postDelayed({
            val intent = Intent(this, Report1Activity::class.java)
            startActivity(intent)
        }, SPLASH_TIME_OUT.toLong())
    }
}
