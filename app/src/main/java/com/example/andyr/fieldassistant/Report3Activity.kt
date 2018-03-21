package com.example.andyr.fieldassistant

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.format.DateFormat
import kotlinx.android.synthetic.main.report3.*


/**
 * Created by andyr on 2/24/2018.
 */
class Report3Activity : AppCompatActivity() {

    private val TYPE_CODE = 0;
    private val DICTATE_CODE = 1;
    private lateinit var report: Report;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report3)
        field_image_3.setImageBitmap(BitmapSender.instance.getBitmap())

        keyboardInit(intent.extras.getInt("keyboard_mode"));

        send_message.setOnClickListener { send() }
    }

    private fun send() {
        var emailList = arrayOf("andyrock.cook@gmail.com")
        var intent: Intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, emailList)
        intent.putExtra(Intent.EXTRA_TEXT, getReport())
        intent = Intent.createChooser(intent, getString(R.string.send_report))
        startActivity(intent)

        intent = Intent(this, Report1Activity::class.java)
        startActivity(intent)
    }

    private fun getReport(): String {
        
        val dateFormat = "EEE, MMM dd"
        val dateString = DateFormat.format(dateFormat, report.getDate()).toString()

        var message = report.getMessage()
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect)
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect)
        }

        return getString(R.string.report, report.getLocation(), dateString, solvedString, suspect)
    }

    private fun keyboardInit(code: Int) {

        //open keyboard automatically if type
        if(code == TYPE_CODE) {
            field_message_3.performClick();

            //open keyboard then enable dictation automatically if dictate
        } else if(code == DICTATE_CODE) {

        }
    }

}