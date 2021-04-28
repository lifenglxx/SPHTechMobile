package com.sph.sphtech.viewmodel

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.sp.sphtech.app.SPApplication
import com.sp.sphtech.data.net.response.AllRecordResponse
import com.sp.sphtech.vm.MainViewModel
import com.sph.sphtech.BaseRobolectricTest
import org.junit.Assert
import org.junit.Test

class MainRobolectricTestCase : BaseRobolectricTest() {

    /**
     *  test view model load data
     */
    @Test
    @Throws(Exception::class)
    fun testMainViewModelDataLoad() {
        val model = MainViewModel()
        model.mListData.observeForever {
            model.mListData.value?.forEach {
                println(it.quarter)
            }
        }
        SPApplication.instance().assets.open("test.json").use { inputStream ->
            JsonReader(inputStream.reader()).use { jsonReader ->
                val records = object : TypeToken<AllRecordResponse>() {}.type
                val plantList: AllRecordResponse = Gson().fromJson(jsonReader, records)
                plantList.result?.records?.let {
                    it.forEach { item ->
                        item.year = item.quarter.split("-")[0].toInt()
                    }
                    model.mListData.value = it
                }
            }
        }

        Assert.assertNotNull(model.mListData.value)
    }


}