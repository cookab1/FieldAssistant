package com.example.andyr.fieldassistant

import android.content.ClipData
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.support.v4.content.FileProvider.getUriForFile
import android.util.Log
import kotlinx.android.synthetic.main.report1.*
import java.io.File
import android.R.attr.data
import android.support.v4.app.NotificationCompat.getExtras
import kotlinx.android.synthetic.main.report2.*


class Report1Activity : AppCompatActivity() {

    val APP_TAG = "FieldAssistant"
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    var photoFileName = null
    var photoFile: File? = null
    //var report: Report = Report()
    private val TAKE_PHOTO_REQUEST_CODE = 1
    private val REQUEST_IMAGE_CAPTURE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report1)
        field_camera.setOnClickListener { takePictureWithCamera() }
        field_photos.setOnClickListener { openPhotos() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val bitmap = data!!.getExtras().get("data") as Bitmap
        //image needs to be saved somehow
        field_image.setImageBitmap(bitmap)
    }

    private fun openPhotos() {
        val intent = Intent(this, Report2Activity::class.java)
        startActivity(intent)
    }

    private fun takePictureWithCamera() {
        
        // create Intent to take a picture and return control to the calling application
        var intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 1)
        
        
        intent = Intent(this, Report2Activity::class.java)
        startActivity(intent)
    }

    fun getPhotoFileUri(fileName: String): File? {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            val mediaStorageDir = File(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG)

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
                Log.d(APP_TAG, "failed to create directory")
            }

            // Return the file target for the photo based on filename

            return File(mediaStorageDir.getPath() + File.separator + fileName)
        }
        return null
    }

    // Returns true if external storage for photos is available
    private fun isExternalStorageAvailable(): Boolean {
        val state = Environment.getExternalStorageState()
        return state == Environment.MEDIA_MOUNTED
    }
}
