package com.example.andyr.fieldassistant

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.format.DateFormat
import kotlinx.android.synthetic.main.report3.*
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import java.util.*


/**
 * Created by andyr on 2/24/2018.
 */
class Report3Activity : AppCompatActivity() {
    
    private val LOCATION_REQUEST_CODE = 10

    private val TYPE_CODE = 0
    private val DICTATE_CODE = 1
    private val SEND_CODE = 2
    private lateinit var report: Report
    private var photoUri: Uri? = null
    private lateinit var locationManager: LocationManager
    //define the listener
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            location_text.text = getLocationString(false)
            report.setLocation(location)
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report3)

        //report = Report(intent.getSerializableExtra("UUID") as UUID)
        report = ReportSender.instance.getReport()
        initializeView(report)

        ReportManager.get.setContext(this)
    }

    fun initializeView(data : Report) {
        //set the image
        field_image_3.setImageBitmap(BitmapSender.instance.getBitmap())

        //if there's a message, set the message
        if(data.getMessage() != null)
            field_message_3.setText(data.getMessage())
        
        if(data.getRecipient() != null)
            choose_recipient.setText(data.getRecipient())

        //set the location services to get location updates
        setupLocation()

        //set listeners
        messageListener()
        emailListener()

        //initialize the date and time
        val calendar: Calendar = Calendar.getInstance()
        report.setDate(calendar.time)
        val dateFormat = "EEE, MMM dd hh:mm"
        val dateString = DateFormat.format(dateFormat, report.getDate()).toString()
        date_text.setText(dateString)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.send_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.send_email -> {
                send()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(resultCode == Activity.RESULT_OK) {
            //everything processed correctly
            if(requestCode == SEND_CODE) {
                intent = Intent(this, Report1Activity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun send() {
        val emailList = arrayOf(report.getRecipient())
        val reportText = getReportString()
        photoUri = report.getUri()

        try{

            val intent: Intent = Intent(Intent.ACTION_SEND)
            intent.type = "plain/text"

            if (emailList[0] != null)
                intent.putExtra(Intent.EXTRA_EMAIL, emailList)
            if (reportText != null)
                intent.putExtra(Intent.EXTRA_TEXT, reportText)
            if (photoUri != null) {
                intent.putExtra(Intent.EXTRA_STREAM, photoUri)
            }
            //intent = Intent.createChooser(intent, getString(R.string.send_report))
            startActivityForResult(intent, SEND_CODE)
        } catch (t : Throwable){
            Toast.makeText(this, "Request failed: " + t.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun getReportString(): String? {

        val message = report.getMessage() + "\n"

        val dateFormat = "EEE, MMM dd hh:mm aa z"
        val dateString = DateFormat.format(dateFormat, report.getDate()).toString() + "\n"
        val locationString = getLocationString(true)

        return message + "\n" + dateString + locationString + "\n"
    }

    fun getLocationString(full: Boolean): String {
        val geocoder : Geocoder = Geocoder(this)
        val locationData : List<Address>
        var locationString : String = ""

        if(report.getLocation() != null) {
            val latitude = report.getLocation()!!.latitude
            val longitude = report.getLocation()!!.longitude

            locationData = geocoder.getFromLocation(latitude, longitude, 1)
            //locationString += locationData.get(0).getAddressLine(0) + " : "
            locationString += locationData.get(0).countryName
            if(!full)
                return locationString
            locationString += "\nlon: " + longitude + ", lat: " + latitude + "\n"
        }
        else
            return "No location given.\n\n"
        return locationString
    }

    private fun messageListener() {
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


    fun setupLocation() {
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_REQUEST_CODE)
        }

        try {
            // Request location updates
            locationManager.requestLocationUpdates("gps", 1000L, 0f, locationListener)
        } catch(ex: SecurityException) {
            Log.d("myTag", "Security Exception, no location available")
            Toast.makeText(this, "Location Services Disabled", Toast.LENGTH_LONG).show()
        }

        val location = locationManager.getLastKnownLocation("gps")
        if(location != null) {
            report.setLocation(location)
            location_text.text = getLocationString(false)
        }

    }

}