package com.sp.sphtech.data.net

import android.net.ParseException
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import com.sp.sphtech.data.net.response.ResponseThrowable
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException

/**
 *   @auther : Aleyn
 *   time   : 2019/08/12
 */
object ExceptionHandle {

    fun handleException(e: Throwable): ResponseThrowable {
        e.printStackTrace()
        val ex: ResponseThrowable
        if (e is ResponseThrowable) {
            ex = e
        } else if (e is HttpException) {
            ex = ResponseThrowable(ERROR.HTTP_ERROR, e)
        } else if (e is JsonParseException
            || e is JSONException
            || e is ParseException || e is MalformedJsonException
        ) {
            ex = ResponseThrowable(ERROR.PARSE_ERROR, e)
        } else if (e is ConnectException) {
            ex = ResponseThrowable(ERROR.NETWORD_ERROR, e)
        } else if (e is javax.net.ssl.SSLException) {
            ex = ResponseThrowable(ERROR.SSL_ERROR, e)
        } else if (e is java.net.SocketTimeoutException) {
            ex = ResponseThrowable(ERROR.TIMEOUT_ERROR, e)
        } else if (e is java.net.UnknownHostException) {
            ex = ResponseThrowable(ERROR.TIMEOUT_ERROR, e)
        } else {
            ex = if (!e.message.isNullOrEmpty()) ResponseThrowable(1000, e.message!!, e)
            else ResponseThrowable(ERROR.UNKNOWN, e)
        }
        return ex
    }
}