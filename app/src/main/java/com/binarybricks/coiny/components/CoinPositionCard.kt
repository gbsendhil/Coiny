package com.binarybricks.coiny.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coiny.R
import com.binarybricks.coiny.data.PreferenceHelper
import com.binarybricks.coiny.data.database.entities.CoinTransaction
import com.binarybricks.coiny.network.models.CoinPrice
import com.binarybricks.coiny.utils.Formatters
import com.binarybricks.coiny.utils.ResourceProvider
import com.binarybricks.coiny.utils.TRANSACTION_TYPE_BUY
import kotlinx.android.synthetic.main.coin_position_card_module.view.*
import timber.log.Timber
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.util.*

/**
 * Created by Pragya Agrawal
 */

class CoinPositionCard(private val resourceProvider: ResourceProvider) : Module() {

    private lateinit var inflatedView: View

    private val formatter by lazy {
        Formatters()
    }

    override fun init(layoutInflater: LayoutInflater, parent: ViewGroup?): View {

        inflatedView = layoutInflater.inflate(R.layout.coin_position_card_module, parent, false)
        return inflatedView
    }

    fun showNoOfCoinsView(coinPositionCardModuleData: CoinPositionCardModuleData) {

        val mc = MathContext(2, RoundingMode.HALF_UP)
        val currency = Currency.getInstance(PreferenceHelper.getDefaultCurrency(inflatedView.context))
        val coinPrice = coinPositionCardModuleData.coinPrice

        val noOfCoinsAndTotalCost = getNoOfCoinsAndTotalCost(coinPositionCardModuleData.coinTransactionList)
        val noOfCoins = noOfCoinsAndTotalCost.first
        val totalCost = noOfCoinsAndTotalCost.second

        inflatedView.tvNoOfCoins.text = noOfCoins.toString()
        inflatedView.tvCoinLabel.text = coinPrice.fromSymbol

        val totalCurrentValue = coinPositionCardModuleData.coinPrice.price?.toBigDecimal()?.multiply(noOfCoins.toBigDecimal())
        if (totalCurrentValue != null) {
            inflatedView.tvCoinValue.text = formatter.formatAmount(totalCurrentValue.toPlainString(), currency)
        }

        inflatedView.tvAvgCostValue.text = formatter.formatAmount(totalCost.divide(noOfCoins.toBigDecimal()).toPlainString(), currency)

        val totalReturnAmount = totalCurrentValue?.subtract(totalCost)
        val totalReturnPercentage = (totalReturnAmount?.divide(totalCost, mc))?.multiply(BigDecimal(100), mc)

        if (totalReturnAmount != null && totalReturnPercentage != null) {

            //TODO based on gain or loss do color change
            inflatedView.tvTotalReturnValue.text = "${formatter.formatAmount(totalReturnAmount.toPlainString(), currency)} (${totalReturnPercentage}%)"
        }
    }

    private fun getNoOfCoinsAndTotalCost(coinTransactionList: List<CoinTransaction>): Pair<Int, BigDecimal> {
        var noOfCoins = 0
        var totalCost = BigDecimal.ZERO

        coinTransactionList.forEach { coinTransaction ->
            if (coinTransaction.transactionType == TRANSACTION_TYPE_BUY) {
                noOfCoins += coinTransaction.quantity.toInt()
                totalCost += totalCost.add(coinTransaction.cost.toBigDecimal())
            } else {
                noOfCoins -= coinTransaction.quantity.toInt()
                totalCost -= totalCost.add(coinTransaction.cost.toBigDecimal())
            }
        }

        return Pair<Int, BigDecimal>(noOfCoins, totalCost)
    }

    override fun cleanUp() {
        Timber.d("Clean up add coinSymbol module")
    }

    data class CoinPositionCardModuleData(val coinPrice: CoinPrice, val coinTransactionList: List<CoinTransaction>) : ModuleItem
}