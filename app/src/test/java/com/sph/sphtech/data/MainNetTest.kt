package com.sph.sphtech.data

import androidx.test.filters.SmallTest
import com.sp.sphtech.data.db.AppDatabase
import com.sp.sphtech.data.model.RecordBean
import com.sp.sphtech.data.net.MainApi
import com.sph.sphtech.BaseRobolectricTest
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters

@SmallTest
@FixMethodOrder(MethodSorters.JVM)
class MainNetTest : BaseRobolectricTest() {

    /**
     * test retrofit data load
     */
    @Test
    @Throws(Exception::class)
    fun testNet() {
        launch {
            var res = MainApi.service.reflushRecord("a807b7ab-6cad-4aa6-87d0-e283a7353a0f", 20)
            assert(res.success)
            assert(res.result?.records?.size == 30)

            //load more
            if (res.result?.total != res.result?.records?.size) {
                var next = res.result?.get_links()?.next
                res = MainApi.service.loadNextRecord(next!!)
                assert(res.success)
                assert(res.result?.records?.size == 29)
            }
        }
    }


}