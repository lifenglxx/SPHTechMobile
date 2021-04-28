package com.sph.sphtech

import android.app.Application
import android.content.Context
import com.sp.sphtech.app.SPApplication
import com.sp.sphtech.data.db.AppDatabase
import com.sp.sphtech.data.repository.MainRepository
import com.sph.sphtech.utils.MainCoroutineRule
import com.sph.sphtech.utils.runBlockingTest
import kotlinx.coroutines.*
import org.jetbrains.anko.doAsyncResult
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.modules.junit4.rule.PowerMockRule
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@RunWith(RobolectricTestRunner::class)
@Config(
    shadows = [ShadowLog::class],
    sdk = [26],
    application = SPApplication::class
) //,
//        manifest = "app/build/path/AndroidManifest.xml",
//        assetDir = "some/build/path/assetDir",
//        resourceDir = "some/build/path/resourceDir"
@PowerMockIgnore(
    "org.mockito.*",
    "org.robolectric.*",
    "android.*",
    "org.json.*",
    "sun.security.*",
    "javax.net.*"
)
abstract class BaseRobolectricTest {
    var run = MainCoroutineRule()

    @get:Rule
    val rule = RuleChain.outerRule(run)

    @Before
    fun setUp() {
        ShadowLog.stream = System.out
        if (!hasInited) {
            init()
            hasInited = true
        }
        MockitoAnnotations.initMocks(this)
    }

    val application: Application
        get() = RuntimeEnvironment.application

    val context: Context
        get() = RuntimeEnvironment.application

    private fun init() {}

    companion object {
        private var hasInited = false
    }

    fun launch(block: suspend () -> Unit) {
        val latch = CountDownLatch(1)
        GlobalScope.launch(run.testDispatcher) {
            block()
            latch.countDown()
        }
        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw TimeoutException("data error")
        }
        AppDatabase.getInstance().close()
    }
}