package com.example.andyr.fieldassistant

import android.media.Image
import java.util.UUID;
import java.util.Date;

/**
 * Created by cookab1 on 2/21/2018.
 */
class Report {
    private var mId: UUID? = UUID.randomUUID()
    private var mDate: Date? = null
    private var mSent: Boolean = false
    private var mRecipient: String? = null
    private var mImage: String? = null
    private var mMessage: String? = null

    fun setId(id: UUID?) {
        mId = id
    }
    fun getId(): UUID? {
        return mId
    }

    fun setDate(date: Date?) {
        mDate = date
    }
    fun getDate(): Date? {
        return mDate
    }

    fun setRecipient(recipient: String?) {
        mRecipient = recipient
    }
    fun getRecipient(): String? {
        return mRecipient
    }

    fun setImage(image: String?) {
        mImage = image
    }
    fun getImage(): String? {
        return mImage
    }

    fun setMessage(message: String?) {
        mMessage = message
    }
    fun getMessage(): String? {
        return mMessage
    }

    fun notSent() {
        mSent = false
    }
    fun sent() {
        mSent = true
    }
    fun ifSent(): Boolean {
        return mSent
    }

}