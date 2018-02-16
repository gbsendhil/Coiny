package com.binarybricks.coiny.data.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.binarybricks.coiny.data.database.entities.WatchedCoin
import io.reactivex.Flowable

/**
 * Created by Pragya Agrawal
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
