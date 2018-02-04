package com.binarybricks.coinhood.data.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.binarybricks.coinhood.data.database.entities.WatchedCoin

/**
 * @author Pragya Agrawal on January 27, 2018
 *
 * Add queries to read/update coin data from database.
 */
@Dao
interface WatchedCoinDao {

    @Query("select * from WatchedCoin")
    fun getAllWatchedCoins(): List<WatchedCoin>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCoinsIntoWatchList(list: List<WatchedCoin>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCoinIntoWatchList(watchedCoin: WatchedCoin)
}
