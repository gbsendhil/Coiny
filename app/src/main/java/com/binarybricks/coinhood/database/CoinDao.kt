package com.binarybricks.coinhood.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query

/**
 * @author Pragya Agrawal on January 27, 2018
 *
 * Add queries to read/update data from database.
 */
@Dao
interface CoinDao {

    @Query("select * from coin")
    fun getAllCoins(): List<CoinEntity>
}
