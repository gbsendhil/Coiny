package com.binarybricks.coiny.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coiny.R
import com.binarybricks.coiny.data.database.entities.CoinTransaction
import com.binarybricks.coiny.network.models.CoinPrice
import com.binarybricks.coiny.utils.TRANSACTION_TYPE_BUY
import kotlinx.android.synthetic.main.coin_position_card_module.view.*
import timber.log.Timber

/**
 * Created by Pragya Agrawal
 */

class CoinPositionCard : Module() {

    private lateinit var inflatedView: View

    override fun init(layoutInflater: LayoutInflater, parent: ViewGroup?): View {

        inflatedView = layoutInflater.inflate(R.layout.coin_position_card_module, parent, false)
        return inflatedView
    }

    fun showNoOfCoinsView(coinPositionCardModuleData: CoinPositionCardModuleData) {
        val coinPrice = coinPositionCardModuleData.coinPrice
        val noOfCoins = getNoOfCoins(coinPositionCardModuleData.coinTransactionList)
        inflatedView.tvNoOfCoins.text = noOfCoins.toString()
        inflatedView.tvCoinLabel.text = coinPrice.fromSymbol
    }

    private fun getNoOfCoins(coinTransactionList: List<CoinTransaction>): Int {
        var noOfCoins = 0

        coinTransactionList.forEach { coinTransaction ->
            if (coinTransaction.transactionType == TRANSACTION_TYPE_BUY) {
                noOfCoins += coinTransaction.amount.toInt()
            } else {
                noOfCoins -= coinTransaction.amount.toInt()
            }
        }

        return noOfCoins
    }

    override fun cleanUp() {
        Timber.d("Clean up add coin module")
    }

    data class CoinPositionCardModuleData(val coinPrice: CoinPrice, val coinTransactionList: List<CoinTransaction>) : ModuleItem
}