package com.sp.sphtech.ui.activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.gyf.immersionbar.ImmersionBar

open class BaseActivity : AppCompatActivity(), LifecycleOwner {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        ImmersionBar.with(this).barAlpha(0.0f).statusBarDarkFont(true).init()
    }

}