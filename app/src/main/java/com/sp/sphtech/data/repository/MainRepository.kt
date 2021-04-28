package com.sp.sphtech.data.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.sp.sphtech.app.SPApplication
import com.sp.sphtech.data.db.AppDatabase
import com.sp.sphtech.data.model.RecordBean
import com.sp.sphtech.data.model.StatisticsBean
import com.sp.sphtech.data.net.BaseApi
import com.sp.sphtech.data.net.MainApi
import com.sp.sphtech.data.net.response.AllRecordResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MainRepository {
    companion object {
        val instance by lazy { MainRepository() }
    }

    /**
     * test from file
     */
    suspend fun getFromFile(): List<RecordBean>? {
        SPApplication.instance().assets.open("test.json").use { inputStream ->
            JsonReader(inputStream.reader()).use { jsonReader ->
                val records = object : TypeToken<AllRecordResponse>() {}.type
                val plantList: AllRecordResponse = Gson().fromJson(jsonReader, records)
                plantList.result?.records?.let {
                    it.forEach { item ->
                        item.year = item.quarter.split("-")[0].toInt()
                    }
                    insertRecord(it)
                    Log.d("data", "insert success ")
                }
                return plantList.result?.records
            }
        }
        return null
    }

    //get from db get 2008~2018
    suspend fun getTotalByYear(): List<RecordBean>? {
        return AppDatabase.getInstance().recordDao().selectByYearData()
    }

    // save to db
    suspend fun insertRecord(records: List<RecordBean>) {
        AppDatabase.getInstance().recordDao().insert(records)
    }

    var allRecordBean: ArrayList<RecordBean>? = null

    // get from net sync all data
    suspend fun getFromNet(nextUrl: String? = null): List<RecordBean>? {
        var response = if (nextUrl == null) {
            allRecordBean?.clear()
            MainApi.service.reflushRecord("a807b7ab-6cad-4aa6-87d0-e283a7353a0f", 30)
        } else {
            MainApi.service.loadNextRecord(nextUrl)
        }
        if (response.success) {
            response.result?.records?.let {
                if (allRecordBean == null) {
                    allRecordBean = ArrayList()
                }
                allRecordBean?.addAll(it)
            }
            if (response.result?.total != allRecordBean?.size) {
                getFromNet(response.result?.get_links()?.next)
            }
        }
        return allRecordBean
    }


}