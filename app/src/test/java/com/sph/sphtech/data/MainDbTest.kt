package com.sph.sphtech.data

import androidx.test.filters.SmallTest
import com.sp.sphtech.data.db.AppDatabase
import com.sp.sphtech.data.model.RecordBean
import com.sph.sphtech.BaseRobolectricTest
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters

@SmallTest
@FixMethodOrder(MethodSorters.JVM)
class MainDbTest : BaseRobolectricTest() {
    /**
     * test db options
     */
    @Test
    @Throws(Exception::class)
    fun testDB() {
        launch {
            var dao = AppDatabase.getInstance().recordDao()
            var list = ArrayList<RecordBean>()
            var r1 = RecordBean()
            r1.volume_of_mobile_data = 1f
            r1._id = 1
            r1.year = 2015
            r1.quarter = "2015年Q1"
            list.add(r1)

            var r2 = RecordBean()
            r2.volume_of_mobile_data = 2f
            r2._id = 2
            r2.year = 2015
            r2.quarter = "2015年Q2"
            list.add(r2)
            dao.insert(list)
            var dbdata = dao.selectByYearData()
            assert(dbdata.size == 2)
        }
    }

    @Test
    @Throws(Exception::class)
    fun testDeleteDB() {
        launch {
            var dao = AppDatabase.getInstance().recordDao()
            var list = ArrayList<RecordBean>()
            var r1 = RecordBean()
            r1.volume_of_mobile_data = 1f
            r1._id = 1
            r1.year = 2015
            r1.quarter = "2015年Q1"
            list.add(r1)

            var r2 = RecordBean()
            r2.volume_of_mobile_data = 2f
            r2._id = 2
            r2.year = 2015
            r2.quarter = "2015年Q2"
            list.add(r2)

            dao.insert(list)

            dao.deleteAll()
            var dbdata = dao.selectByYearData()
            assert(dbdata.isEmpty())
        }
    }

}