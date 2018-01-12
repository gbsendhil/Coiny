package com.binarybricks.coinhood.network.models

/**
 * Created by pairan on 1/10/18.
 */

data class CryptoCompareHistoricalResponse(val firstValueInArray: String, val data: List<Data>, val timeFrom: String, val type: String,
                                           val response: String, val conversionType: ConversionType, val timeTo: String, val aggregated: String) {

    data class ConversionType(val conversionSymbol: String, val type: String)

    data class Data(val open: String, val time: String, val volumeto: String, val volumefrom: String,
                    val high: String, val low: String, val close: String)

}
