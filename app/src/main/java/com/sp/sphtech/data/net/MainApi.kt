package com.sp.sphtech.data.net

import com.sp.sphtech.data.net.response.AllRecordResponse
import com.sp.sphtech.data.net.response.NetResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

class MainApi : BaseApi() {
    companion object {
        val service by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            var api = MainApi()
            api.getDefaultRetrofit(api.baseUrl, MainService::class.java)
        }
    }
}

interface MainService {
    @GET("api/action/datastore_search")
    suspend fun reflushRecord(
        @Query("resource_id") resId: String,
        @Query("limit") limit: Int
    ): AllRecordResponse

    @GET
    suspend fun loadNextRecord(@Url urlPath: String): AllRecordResponse
}