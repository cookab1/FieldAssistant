package com.andy.fieldassistant

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.location.*
import android.media.ExifInterface
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Bundle
import android.preference.PreferenceManager
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
import android.view.View
import android.widget.Switch
import android.widget.Toast
import java.io.IOError
import java.io.IOException
import java.util.*


/**
 * Created by andyr on 2/24/2018.
 */
class Report2Activity : AppCompatActivity() {
    
    private val LOCATION_REQUEST_CODE = 10
    private val TAKE_PHOTO_REQUEST_CODE = 1
    private val IMAGE_GALLERY_REQUEST_CODE = 2

    private val SEND_CODE = 2
    private lateinit var report: Report
    private var photoUri: Uri? = null
    private lateinit var locationManager: LocationManager
    private var locationStyle: Int = 3
    //define the listener
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            if(location_text.text == R.string.location_hint.toString() )
                location_text.text = getLocationString(1)
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

        change_button.setOnClickListener {
            val groups = PreferenceManager.getDefaultSharedPreferences(this)
            if(display_default.visibility == View.VISIBLE) {
                display_default.visibility = View.INVISIBLE
                choose_recipient.visibility = View.VISIBLE
                report.setRecipient(choose_recipient.text.toString())
                change_button.setText(R.string.return_default)
            } else {
                display_default.visibility = View.VISIBLE
                choose_recipient.visibility = View.INVISIBLE
                report.setRecipient(groups.getString("default_recipient","no default set"))
                change_button.setText(R.string.change_recipient)
            }
        }
        location_button.setOnClickListener {
            location_text.text = getLocationString(1)
        }
    }

    fun initializeView(data : Report) {
        //set the image
        if(intent.getIntExtra("image_code", 0) == IMAGE_GALLERY_REQUEST_CODE)
            field_image_3.setImageBitmap(BitmapSender.instance.getBitmap()!!)
        else
            field_image_3.setImageBitmap(rotateImage(BitmapSender.instance.getBitmap()!!))
        val groups = PreferenceManager.getDefaultSharedPreferences(this)

        //if there's a message, set the message
        if(data.getMessage() != null)
            field_message_3.setText(data.getMessage())

        if(data.getRecipient() != null) {
            display_default.visibility = View.INVISIBLE
            choose_recipient.visibility = View.VISIBLE
            choose_recipient.setText(data.getRecipient())
        } else {
            display_default.visibility = View.VISIBLE
            choose_recipient.visibility = View.INVISIBLE
            report.setRecipient(groups.getString("default_recipient","no default set"))
        }
        display_default.setText(groups.getString("default_recipient", "no default set"))


        //initialize the date and time
        val calendar: Calendar = Calendar.getInstance()
        report.setDate(calendar.time)
        date_text.setText(getDateString())

        //set the location services to get location updates
        setupLocation()

        //set listeners
        messageListener()
        emailListener()
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
        val groups = PreferenceManager.getDefaultSharedPreferences(this)
        locationStyle = Integer.parseInt(groups.getString("location_format", "3"))

        val emailList = arrayOf(report.getRecipient())
        val reportText = getReportString()
        var subject: String = ""


        //set subject style based on settings
        subject = groups.getString("default_subject", "Field Assistant")
        photoUri = report.getUri()

        try{

            val intent: Intent = Intent(Intent.ACTION_SEND)
            intent.type = "plain/text"

            if (emailList[0] != null)
                intent.putExtra(Intent.EXTRA_EMAIL, emailList)
                intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            if (reportText != null) 
                intent.putExtra(Intent.EXTRA_TEXT, reportText)
            if (photoUri != null) 
                intent.putExtra(Intent.EXTRA_STREAM, photoUri)
            
            //intent = Intent.createChooser(intent, getString(R.string.send_report))
            startActivityForResult(intent, SEND_CODE)
        } catch (t : Throwable){
            Toast.makeText(this, "Request failed: " + t.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun getReportString(): String? {

        val message = report.getMessage() + "\n"

        val dateString = getDateString() + "\n"
        val locationString = getLocationString(locationStyle)

        return message + "\n" + dateString + locationString + "\n"
    }

    fun getDateString(): String {
        val groups = PreferenceManager.getDefaultSharedPreferences(this)
        val dateFormat: String = groups.getString("date_format", "EEE, MMM dd hh:mm aa z")

        return DateFormat.format(dateFormat, report.getDate()).toString()
    }

    fun getLocationString(style: Int): String {
        val geocoder : Geocoder = Geocoder(this)
        val locationData : List<Address>
        var locationString : String = ""

        if(report.getLocation() != null) {
            val latitude = report.getLocation()!!.latitude
            val longitude = report.getLocation()!!.longitude

            val wifiManager = getApplicationContext().getSystemService(WIFI_SERVICE) as WifiManager

            if(wifiManager.isWifiEnabled) {
                locationData = geocoder.getFromLocation(latitude, longitude, 1)

                if (style == 0) //only country
                    return locationData.get(0).countryName

                //add city, (state), and country
                locationString += locationData.get(0).locality + ",  "
                if (locationData.get(0).adminArea != null) //if there is a state, add it
                    locationString += locationData.get(0).adminArea + " \n"
                locationString += locationData.get(0).countryName

                if (style == 1) //city, state, country
                    return locationString

            }
            else {
                Toast.makeText(this, R.string.no_wifi_text, Toast.LENGTH_LONG).show()
                return "lon: " + longitude + "\n lat: " + latitude + "\n"
            }
            //add lon and lat
            val lonandlat = "lon: " + longitude + ", lat: " + latitude + "\n"

            if(style == 2) //only lon and lat
                return lonandlat

            //add lon and lat
            locationString += "\n" + lonandlat
        }
        else
            return "NO LOCATION\n\n"
        return locationString
    }

    fun setupLocation() {

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission not yet Granted", Toast.LENGTH_LONG).show()
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_REQUEST_CODE)
        }
        try {
            // Request location updates
            locationManager.requestLocationUpdates("gps", 0L, 0f, locationListener)
            setLocationText()
        } catch (ex: SecurityException) {
            Log.d("myTag", "Security Exception, no location available")
            Toast.makeText(this, "Location Services Disabled", Toast.LENGTH_LONG).show()
        }
    }

    fun setLocationText() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission not yet Granted", Toast.LENGTH_LONG).show()
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE)
        }
        val location = locationManager.getLastKnownLocation("gps")
        if (location != null) {
            report.setLocation(location)
            location_text.text = getLocationString(1)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        if(requestCode == LOCATION_REQUEST_CODE){
            if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //accepted
                Toast.makeText(this, "Location Enabled", Toast.LENGTH_LONG).show()

                try {
                    // Request location updates
                    locationManager.requestLocationUpdates("gps", 0L, 0f, locationListener)
                    val location = locationManager.getLastKnownLocation("gps")
                    if(location != null) {
                        report.setLocation(location)
                        location_text.text = getLocationString(1)
                    }
                } catch(ex: SecurityException) {
                    Log.d("myTag", "Security Exception, no location available")
                    Toast.makeText(this, "Location Services Disabled", Toast.LENGTH_LONG).show()
                }
            } else {
                //denied
                Toast.makeText(this, "Location Disabled", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun rotateImage(bitmap: Bitmap): Bitmap {
        val fileLocation = ReportManager.get.getPhotoFile(report).absolutePath
        var exif: ExifInterface? = null
        try {
            exif = ExifInterface(fileLocation)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        val orientation = exif!!.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
        val matrix: Matrix = Matrix()
        when(orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
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
}
