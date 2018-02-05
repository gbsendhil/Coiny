package com.binarybricks.coinhood.data.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.binarybricks.coinhood.data.database.entities.WatchedCoin
import io.reactivex.Flowable

/**
 * @author Pragya Agrawal on January 27, 2018
 *
 * Add queries to read/update coin data from database.
 */
@Dao
interface WatchedCoinDao {

    @Query("select * from WatchedCoin order by purchased")
    fun getAllWatchedCoins(): Flowable<List<WatchedCoin>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCoinsIntoWatchList(list: List<WatchedCoin>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCoinIntoWatchList(watchedCoin: WatchedCoin)
}
