package com.example.andyr.fieldassistant

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.report2.*
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream

/**
 * Created by andyr on 2/24/2018.
 */
class Report2Activity : AppCompatActivity() {

    lateinit var image: Bitmap
    private val TAKE_PHOTO_REQUEST_CODE = 1
    private val IMAGE_GALLERY_REQUEST_CODE = 2
    private val TYPE_CODE = 0;
    private val DICTATE_CODE = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.report2)

        field_image.setImageBitmap(BitmapSender.instance.getBitmap())

        dictate_message.setOnClickListener { dictate() }
        type_message.setOnClickListener { type() }
        field_image.setOnClickListener{ reSelectImage(intent.extras.getInt("requestCode")) }
    }

    private fun reSelectImage(requestCode: Int) {
        if (requestCode == TAKE_PHOTO_REQUEST_CODE) {
            var intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE)
        } else if (requestCode == IMAGE_GALLERY_REQUEST_CODE) {
            var photoPickerIntent: Intent = Intent(Intent.ACTION_PICK)

            var pictureDirectory: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            var pictureDirectoryPath: String = pictureDirectory.path

            var data: Uri = Uri.parse(pictureDirectoryPath)

            photoPickerIntent.setDataAndType(data, "image/*")

            startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST_CODE)
        }
    }

    private fun dictate() {
        val intent = Intent(this, Report3Activity::class.java)
        intent.putExtra("keyboard_mode", DICTATE_CODE);
        startActivity(intent)
    }

    private fun type() {
        val intent = Intent(this, Report3Activity::class.java)
        intent.putExtra("keyboard_mode", TYPE_CODE);
        startActivity(intent)
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
                    updateImage()
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show()
                }
            } else if (requestCode == TAKE_PHOTO_REQUEST_CODE) {
                image = data!!.getExtras().get("data") as Bitmap
                BitmapSender.instance.setBitmap(image)
                updateImage()
                //field_image.setImageBitmap(image)
            } else {
                val intent = Intent(this, Report3Activity::class.java)
                startActivity(intent)
            }
        }
    }

    fun updateImage() {
        field_image.setImageBitmap(BitmapSender.instance.getBitmap())
    }
}