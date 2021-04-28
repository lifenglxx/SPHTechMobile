package com.sp.sphtech.vm

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.sp.sphtech.app.SPApplication
import com.sp.sphtech.data.model.RecordBean
import com.sp.sphtech.data.repository.MainRepository

class MainViewModel : BaseViewModel() {

    val mListData = MutableLiveData<List<RecordBean>>()


    fun getAllData() {
        getLocal()
        launchGo({
            MainRepository.instance.getFromNet()?.let {
                it.forEach { item ->
                    item.year = item.quarter.split("-")[0].toInt()
                }
                MainRepository.instance.insertRecord(it)
            }
        }, complete = { dismiss();getLocal() }, error = {
            showToas("${it.errMsg} (${it.code})")
        })
    }

    fun getLocal() {
        launchGo({
            MainRepository.instance.getTotalByYear()
        }, success = {
            it?.let { data ->
                mListData.postValue(data)
            }
        }, error = {
            showToas("error ${it.code}:${it.errMsg}")
        }, complete = {

        })
    }

    /**
     * 季度成交量下降警告 状态
     */
    fun needWarn(list: List<RecordBean>): Boolean {
        var load = 0f
        list.forEach {
            if (load > it.volume_of_mobile_data) {
                return true
            }
            load = it.volume_of_mobile_data
        }
        return false
    }

}