package com.example.andyr.fieldassistant

import android.content.ClipData
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.FileProvider.getUriForFile
import kotlinx.android.synthetic.main.report1.*
import java.io.File

class Report1Activity : AppCompatActivity() {

    private val TAKE_PHOTO_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report1)
        field_camera.setOnClickListener { takePictureWithCamera() }
        field_photos.setOnClickListener { openPhotos() }
    }
    /* //This may need to be put in Fragment
    fun newIntent() {
        var intent = Intent(this, ::class.java)
        startActivity(intent)
        finish()
    }
    */

    private fun openPhotos() {
        val intent = Intent(this, Report2Activity::class.java)
        startActivity(intent)
    }

    private fun takePictureWithCamera() {
        val intent = Intent(this, Report2Activity::class.java)
        startActivity(intent)

        /*
        // 1
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // 2
        val imagePath = File(filesDir, "images")
        val newFile = File(imagePath, "default_image.jpg")
        if (newFile.exists()) {
            newFile.delete()
        } else {
            newFile.parentFile.mkdirs()
        }
        val selectedPhotoPath = getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", newFile)

        // 3
        captureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, selectedPhotoPath)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        } else {
            val clip = ClipData.newUri(contentResolver, "A photo", selectedPhotoPath)
            captureIntent.clipData = clip
            captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
        startActivityForResult(captureIntent, TAKE_PHOTO_REQUEST_CODE)
        */
    }
}
