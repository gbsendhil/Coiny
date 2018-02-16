package com.binarybricks.coiny.data.database.entities

import android.arch.persistence.room.*
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Pragya Agrawal
 */
@Entity(indices = [(Index("coinId", unique = true))])
@Parcelize
data class WatchedCoin(

    @Embedded
    val coin: Coin,
    var exchange: String,
    var fromCurrency: String,
    var purchased: Boolean = false,
    var purchaseQuantity: String,
    @ColumnInfo(name = "watched_id") @PrimaryKey(autoGenerate = true) var idKey: Long = 0
) : Parcelable
