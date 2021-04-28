package com.sp.sphtech.app

import android.app.Application
import kotlin.properties.Delegates

class SPApplication : Application() {

    override fun onCreate() {
        instance = this
        super.onCreate()

    }

    //单例化：利用系统自带的Delegates生成委托属性
    companion object {
        var instance: Application by Delegates.notNull()
        fun instance() = instance
    }

}