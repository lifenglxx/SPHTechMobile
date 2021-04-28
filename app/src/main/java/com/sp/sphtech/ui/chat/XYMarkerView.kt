package com.sp.sphtech.ui.chat

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.sph.sphtech.R
import java.text.DecimalFormat

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
@SuppressLint("ViewConstructor")
class XYMarkerView(
    context: Context?,
    private val xAxisValueFormatter: IAxisValueFormatter?
) : MarkerView(context, R.layout.mark_view) {
    private val tvContent: TextView
    private val format: DecimalFormat

    init {
        tvContent = findViewById(R.id.tvContent)
        format = DecimalFormat("###.0")
    }
    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    override fun refreshContent(
        e: Entry,
        highlight: Highlight
    ) {
        tvContent.text = String.format(
            "%s: %s",
            xAxisValueFormatter?.getFormattedValue(e.x, null),
            format.format(e.y.toDouble())
        )
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }

}