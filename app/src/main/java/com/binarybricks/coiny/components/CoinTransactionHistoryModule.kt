package com.binarybricks.coiny.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coiny.R
import com.binarybricks.coiny.data.PreferenceHelper
import com.binarybricks.coiny.data.database.entities.CoinTransaction
import com.binarybricks.coiny.utils.Formatters
import com.binarybricks.coiny.utils.TRANSACTION_TYPE_SELL
import kotlinx.android.synthetic.main.coin_transaction_module.view.*
import timber.log.Timber
import java.util.*

/**
 * Created by Pranay Airan
 * A compound layout to see coin transaction history
 */
class CoinTransactionHistoryModule : Module() {

    private val formatter by lazy {
        Formatters()
    }

    override fun init(layoutInflater: LayoutInflater, parent: ViewGroup?): View {
        return layoutInflater.inflate(R.layout.coin_transaction_module, parent, false)
    }

    fun showRecentTransactions(inflatedView: View, coinTransactionHistoryModuleData: CoinTransactionHistoryModuleData) {
        val coinTransactionList = coinTransactionHistoryModuleData.coinTransactionList
        val currency = Currency.getInstance(PreferenceHelper.getDefaultCurrency(inflatedView.context))

        if (coinTransactionList.size > 0) {
            val coinTransaction = coinTransactionList[0]
            var transactionType = "Buy"
            if (coinTransaction.transactionType == TRANSACTION_TYPE_SELL) transactionType = "Sell"
            inflatedView.tvFirstTxnTypeAndQuantity.text = "${transactionType} (${coinTransaction.amount})"
            inflatedView.tvFirstTxnCost.text = formatter.formatAmount(coinTransaction.cost, currency, false)
            inflatedView.tvFirstTxnTimeAndExchange.text = "${formatter.formatTransactionDate(coinTransaction.transactionTime)} via ${coinTransaction.exchange}"
        } else {
            inflatedView.clFirstTransaction.visibility = View.GONE
        }

        if (coinTransactionList.size > 1) {
            val coinTransaction = coinTransactionList[1]
            var transactionType = "Buy"
            if (coinTransaction.transactionType == TRANSACTION_TYPE_SELL) transactionType = "Sell"
            inflatedView.tvFirstTxnTypeAndQuantity.text = "${transactionType} (${coinTransaction.amount})"
            inflatedView.tvFirstTxnCost.text = formatter.formatAmount(coinTransaction.cost, currency, false)
            inflatedView.tvFirstTxnTimeAndExchange.text = "${formatter.formatTransactionDate(coinTransaction.transactionTime)} via ${coinTransaction.exchange}"
        } else {
            inflatedView.clSecondTransaction.visibility = View.GONE
        }

        if (coinTransactionList.size > 2) {
            val coinTransaction = coinTransactionList[2]
            var transactionType = "Buy"
            if (coinTransaction.transactionType == TRANSACTION_TYPE_SELL) transactionType = "Sell"
            inflatedView.tvFirstTxnTypeAndQuantity.text = "${transactionType} (${coinTransaction.amount})"
            inflatedView.tvFirstTxnCost.text = formatter.formatAmount(coinTransaction.cost, currency, false)
            inflatedView.tvFirstTxnTimeAndExchange.text = "${formatter.formatTransactionDate(coinTransaction.transactionTime)} via ${coinTransaction.exchange}"
        } else {
            inflatedView.clThirdTransaction.visibility = View.GONE
        }

        if (coinTransactionList.size <= 3) {
            inflatedView.dividerView.visibility = View.GONE
            inflatedView.tvMore.visibility = View.GONE
        }
    }

    override fun cleanUp() {
        Timber.d("Cleaning up transaction history module")
    }

    class CoinTransactionHistoryModuleData(val coinTransactionList: List<CoinTransaction>) : ModuleItem
}