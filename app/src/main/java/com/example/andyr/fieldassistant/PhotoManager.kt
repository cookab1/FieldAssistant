package com.example.andyr.fieldassistant

import android.widget.Toast
import android.graphics.BitmapFactory
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Environment
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.View
import android.widget.ImageView
import java.io.File


/**
 * Created by andyr on 3/7/2018.
 */
class PhotoManager {

    val APP_TAG = "FieldAssistant"
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    var photoFileName = "photo.jpg"
    var photoFile: File? = null
/*
    fun onLaunchCamera(view: View) {
        // create Intent to take a picture and return control to the calling application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference to access to future access
        photoFile = getPhotoFileUri(photoFileName)

        // wrap File object into a content provider
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        val fileProvider = FileProvider.getUriForFile(
                this@Report1Activity,
                "com.codepath.fileprovider",
                photoFile
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                val takenImage = BitmapFactory.decodeFile(photoFile)
                // Load the taken image into a preview
                val ivPreview = findViewById(R.id.ivPreview) as ImageView
                ivPreview.setImageBitmap(takenImage)
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
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
*/
}