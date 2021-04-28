package com.sp.sphtech.utils

import android.graphics.drawable.Drawable
import android.view.ViewManager
import com.sp.sphtech.view.BarPercentView
import com.sp.sphtech.app.SPApplication
import org.jetbrains.anko.custom.ankoView

import java.util.concurrent.atomic.AtomicInteger


inline val Int.dm: Int get() = SPApplication.instance().resources.getDimension(this).toInt()
inline val Int.dc: Int get() = SPApplication.instance().resources.getColor(this)
inline val Int.dd: Drawable get() = SPApplication.instance().resources.getDrawable(this)


//自己定义进度条View 扩展为 dsl 方法
inline fun ViewManager.progressView(
    theme: Int = 0,
    init: BarPercentView.() -> Unit
): BarPercentView = ankoView({
    BarPercentView(
        it
    )
}, theme, init)




