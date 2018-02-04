package com.binarybricks.coinhood.data.database.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * @author Pragya Agrawal on January 27, 2018
 */
@Entity(tableName = "exchange")
data class Exchange(@ColumnInfo(name = "exchange_name") var name: String) {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
