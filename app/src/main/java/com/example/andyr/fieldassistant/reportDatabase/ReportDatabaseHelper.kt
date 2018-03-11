package com.example.andyr.fieldassistant.reportDatabase

import android.database.sqlite.SQLiteDatabase
import com.example.andyr.fieldassistant.reportDatabase.ReportSchema.ReportTable
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;


/**
* Created by andyr on 3/6/2018.
*/
class ReportDatabaseHelper(context: Context?) : SQLiteOpenHelper(
        context,
        "reportBase.db",
        null,
        1
) {


    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("create table " + ReportTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                ReportTable.Cols.UUID + ", " +
                ReportTable.Cols.LOCATION + ", " +
                ReportTable.Cols.DATE + ", " +
                ReportTable.Cols.SENT + ", " +
                ReportTable.Cols.MESSAGE + ", " +
                ReportTable.Cols.RECIPIENT + ")"
        )
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase?, i: Int, i1: Int) {

    }
}