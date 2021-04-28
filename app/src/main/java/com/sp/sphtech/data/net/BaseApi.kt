package com.sp.sphtech.data.net

import com.sph.sphtech.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

open class BaseApi {
    private val READ_TIMEOUT_DEFAULT = 30L
    private val CONNECT_TIMEOUT_DEFAULT = 30L
    private var okHttpBuild: OkHttpClient.Builder? = null
    val baseUrl = "https://data.gov.sg/"

    private fun getOkHttpClient(): OkHttpClient.Builder {
        return (okHttpBuild ?: getOkHttpClientBuilder().apply { okHttpBuild = this })
    }

    private fun getOkHttpClientBuilder(): OkHttpClient.Builder {
        var build = OkHttpClient.Builder().readTimeout(
            READ_TIMEOUT_DEFAULT, TimeUnit.SECONDS
        ).connectTimeout(
            CONNECT_TIMEOUT_DEFAULT, TimeUnit.SECONDS
        )
        if(BuildConfig.DEBUG){
            build.addInterceptor(LogInterceptor())
        }
        return build
    }

    fun <T> getDefaultRetrofit(baseUrl: String, service: Class<T>): T {
        val retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(NullOnEmptyConverterFactory())
            //格式转换
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClient().build())
            .build()
        return retrofit.create(service)
    }
}

class NullOnEmptyConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val delegate: Converter<ResponseBody, *> =
            retrofit.nextResponseBodyConverter<Any>(this, type, annotations)
        return Converter<ResponseBody, Any?> { body ->
            if (body.contentLength() == 0L) null else delegate.convert(
                body
            )
        }
    }
}