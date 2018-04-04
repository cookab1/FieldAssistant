package com.example.andyr.fieldassistant

/**
 * Created by andyr on 3/29/2018.
 */
class ReportSender private constructor(){
    private var report: Report = Report()

    private object Holder { val INSTANCE = ReportSender() }

    companion object {
        val instance: ReportSender by lazy { Holder.INSTANCE }
    }

    fun getReport() : Report {
        return report
    }

    fun setReport(sentReport: Report) {
        report = sentReport
    }
}