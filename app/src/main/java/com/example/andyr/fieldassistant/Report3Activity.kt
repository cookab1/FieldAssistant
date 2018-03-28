package com.example.andyr.fieldassistant

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.format.DateFormat
import kotlinx.android.synthetic.main.report3.*
import android.text.Editable
import android.text.TextWatcher
import android.os.Environment.DIRECTORY_PICTURES


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
        report = Report()
        field_image_3.setImageBitmap(BitmapSender.instance.getBitmap())
        messageListener()
        emailListener()

        //keyboardInit(intent.extras.getInt("keyboard_mode"));

        send_message.setOnClickListener { send() }
    }

    private fun send() {
        var emailList = arrayOf(report.getRecipient())
        var intent: Intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, emailList)
        intent.putExtra(Intent.EXTRA_TEXT, getReport())
        //intent = Intent.createChooser(intent, getString(R.string.send_report))
        startActivity(intent)

        //intent = Intent(this, Report1Activity::class.java)
        //startActivity(intent)
    }
    private fun getReport(): String {

        var message = report.getMessage()
        if(message == null)
            return "no text"

        return message
        /*
        val dateFormat = "EEE, MMM dd"
        val dateString = DateFormat.format(dateFormat, report.getDate()).toString()

        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect)
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect)
        }

        return getString(R.string.report, message, report.getLocation(), dateString)
        */
    }

    private fun keyboardInit(code: Int) {

        //open keyboard automatically if type
        if(code == TYPE_CODE) {
            field_message_3.performClick();

            //open keyboard then enable dictation automatically if dictate
        } else if(code == DICTATE_CODE) {

        }
    }

    fun messageListener() {
        field_message_3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                    s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(
                    s: CharSequence, start: Int, before: Int, count: Int) {
                report.setMessage(s.toString())
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
    }

    fun emailListener() {
        choose_recipient.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                    s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(
                    s: CharSequence, start: Int, before: Int, count: Int) {
                    report.setRecipient(s.toString())
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
    }
}