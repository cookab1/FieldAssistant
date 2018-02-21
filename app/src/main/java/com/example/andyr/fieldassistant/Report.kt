package com.example.andyr.fieldassistant

import android.media.Image
import java.util.UUID;
import java.util.Date;

/**
 * Created by cookab1 on 2/21/2018.
 */
class Report {
    private val mId: UUID = UUID.randomUUID()
    private val mTitle: String = ""
    private val mDate: Date = Date()
    private val mSent: Boolean = false
    private val mRecipient: String = ""
    //private val mImage: Image
    private val mMessage: String = ""
}