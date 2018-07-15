package com.binarybricks.coiny.data.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.binarybricks.coiny.data.database.entities.Exchange

/**
 * Created by Pragya Agrawal
 *
 * Add queries to read/update exchange data from database.
 */
@Dao
interface ExchangeDao {

    @Query("select * from exchange")
    fun getAllExchanges(): List<Exchange>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExchanges(list: List<Exchange>)
}
