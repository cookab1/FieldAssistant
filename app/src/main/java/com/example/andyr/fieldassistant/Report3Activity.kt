package com.example.andyr.fieldassistant

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.format.DateFormat
import kotlinx.android.synthetic.main.report3.*
import android.text.Editable
import android.text.TextWatcher
import java.net.URI
import java.util.*


/**
 * Created by andyr on 2/24/2018.
 */
class Report3Activity : AppCompatActivity() {
    
    private val LOCATION_REQUEST_CODE = 10

    private val TYPE_CODE = 0
    private val DICTATE_CODE = 1
    private lateinit var report: Report
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report3)
        report = Report()
        field_image_3.setImageBitmap(BitmapSender.instance.getBitmap())
        messageListener()
        emailListener()

        var calendar: Calendar = Calendar.getInstance()
        report.setDate(calendar.time)

        report.setLocation().getLocation()

        //keyboardInit(intent.extras.getInt("keyboard_mode"));

        send_message.setOnClickListener { send() }
    }

    private fun send() {
        var emailList = arrayOf(report.getRecipient())
        var reportText = getReport()

        var intent: Intent = Intent(Intent.ACTION_SEND)
        intent.type = "plain/text"

        if(emailList != null)
            intent.putExtra(Intent.EXTRA_EMAIL, emailList)
        if(reportText != null)
            intent.putExtra(Intent.EXTRA_TEXT, getReport())
        //if(URI != null)
            //intent.putExtra(Intent.EXTRA_STREAM, URI)
        //intent = Intent.createChooser(intent, getString(R.string.send_report))
        startActivity(intent)

        //intent = Intent(this, Report1Activity::class.java)
        //startActivity(intent)
    }
    private fun getReport(): String? {

        val message = report.getMessage() + "\n"

        val dateFormat = "EEE, MMM dd hh:mm aa z"
        val dateString = "When: " + DateFormat.format(dateFormat, report.getDate()).toString() + "\n"
        val locationString = "Where: " + ""

        return message + dateString
        /*

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


    fun getLocation(): Location {
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        LOCATION_REQUEST_CODE)
            }
        }


        locationManager.requestLocationUpdates("gps", 1000, 0 as Float, locationListener)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}