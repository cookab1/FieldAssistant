package com.andy.fieldassistant

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import kotlinx.android.synthetic.main.activity_splash_page.*

class SplashPage : AppCompatActivity() {

    val SPLASH_TIME_OUT = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_splash_page)

        Handler().postDelayed({
            val intent = Intent(this, Report1Activity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent)
        }, SPLASH_TIME_OUT.toLong())

        splash_button.setOnClickListener {
            val intent = Intent(this, Report1Activity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent)
        }
    }
}
