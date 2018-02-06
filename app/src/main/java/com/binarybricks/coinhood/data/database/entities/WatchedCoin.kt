package com.binarybricks.coinhood.data.database.entities

import android.arch.persistence.room.*
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author Pragya Agrawal on January 27, 2018
 */
@Entity(indices = [(Index("coinId", unique = true))])
@Parcelize
data class WatchedCoin(

    @Embedded
    val coin: Coin,
    var exchange: String,
    var fromCurrency: String,
    var purchased: Boolean = false,
    var purchaseQuantity: String

) : Parcelable {

    @ColumnInfo(name = "watched_id")
    @PrimaryKey(autoGenerate = true)
    var idKey: Long = 0
}
