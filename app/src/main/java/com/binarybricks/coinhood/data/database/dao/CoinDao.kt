package com.binarybricks.coinhood.data.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.binarybricks.coinhood.data.database.entities.Coin


/**
 * Created by Pragya Agrawal
 *
 * Add queries to read/update coin data from database.
 */
@Dao
interface CoinDao {

    @Query("select * from coin")
    fun getAllCoins(): List<Coin>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCoins(list: List<Coin>)
}
