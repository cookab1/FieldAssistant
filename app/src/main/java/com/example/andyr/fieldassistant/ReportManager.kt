package com.andy.fieldassistant

import android.content.Context
import java.io.File

/**
 * Created by andyr on 3/29/2018.
 */
class ReportManager private constructor(){
    private var report: Report = Report()

    private object Holder { val INSTANCE = ReportManager() }

    companion object {
        val instance: ReportManager by lazy { Holder.INSTANCE }
    }

    fun getReport() : Report {
        return report
    }

    fun setReport(sentReport: Report) {
        report = sentReport
    }

    fun getPhotoFile(context: Context, report : Report) : File {
        val filesDir : File = context.filesDir
        return File(filesDir, report.getImageFileName())
    }
}