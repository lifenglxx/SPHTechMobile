package com.sp.sphtech.data

import com.sp.sphtech.data.net.response.ResponseThrowable

abstract class ResultCallBack<T> {
    open fun onSuccess(t: T) {}
    open fun onError(e: ResponseThrowable) {}
    open fun onComplete() {}
}