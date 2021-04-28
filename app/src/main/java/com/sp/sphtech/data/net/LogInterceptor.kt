package com.sp.sphtech.data.net

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset

/**
 * @author Ahser
 * @date: 2020-10-31
 */
class LogInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val charset = Charset.forName("UTF-8")
        val buffer = Buffer()
        val request = chain.request()
        val headers = request.headers
        val responseHeadersLength = headers.size
        Log.i(
            TAG,
            "╔════════════════════════════════════════════════════════════════════════════════════════"
        )
        Log.i(
            TAG,
            String.format("║ 请求地址 %s", request.url)
        )
        Log.i(
            TAG,
            String.format("║ 请求方式 %s", request.method)
        )
        Log.i(
            TAG,
            "╟────────────────────────────────────────────────────────────────────────────────────────"
        )
        if(responseHeadersLength>0){
            for (i in 0 until responseHeadersLength) {
                val headerName = headers.name(i)
                val headerValue = headers[headerName]
                Log.i(
                    TAG,
                    String.format("║ 请求头: Key: %s Value: %s", headerName, headerValue)
                )
            }
            Log.i(
                TAG,
                "╚════════════════════════════════════════════════════════════════════════════════════════"
            )
        }
        val requestBody = request.body
        val response = chain.proceed(request)
        val responseBody = response.body
        val source = responseBody!!.source()
        Log.i(
            TAG,
            "B:╔════════════════════════════════════════════════════════════════════════════════════════"
        )
        Log.i(
            TAG,
            String.format("B:║ 请求地址 %s", request.url)
        )
        Log.i(
            TAG,
            String.format("B:║ 请求方式 %s", request.method)
        )
        Log.i(
            TAG,
            "B:╟────────────────────────────────────────────────────────────────────────────────────────"
        )
        if (requestBody != null) {
            requestBody.writeTo(buffer)
            Log.i(
                TAG,
                String.format("Back:║ 请求参数 %s", buffer.readString(charset))
            )
            Log.i(
                TAG,
                "B:╟────────────────────────────────────────────────────────────────────────────────────────"
            )
        }
        source.request(Long.MAX_VALUE) // Buffer the entire body.
        val bufferS = source.buffer()
        val contentType = responseBody.contentType()
        if (contentType != null) {
            val json = bufferS.clone().readString(contentType.charset(charset)!!)
            Log.i(TAG, "B:║ 返回数据")
            val con =
                formatJson(json).split("\n").toTypedArray()
            for (line in con) {
                Log.i(TAG, "B:║$line")
            }
        }
        Log.i(
            TAG,
            "B:╚════════════════════════════════════════════════════════════════════════════════════════"
        )
        return response
    }

    companion object {
        const val TAG = "API_LOG"

        /**
         * 格式化
         *
         * @param jsonStr
         * @return
         */
        fun formatJson(jsonStr: String?): String {
            if (null == jsonStr || "" == jsonStr) return ""
            val sb = StringBuilder()
            var last = '\u0000'
            var current = '\u0000'
            var indent = 0
            var isInQuotationMarks = false
            for (i in 0 until jsonStr.length) {
                last = current
                current = jsonStr[i]
                when (current) {
                    '"' -> {
                        if (last != '\\') {
                            isInQuotationMarks = !isInQuotationMarks
                        }
                        sb.append(current)
                    }
                    '{', '[' -> {
                        sb.append(current)
                        if (!isInQuotationMarks) {
                            sb.append('\n')
                            indent++
                            addIndentBlank(sb, indent)
                        }
                    }
                    '}', ']' -> {
                        if (!isInQuotationMarks) {
                            sb.append('\n')
                            indent--
                            addIndentBlank(sb, indent)
                        }
                        sb.append(current)
                    }
                    ',' -> {
                        sb.append(current)
                        if (last != '\\' && !isInQuotationMarks) {
                            sb.append('\n')
                            addIndentBlank(sb, indent)
                        }
                    }
                    else -> sb.append(current)
                }
            }
            return sb.toString()
        }

        /**
         * 添加space
         *
         * @param sb
         * @param indent
         */
        private fun addIndentBlank(sb: StringBuilder, indent: Int) {
            for (i in 0 until indent) {
                sb.append('\t')
            }
        }
    }
}