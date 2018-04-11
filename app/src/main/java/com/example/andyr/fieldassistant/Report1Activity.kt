package com.example.andyr.fieldassistant

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory
import android.os.Environment.getExternalStoragePublicDirectory
import android.provider.MediaStore
import android.renderscript.ScriptGroup
import android.support.v4.content.FileProvider
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.report1.*
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat


class Report1Activity : AppCompatActivity() {

    val APP_TAG = "FieldAssistant"
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    var photoFileName = null
    var photoFile: File? = null
    private var report: Report = Report()
    lateinit var image: Bitmap
    private val TAKE_PHOTO_REQUEST_CODE = 1
    private val IMAGE_GALLERY_REQUEST_CODE = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ReportManager.get.setContext(this)
        setContentView(R.layout.report1)

        field_camera.setOnClickListener { takePictureWithCamera() }
        field_photos.setOnClickListener { openPhotos() }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.report_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.group_item -> {
                intent = Intent(this, GroupActivity::class.java)
                startActivity(intent)
            }
            R.id.report_item -> {
                //do nothing
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(resultCode == Activity.RESULT_OK) {
            //everything processed correctly
            if(requestCode == IMAGE_GALLERY_REQUEST_CODE) {
                //hearing back from the image gallery
                var imageUri: Uri = data!!.data
                report.setUri(imageUri)

                var inputStream: InputStream

                try {
                    inputStream = contentResolver.openInputStream(imageUri)
                    //image retrieved
                    image = BitmapFactory.decodeStream(inputStream)

                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show()
                }
            } else if (requestCode == TAKE_PHOTO_REQUEST_CODE) {
                image = BitmapFactory.decodeFile(photoFile!!.absolutePath)
            }
            ReportSender.instance.setReport(report)
            BitmapSender.instance.setBitmap(image)
        }

        //Use requestCode to identify which action was taken

        intent = Intent(this, Report3Activity::class.java)

        ReportManager.get.addReport(report)
        intent.putExtra("UUID", report.getId())

        startActivity(intent)
    }

    private fun openPhotos() {
        var photoPickerIntent: Intent = Intent(Intent.ACTION_PICK)

        var pictureDirectory: File = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        var pictureDirectoryPath: String = pictureDirectory.path

        var photoUri: Uri = Uri.parse(pictureDirectoryPath)

        photoPickerIntent.setDataAndType(photoUri, "image/*")

        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST_CODE)
    }

    private fun takePictureWithCamera() {
        // create Intent to take a picture and return control to the calling application
        val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        photoFile = ReportManager.get.getPhotoFile(report)

        if(photoFile != null) {
            val photoUri: Uri = FileProvider.getUriForFile(this,
                    "com.example.andyr.fieldassistant", photoFile)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            report.setUri(photoUri)
        }
        startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE)
    }
}
