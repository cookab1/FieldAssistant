package com.example.andyr.fieldassistant

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.report2.*
import kotlinx.android.synthetic.main.report3.*

/**
 * Created by andyr on 2/24/2018.
 */
class Report3Activity : AppCompatActivity() {

    private val TYPE_CODE = 0;
    private val DICTATE_CODE = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report3)
        field_image_3.setImageBitmap(BitmapSender.instance.getBitmap())

        keyboardInit(intent.extras.getInt("keyboard_mode"));

        send_message.setOnClickListener { send() }
    }

    private fun send() {
        val intent = Intent(this, Report1Activity::class.java)
        startActivity(intent)
    }

    private fun keyboardInit(code: Int) {

        //open keyboard automatically if type
        if(code == TYPE_CODE) {
            field_message_3.performClick();

            //open keyboard then enable dictation automatically if dictate
        } else if(code == DICTATE_CODE) {

        }}
}