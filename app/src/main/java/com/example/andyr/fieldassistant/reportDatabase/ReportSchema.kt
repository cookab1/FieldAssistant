package com.andy.fieldassistant.reportDatabase

/**
 * Created by andyr on 3/6/2018.
 */
class ReportSchema {
    object ReportTable {
        val NAME = "reports"

        object Cols {
            val UUID = "uuid"
            val LOCATION = "location"
            val DATE = "date"
            val SENT = "sent"
            val MESSAGE = "message"
            val RECIPIENT = "recipient"
        }
    }
}