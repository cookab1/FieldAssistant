package com.example.andyr.fieldassistant

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import com.example.andyr.fieldassistant.reportDatabase.ReportCursorWrapper
import com.example.andyr.fieldassistant.reportDatabase.ReportDatabaseHelper
import com.example.andyr.fieldassistant.reportDatabase.ReportSchema
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by andyr on 3/6/2018.
 */
public class ReportManager private constructor() {

    private var mContext: Context? = null
    private var mDatabase: SQLiteDatabase? = null

    private object Holder { val GET = ReportManager() }

    companion object {
        val get: ReportManager by lazy { Holder.GET }
    }

    fun setContext(context: Context) {
        mContext = context.getApplicationContext()
        mDatabase = ReportDatabaseHelper(mContext).getWritableDatabase()
    }

    fun addReport(report: Report?) {
        val values : ContentValues? = getContentValues(report)
        mDatabase!!.insert(ReportSchema.ReportTable.NAME, null, values)
    }

    fun getReports(): ArrayList<Report> {
        val reports : ArrayList<Report>  = ArrayList<Report>()

        val cursor : ReportCursorWrapper = queryReports(null, null)

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                reports.add(cursor.getReport())
                cursor.moveToNext()
            }
        } finally {
            cursor.close()
        }

        return reports
    }

    fun getReport(id : UUID) : Report? {
        val cursor : ReportCursorWrapper = queryReports(
                ReportSchema.ReportTable.Cols.UUID + " = ?",
                Array<String?>(1) {id.toString()}
        )
        try {
            if(cursor.getCount() == 0)
            return null;

            cursor.moveToFirst()
            return cursor.getReport()
        } finally {
            cursor.close()
        }
    }
    fun getPhotoFile(report : Report?) : File {
        val filesDir : File = mContext!!.getFilesDir()
        return File(filesDir, report!!.getImageFileName())
    }

    fun updateReport(report : Report) {
        var uuidString : String? = report.getId().toString()
        var values : ContentValues = getContentValues(report)
    }

    @SuppressLint("Recycle")
    fun queryReports(whereClause : String?, whereArgs : Array<String?>?) : ReportCursorWrapper {
        val cursor : Cursor? = mDatabase!!.query(
                ReportSchema.ReportTable.NAME,
                null, //null selects all columns
                whereClause, whereArgs,
                null, null, null
        )
        return ReportCursorWrapper(cursor)
    }

    fun getContentValues(report : Report?) : ContentValues {
        val values : ContentValues = ContentValues()
        values.put(ReportSchema.ReportTable.Cols.MESSAGE, report!!.getMessage())
        //this function needs to implement all the rest of the database items.
        return values
    }
}