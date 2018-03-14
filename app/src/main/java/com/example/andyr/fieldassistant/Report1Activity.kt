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
    var report: Report = Report()
    lateinit var image: Bitmap
    private val TAKE_PHOTO_REQUEST_CODE = 1
    private val IMAGE_GALLERY_REQUEST_CODE = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //photoFile = ReportManager.get(this)!!.getPhotoFile(report)

        setContentView(R.layout.report1)

        field_camera.setOnClickListener { takePictureWithCamera() }
        field_photos.setOnClickListener { openPhotos() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(resultCode == Activity.RESULT_OK) {
            //everything processed correctly
            if(requestCode == IMAGE_GALLERY_REQUEST_CODE) {
                //hearing back from the image gallery
                var imageUri: Uri = data!!.data

                var inputStream: InputStream

                try {
                    inputStream = contentResolver.openInputStream(imageUri)
                    //image retrieved
                    image = BitmapFactory.decodeStream(inputStream)
                    BitmapSender.instance.setBitmap(image)

                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show()
                }
            } else if (requestCode == TAKE_PHOTO_REQUEST_CODE) {
                image = data!!.getExtras().get("data") as Bitmap
                BitmapSender.instance.setBitmap(image)

                //field_image.setImageBitmap(image)
            }
        }

        //Use requestCode to identify which action was taken

        intent = Intent(this, Report2Activity::class.java)

        intent.putExtra("requestCode", requestCode)

        startActivity(intent)
    }

    private fun openPhotos() {
        var photoPickerIntent: Intent = Intent(Intent.ACTION_PICK)

        var pictureDirectory: File = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        var pictureDirectoryPath: String = pictureDirectory.path

        var data: Uri = Uri.parse(pictureDirectoryPath)

        photoPickerIntent.setDataAndType(data, "image/*")

        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST_CODE)
    }

    private fun takePictureWithCamera() {
        // create Intent to take a picture and return control to the calling application
        var intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        //photoFile = getPhotoFileUri(report.getImageFileName())

        //val fileProvider : Uri = FileProvider.getUriForFile(this@Report1Activity,
                //"com.example.andyr.fieldassistant", photoFile)
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

        startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE)
    }
}
