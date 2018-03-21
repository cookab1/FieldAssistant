package com.example.andyr.fieldassistant

import android.support.v4.app.Fragment


/**
 * Created by cookab1 on 3/21/2018.
 */
class ReportListActivity : SingleFragmentActivity() {
    override protected fun createFragment(): Fragment {
        return ReportListFragment()
    }
}