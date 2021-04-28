package com.sp.sphtech.data.db.dao

import androidx.room.*
import com.sp.sphtech.data.model.RecordBean
import com.sp.sphtech.data.model.StatisticsBean

@Dao
interface RecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(records: List<RecordBean>)

    /**
     * select by year in 2008~2018
     */
    @Query("select * from RecordBean where year between 2008 and 2018 ")
    suspend fun selectByYearData(): List<RecordBean>


    @Query("DELETE FROM RecordBean")
    suspend fun deleteAll()

}