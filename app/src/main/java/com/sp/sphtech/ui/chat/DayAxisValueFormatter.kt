package com.sp.sphtech.ui.chat

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter

class DayAxisValueFormatter : IAxisValueFormatter {
    private val quarter = arrayOf("Q1", "Q2", "Q3", "Q4")

    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        return quarter[((value - 1) % quarter.size).toInt()]
    }

}