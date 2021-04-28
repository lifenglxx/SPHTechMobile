package com.sp.sphtech.ui.adapter

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.sp.sphtech.data.model.StatisticsBean
import com.sp.sphtech.utils.dc
import com.sp.sphtech.utils.dd
import com.sp.sphtech.utils.dm
import com.sp.sphtech.utils.progressView
import com.sp.sphtech.view.BarPercentView
import com.sph.sphtech.R
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.constraint.layout.constraintLayout
import org.jetbrains.anko.textColor
import org.jetbrains.anko.textView
import java.math.BigDecimal
import java.math.RoundingMode

class YearAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var mData = Array(11) { i -> (2008 + i) }
    var mMap = HashMap<Int, StatisticsBean>()
    var max = 0f
    var allCount = R.dimen.margin_150.dm
    var warn = R.mipmap.exclamation.dd
    var clickListener: View.OnClickListener? = null

    init {
        warn.setBounds(0, 0, warn.intrinsicWidth, warn.intrinsicHeight)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var ui = UI()
        var view = ui.createView(AnkoContext.Companion.create(parent.context, parent))
        return Holder(view, ui)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is Holder) {
            holder.bindData(mData[position])
        }
    }

    inner class Holder(root: View, var ui: UI) : RecyclerView.ViewHolder(root) {
        fun bindData(year: Int) {
            with(ui) {
                tvYear.text = "year: $year"
                root.setOnClickListener(null)
                if (max > 0 && mMap.containsKey(year)) {
                    mMap[year]?.run {//通过年得到对应年份4个季度的销量
                        //以所有年份最高销量为基准，显示每年销量点比
                        ivProcess.setPercentage((count.div(max) * allCount).div(allCount) * 100)
                        if (hasWarn) {
                            tvYear.setCompoundDrawables(null, null, warn, null)
                        } else {
                            tvYear.setCompoundDrawables(null, null, null, null)
                        }
                        total.text = BigDecimal(count.toString()).setScale(3, RoundingMode.CEILING).toString()
                        root.setOnClickListener(clickListener)
                        root.setTag(R.id.list, year)
                    }
                } else {
                    ivProcess.setPercentage(0f)
                    tvYear.setCompoundDrawables(null, null, null, null)
                }
            }
        }
    }

    /**
     * view layout dsl
     */
    class UI : AnkoComponent<ViewGroup> {

        lateinit var root: ConstraintLayout
        lateinit var tvYear: TextView
        lateinit var ivProcess: BarPercentView
        lateinit var total: TextView
        override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
            root = constraintLayout {
                tvYear = textView {
                    textSize = 16f
                    textColor = R.color.c333333.dc
                    gravity = Gravity.CENTER_VERTICAL
                    compoundDrawablePadding = R.dimen.margin_5.dm
                }.lparams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                ) {
                    topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                    bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                    startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                    marginStart = R.dimen.margin_10.dm
                }
                ivProcess = progressView {
                    id = (Math.random() * 1000).toInt()
                }.lparams(//自定义view
                    R.dimen.margin_150.dm,
                    R.dimen.margin_10.dm
                ) {
                    topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                    bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                    marginEnd = R.dimen.margin_10.dm
                    endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                }
                total = textView {
                    textSize = 11f
                }.lparams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                ) {
                    topMargin = R.dimen.margin_5.dm
                    topToBottom = ivProcess.id
                    startToStart = ivProcess.id
                }
            }
            root.layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                R.dimen.margin_60.dm
            )
            root
        }

    }

}