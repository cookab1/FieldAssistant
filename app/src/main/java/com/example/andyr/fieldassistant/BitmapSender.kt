package com.andy.fieldassistant

import android.graphics.Bitmap

/**
 * Created by andyr on 3/14/2018.
 */
public class BitmapSender private constructor () {
    private var bitmap: Bitmap? = null

    private object Holder { val INSTANCE = BitmapSender() }

    companion object {
        val instance: BitmapSender by lazy { Holder.INSTANCE }
    }

    fun getBitmap() : Bitmap? {
        return bitmap
    }

    fun setBitmap(sentBitmap: Bitmap?) {
        bitmap = sentBitmap
    }
}