package com.sph.sphtech.viewmodel

import com.sp.sphtech.vm.MainViewModel
import com.sph.sphtech.BaseRobolectricTest
import kotlinx.coroutines.*
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class MainRobolectricTestCase : BaseRobolectricTest() {

    /**
     *  test view model load data
     */
    @Test
    @Throws(Exception::class)
    fun testMainViewModelDataLoad() {
        GlobalScope.launch((Dispatchers.Main)) {
            val model = MainViewModel()
            model.getAllData()
            val latch = CountDownLatch(1)
            model.mListData.observeForever {
                latch.countDown()
            }
            withContext(Dispatchers.Unconfined) {
                latch.await(10, TimeUnit.SECONDS)
            }
            Assert.assertNotNull(model.mListData.value)
        }
    }


}