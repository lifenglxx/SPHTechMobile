package com.sp.sphtech.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecordBean(@PrimaryKey(autoGenerate = true) var _id: Int = 0) {
    var volume_of_mobile_data = 0f
    var quarter = ""
    var year = 0
}