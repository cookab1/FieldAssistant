package com.example.andyr.fieldassistant

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.report2.*

/**
 * Created by andyr on 2/24/2018.
 */
class Report2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report2)
        dictate_message.setOnClickListener { dictate() }
        type_message.setOnClickListener { type() }
    }

    private fun dictate() {
        val intent = Intent(this, Report3Activity::class.java)
        startActivity(intent)
    }

    private fun type() {
        val intent = Intent(this, Report3Activity::class.java)
        startActivity(intent)
    }
}