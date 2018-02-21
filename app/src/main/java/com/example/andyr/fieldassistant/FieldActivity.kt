package com.example.andyr.fieldassistant

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class FieldActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_field)
    }
    /* //This may need to be put in Fragment
    fun newIntent() {
        var intent = Intent(this, ::class.java)
        startActivity(intent)
        finish()
    }
    */
}
