package com.binarybricks.coinhood.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.binarybricks.coinhood.data.database.dao.CoinDao
import com.binarybricks.coinhood.data.database.dao.ExchangeDao
import com.binarybricks.coinhood.data.database.dao.WatchedCoinDao
import com.binarybricks.coinhood.data.database.entities.Coin
import com.binarybricks.coinhood.data.database.entities.Exchange
import com.binarybricks.coinhood.data.database.entities.WatchedCoin

/**
 * @author Pragya Agrawal on January 27, 2018
 */
@Database(
    entities = [Coin::class, Exchange::class, WatchedCoin::class], version = 1, exportSchema = false
)
abstract class CoinHoodDatabase : RoomDatabase() {

    abstract fun coinDao(): CoinDao
    abstract fun exchangeDao(): ExchangeDao
    abstract fun watchedCoinDao(): WatchedCoinDao
}
