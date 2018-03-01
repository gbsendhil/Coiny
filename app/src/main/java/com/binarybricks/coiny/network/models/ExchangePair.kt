package com.binarybricks.coiny.network.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by pairan on 2/18/18.
 */

@Parcelize
data class ExchangePair(val exchangeName: String, val pairList: MutableList<String>) : Parcelable