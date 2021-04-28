package com.sp.sphtech.data.net.response

data class NetResult<T>(val code: String, val msg: String, val data: T)