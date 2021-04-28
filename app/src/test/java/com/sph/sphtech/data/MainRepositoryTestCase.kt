package com.sph.sphtech.data

import androidx.test.filters.SmallTest
import com.sp.sphtech.data.model.RecordBean
import com.sp.sphtech.data.repository.MainRepository
import com.sph.sphtech.BaseRobolectricTest
import kotlinx.coroutines.*
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@InternalCoroutinesApi
@SmallTest
class MainRepositoryTestCase : BaseRobolectricTest() {


    /**
     * test Repository db insert android read
     */
    @Test
    @Throws(Exception::class)
    fun testgetFromFile() {
        launch {
            var list: List<RecordBean>? = MainRepository.instance.getFromFile()
            Assert.assertNotNull(list)
            Assert.assertEquals(59, list?.size)

            var data: List<RecordBean>? = MainRepository.instance.getTotalByYear()
            Assert.assertNotNull(data)
            Assert.assertEquals(44, data?.size)
        }
    }

    @Test
    @Throws(Exception::class)
    fun testGetFromNet() {
        launch {
            var data = MainRepository.instance.getFromNet(null)
            Assert.assertNotNull(data)
            Assert.assertEquals(59, data?.size)
        }
    }

}

