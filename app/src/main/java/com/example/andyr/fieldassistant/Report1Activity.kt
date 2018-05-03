package com.andy.fieldassistant

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.report1.*
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream


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
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(resultCode == Activity.RESULT_OK) {
            //everything processed correctly
            if(requestCode == IMAGE_GALLERY_REQUEST_CODE) {
                //hearing back from the image gallery
                val imageUri: Uri = data!!.data
                report.setUri(imageUri)
                val inputStream: InputStream

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
            ReportManager.instance.setReport(report)
            BitmapSender.instance.setBitmap(image)
        }

        //Use requestCode to identify which action was taken

        intent = Intent(this, Report2Activity::class.java)

        intent.putExtra("image_code", requestCode)

        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent)
    }

    private fun openPhotos() {
        val photoPickerIntent: Intent = Intent(Intent.ACTION_PICK)

        val pictureDirectory: File = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val pictureDirectoryPath: String = pictureDirectory.path

        val photoUri: Uri = Uri.parse(pictureDirectoryPath)
        report.setUri(photoUri)

        photoPickerIntent.setDataAndType(photoUri, "image/*")

        photoPickerIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST_CODE)
    }

    private fun takePictureWithCamera() {
        // create Intent to take a picture and return control to the calling application
        val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        photoFile = ReportManager.instance.getPhotoFile(this, report)

        if(photoFile != null) {
            val photoUri: Uri = FileProvider.getUriForFile(this,
                    "com.andy.fieldassistant", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE)
    }

    //lifecycle overrides
    override fun onPause() {
        super.onPause()
        overridePendingTransition(0,0)
    }
}
