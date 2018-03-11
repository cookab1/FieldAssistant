package com.example.andyr.fieldassistant

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import kotlinx.android.synthetic.main.report1.*
import java.io.File


class Report1Activity : AppCompatActivity() {

    val APP_TAG = "FieldAssistant"
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    var photoFileName = null
    var photoFile: File? = null
    var report: Report = Report()
    private val TAKE_PHOTO_REQUEST_CODE = 1
    private val REQUEST_IMAGE_CAPTURE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //photoFile = ReportManager.get(this)!!.getPhotoFile(report)


        setContentView(R.layout.report1)
        field_camera.setOnClickListener { takePictureWithCamera() }
        field_photos.setOnClickListener { openPhotos() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        /*      This code will be useful when displaying the image. but not now.
        super.onActivityResult(requestCode, resultCode, data)
        val bitmap = data!!.getExtras().get("data") as Bitmap
        //image needs to be saved somehow
        field_image.setImageBitmap(bitmap)
        */
        intent = Intent(this, Report2Activity::class.java)
        startActivity(intent)
    }

    private fun openPhotos() {

    }

    private fun takePictureWithCamera() {
        // create Intent to take a picture and return control to the calling application
        var intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        //photoFile = getPhotoFileUri(report.getImageFileName())

        //val fileProvider : Uri = FileProvider.getUriForFile(this@Report1Activity,
                //"com.example.andyr.fieldassistant", photoFile)
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

        startActivityForResult(intent, 2)
    }
}
