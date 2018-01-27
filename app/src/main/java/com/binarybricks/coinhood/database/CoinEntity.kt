package com.binarybricks.coinhood.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * @author Pragya Agrawal on January 27, 2018
 */
@Entity(tableName = "coin")
data class CoinEntity(@ColumnInfo(name = "url") var url: String,
                      @ColumnInfo(name = "imageUrl") var imageUrl: String,
                      @ColumnInfo(name = "name") var name: String,
                      @ColumnInfo(name = "symbol") var symbol: String,
                      @ColumnInfo(name = "coinName") var coinName: String,
                      @ColumnInfo(name = "fullName") var fullName: String,
                      @ColumnInfo(name = "algorithm") var algorithm: String,
                      @ColumnInfo(name = "fullyPremined") var fullyPremined: String,
                      @ColumnInfo(name = "totalCoinSupply") var totalCoinSupply: String,
                      @ColumnInfo(name = "preMinedValue") var preMinedValue: String,
                      @ColumnInfo(name = "totalCoinsFreeFloat") var totalCoinsFreeFloat: String,
                      @ColumnInfo(name = "sortOrder") var sortOrder: String,
                      @ColumnInfo(name = "sponsored") var sponsored: Boolean = false) {

    @ColumnInfo(name = "coinId")
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
