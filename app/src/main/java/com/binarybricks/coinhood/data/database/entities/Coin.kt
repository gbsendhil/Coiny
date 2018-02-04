package com.binarybricks.coinhood.data.database.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

/**
 * @author Pragya Agrawal on January 27, 2018
 */
@Entity(indices = [(Index("coinId", unique = true))])
data class Coin(
    @ColumnInfo(name = "coinId") var id: String,
    @ColumnInfo(name = "url") var url: String,
    @ColumnInfo(name = "imageUrl") var imageUrl: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "symbol") var symbol: String,
    @ColumnInfo(name = "coinName") var coinName: String,
    @ColumnInfo(name = "fullName") var fullName: String,
    @ColumnInfo(name = "algorithm") var algorithm: String,
    @ColumnInfo(name = "proofType") var proofType: String,
    @ColumnInfo(name = "fullyPremined") var fullyPremined: String,
    @ColumnInfo(name = "totalCoinSupply") var totalCoinSupply: String,
    @ColumnInfo(name = "preMinedValue") var preMinedValue: String,
    @ColumnInfo(name = "totalCoinsFreeFloat") var totalCoinsFreeFloat: String,
    @ColumnInfo(name = "sortOrder") var sortOrder: String,
    @ColumnInfo(name = "sponsored") var sponsored: Boolean = false,
    @ColumnInfo(name = "watched") var watched: Boolean = false
) {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var idKey: Long = 0
}
