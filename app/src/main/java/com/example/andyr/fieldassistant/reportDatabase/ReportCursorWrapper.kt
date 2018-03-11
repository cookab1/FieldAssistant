package com.example.andyr.fieldassistant.reportDatabase

import android.database.Cursor
import android.database.CursorWrapper
import android.location.Location
import com.example.andyr.fieldassistant.Group
import com.example.andyr.fieldassistant.Report
import com.example.andyr.fieldassistant.reportDatabase.ReportSchema.ReportTable
import java.util.*


/**
 * Created by andyr on 3/6/2018.
 */
class ReportCursorWrapper(cursor: Cursor?) : CursorWrapper(cursor) {

    fun getReport(): Report {
        val uuidString = getString(getColumnIndex(ReportTable.Cols.UUID))
        val isSent = getInt(getColumnIndex(ReportTable.Cols.SENT))
        val date = getLong(getColumnIndex(ReportTable.Cols.DATE))
        val message = getString(getColumnIndex(ReportTable.Cols.MESSAGE))
        val location = getString(getColumnIndex(ReportTable.Cols.LOCATION))
        val recipient = getString(getColumnIndex(ReportTable.Cols.RECIPIENT))

        val report = Report(UUID.fromString(uuidString))
        report.setDate(Date(date))
        report.setLocation(Location(location))
        report.setMessage(message)
        report.setSent(isSent != 0)
        report.setRecipient(Group(recipient))

        return report
    }
}