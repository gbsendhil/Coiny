package com.binarybricks.coiny.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.binarybricks.coiny.data.database.dao.CoinDao
import com.binarybricks.coiny.data.database.dao.ExchangeDao
import com.binarybricks.coiny.data.database.dao.WatchedCoinDao
import com.binarybricks.coiny.data.database.entities.Coin
import com.binarybricks.coiny.data.database.entities.Exchange
import com.binarybricks.coiny.data.database.entities.WatchedCoin

/**
 * Created by Pragya Agrawal
 */
@Database(
        entities = [Coin::class, Exchange::class, WatchedCoin::class], version = 1, exportSchema = false
)
abstract class CoinyDatabase : RoomDatabase() {

    abstract fun coinDao(): CoinDao
    abstract fun exchangeDao(): ExchangeDao
    abstract fun watchedCoinDao(): WatchedCoinDao
}
