package com.example.andyr.fieldassistant

import android.location.Location
import android.net.Uri
import java.util.UUID;
import java.util.Date;

/**
 * Created by cookab1 on 2/21/2018.
 */
class Report {
    private var mId: UUID
    private var mDate: Date? = null
    private var mSent: Boolean = false
    private var mLocation: Location? = null
    private var mRecipient: Group? = null
    private var mSingleRecipient: String? = null
    private var mImage: String? = null
    private var mUri: Uri? = null
    private var mMessage: String? = null

    constructor() {
        mId = UUID.randomUUID()
    }

    constructor(uuid: UUID) {
        mId = uuid
    }

    fun setId(id: UUID) {
        mId = id
    }
    fun getId(): UUID {
        return mId
    }

    fun setDate(date: Date?) {
        mDate = date
    }
    fun getDate(): Date? {
        return mDate
    }

    fun setRecipient(single: String) {
        mSingleRecipient = single
    }

    fun setRecipient(recipient: Group?) {
        mRecipient = recipient
    }
    fun getRecipient(): String? {
        return mSingleRecipient
    }

    fun getImageFileName(): String {
        return "IMG_" + getId().toString() + ".jpg"
    }

    fun setUri(uri: Uri) {
        mUri = uri
    }

    fun getUri() : Uri? {
        return mUri
    }

    fun setMessage(message: String) {
        mMessage = message
    }
    fun getMessage(): String? {
        return mMessage
    }

    fun setLocation(location: Location?) {
        mLocation = location
    }

    fun getLocation() : Location? {
        return mLocation
    }

    fun setSent(sent: Boolean) {
        mSent = sent
    }

    fun ifSent(): Boolean {
        return mSent
    }

}