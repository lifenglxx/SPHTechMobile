package com.sp.sphtech.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sp.sphtech.app.SPApplication
import com.sp.sphtech.data.db.dao.RecordDao
import com.sp.sphtech.data.model.RecordBean

@Database(entities = [RecordBean::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recordDao(): RecordDao

    companion object {
        val DATABASE_NAME = "sphtech-db"

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context = SPApplication.instance()): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // set db Name
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .build()
        }
    }

    override fun close() {
        super.close()
        instance = null
    }
}