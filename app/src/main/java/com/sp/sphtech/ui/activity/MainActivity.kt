package com.sp.sphtech.ui.activity

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.Fill
import com.sp.sphtech.data.model.RecordBean
import com.sp.sphtech.data.model.StatisticsBean
import com.sp.sphtech.ui.adapter.YearAdapter
import com.sp.sphtech.ui.chat.DayAxisValueFormatter
import com.sp.sphtech.ui.chat.MyAxisValueFormatter
import com.sp.sphtech.ui.chat.XYMarkerView
import com.sp.sphtech.utils.RecycleViewDivider
import com.sp.sphtech.utils.dc
import com.sp.sphtech.vm.MainViewModel
import com.sph.sphtech.R
import com.sph.sphtech.databinding.ActivityMainBinding
import com.sph.sphtech.databinding.DialogChatBinding

class MainActivity : BaseActivity() {
    var mBinding: ActivityMainBinding? = null
    var mViewModel: MainViewModel? = null
    var mAdapter = YearAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        setList()

        mViewModel?.mListData?.observe(this,
            Observer<List<RecordBean>> {
                var max = 0f
                mAdapter.mMap.clear()
                it.forEach { item ->
                    var bean = mAdapter.mMap[item.year]
                    if (bean == null) {
                        bean = StatisticsBean()
                        mAdapter.mMap[item.year] = bean
                    }
                    bean.count += item.volume_of_mobile_data
                    bean.list.add(item)
                }
                mAdapter.mMap.forEach { kv ->
                    if (kv.value.count > max) {
                        max = kv.value.count
                    }
                    kv.value.year = kv.key
                    kv.value.list.sortBy { i -> i.quarter }
                    kv.value.hasWarn = mViewModel?.needWarn(kv.value.list) ?: false
                }
                mAdapter.max = max
                mAdapter.notifyDataSetChanged()
            })
        mViewModel?.showLoading(this)
        mViewModel?.getAllData()
    }

    fun setList() {
        mBinding?.run {
            list.layoutManager = LinearLayoutManager(this@MainActivity)
            list.addItemDecoration(
                RecycleViewDivider(
                    this@MainActivity,
                    LinearLayoutManager.VERTICAL
                )
            )
            list.adapter = mAdapter
            mAdapter.clickListener = View.OnClickListener {
                var year = it.getTag(R.id.list) as Int
                mAdapter.mMap[year]?.let { item ->
                    showChatDialog(item)
                }
            }
        }
    }

    var chatDialog: Dialog? = null
    fun showChatDialog(bean: StatisticsBean) {
        if (chatDialog != null) {
            return
        }
        var binding = DialogChatBinding.inflate(LayoutInflater.from(this), null, false)
        chatDialog = Dialog(this, R.style.dialog)
        binding.close.setOnClickListener {
            chatDialog?.dismiss()
        }
        chatDialog?.run {
            val lp = window?.attributes
            setContentView(binding.root)
            show()
            setOnDismissListener {
                chatDialog = null
            }
            setCancelable(true)
            val height = (0.5 * context.resources.displayMetrics.heightPixels).toInt()
            lp?.height = height
            val width = (0.8 * context.resources.displayMetrics.widthPixels).toInt()
            lp?.width = width
            window?.setGravity(Gravity.CENTER)
            window?.attributes = lp
            setView(binding, bean)
        }
    }

    fun setView(binding: DialogChatBinding, bean: StatisticsBean) {
        binding.run {
            chart.setDrawBarShadow(false)
            chart.setDrawValueAboveBar(true)
            chart.description.isEnabled = false
            // if more than 60 entries are displayed in the chart, no values will be
            // drawn
            chart.setMaxVisibleValueCount(4)
            // scaling can now only be done on x- and y-axis separately
            chart.setPinchZoom(false)
            chart.setDrawGridBackground(false)
            // chart.setDrawYLabels(false);
            val xAxisFormatter = DayAxisValueFormatter()

            val xAxis = chart.xAxis
            xAxis.position = XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            xAxis.granularity = 1f // only intervals of 1 day
            xAxis.labelCount = 7
            xAxis.valueFormatter = xAxisFormatter

            val custom: IAxisValueFormatter = MyAxisValueFormatter()
            val leftAxis = chart.axisLeft
            leftAxis.setLabelCount(7, false)
            leftAxis.valueFormatter = custom
            leftAxis.setPosition(YAxisLabelPosition.OUTSIDE_CHART)
            leftAxis.spaceTop = 15f
            leftAxis.axisMinimum = 0f


            val rightAxis = chart.axisRight
            rightAxis.setLabelCount(7, false)
            rightAxis.valueFormatter = custom
            rightAxis.spaceTop = 15f
            rightAxis.axisMinimum = 0f // this replaces setStartAtZero(true)


            val l = chart.legend
            l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
            l.orientation = Legend.LegendOrientation.HORIZONTAL
            l.setDrawInside(false)
            l.form = LegendForm.SQUARE
            l.formSize = 9f
            l.textSize = 11f
            l.xEntrySpace = 4f

            val mv = XYMarkerView(this@MainActivity, xAxisFormatter)
            mv.chartView = chart // For bounds control
            chart.marker = mv // Set the marker to the chart

            val values = ArrayList<BarEntry>()
            bean.list.forEachIndexed {i,v->
                values.add(BarEntry((i+1).toFloat(), v.volume_of_mobile_data))
            }
            val set1: BarDataSet
            if (chart.data != null && chart.data.dataSetCount > 0) {
                set1 = chart.data.getDataSetByIndex(0) as BarDataSet
                set1.values = values
                chart.data.notifyDataChanged()
                chart.notifyDataSetChanged()
            } else {
                set1 = BarDataSet(values, "The year ${bean.year}")
                set1.setDrawIcons(false)
                val startColor1 = android.R.color.holo_red_dark.dc
                val endColor1 = android.R.color.holo_orange_light.dc
                val gradientFills: MutableList<Fill> = java.util.ArrayList()
                gradientFills.add(Fill(startColor1, endColor1))
                set1.fills = gradientFills
                val dataSets = java.util.ArrayList<IBarDataSet>()
                dataSets.add(set1)
                val data = BarData(dataSets)
                data.setValueTextSize(10f)
                data.barWidth = 0.7f
                chart.data = data
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}