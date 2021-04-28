package com.sp.sphtech.vm

import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sp.sphtech.app.SPApplication
import com.sp.sphtech.data.net.ERROR
import com.sp.sphtech.data.net.ExceptionHandle
import com.sp.sphtech.data.net.response.ResponseThrowable
import com.sp.sphtech.utils.EspressoIdlingResource
import com.sph.sphtech.BuildConfig
import kotlinx.coroutines.*

open class BaseViewModel : ViewModel() {

    fun launchUI(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch {
        block()
    }

    fun <T> launchGo(
        block: suspend CoroutineScope.() -> T,
        success: (T) -> Unit = {},
        complete: () -> Unit = {},
        error: (ResponseThrowable) -> Unit = {
            Log.e("net error 1", "${it.code}:${it.errMsg}")
        }
    ) {
        launchUI {
            try {
                if (BuildConfig.DEBUG) {
                    EspressoIdlingResource.increment()
                }
                var t = withContext(Dispatchers.IO) {
                    block()
                }
                if (t != null) {
                    success(t)
                } else {
                    throw ResponseThrowable(ERROR.DATA_ERROR)
                }
            } catch (e: Throwable) {
                if (e.message?.contains("Job was cancelled") == true) {
                    Log.e("job", "Job was cancelled")
                    return@launchUI
                }
                error(ExceptionHandle.handleException(e))
            } finally {
                complete()
            }
            if (BuildConfig.DEBUG) {
                withTimeout(500) {
                    EspressoIdlingResource.decrement()
                }
            }
        }
    }


    fun showToas(msg: String) {
        GlobalScope.launch (Dispatchers.Main){
            Toast.makeText(SPApplication.instance, msg, Toast.LENGTH_SHORT).show()
        }
    }

    var waitForDialog: ProgressDialog? = null
    fun showLoading(context: Context) {
        waitForDialog = ProgressDialog(context)
        waitForDialog?.setOnDismissListener {
            waitForDialog = null
        }
        waitForDialog?.setMessage("Synchronous data")
        waitForDialog?.show()
    }

    fun dismiss() {
        waitForDialog?.dismiss()
    }


    public override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}