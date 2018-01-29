package com.binarybricks.coinhood.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

/**
 * @author Pragya Agrawal on January 27, 2018
 */
@Database(entities = arrayOf(CoinEntity::class), version = 1, exportSchema = false)
abstract class CoinHoodDatabase : RoomDatabase() {

    abstract fun coinDao(): CoinDao
}
