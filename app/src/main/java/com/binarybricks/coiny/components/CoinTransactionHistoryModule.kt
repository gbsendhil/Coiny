package com.binarybricks.coiny.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coiny.R
import com.binarybricks.coiny.data.PreferenceHelper
import com.binarybricks.coiny.data.database.entities.CoinTransaction
import com.binarybricks.coiny.utils.Formatters
import com.binarybricks.coiny.utils.ResourceProvider
import com.binarybricks.coiny.utils.TRANSACTION_TYPE_SELL
import kotlinx.android.synthetic.main.coin_transaction_module.view.*
import timber.log.Timber
import java.util.*

/**
 * Created by Pranay Airan
 * A compound layout to see coinSymbol transaction history
 */
class CoinTransactionHistoryModule(private val resourceProvider: ResourceProvider) : Module() {

    private val formatter by lazy {
        Formatters(resourceProvider)
    }

    override fun init(layoutInflater: LayoutInflater, parent: ViewGroup?): View {
        return layoutInflater.inflate(R.layout.coin_transaction_module, parent, false)
    }

    fun showRecentTransactions(inflatedView: View, coinTransactionHistoryModuleData: CoinTransactionHistoryModuleData) {
        val coinTransactionList = coinTransactionHistoryModuleData.coinTransactionList
        val currency = Currency.getInstance(PreferenceHelper.getDefaultCurrency(inflatedView.context))

        var transactionType = resourceProvider.getString(R.string.buy)

        if (coinTransactionList.isNotEmpty()) {
            val coinTransaction = coinTransactionList[0]

            if (coinTransaction.transactionType == TRANSACTION_TYPE_SELL) {
                transactionType = resourceProvider.getString(R.string.sell)
            }

            inflatedView.tvFirstTxnTypeAndQuantity.text = resourceProvider.getString(R.string.transactionTypeWithQuantity,
                    transactionType, coinTransaction.quantity.toPlainString())

            inflatedView.tvFirstTxnCost.text = formatter.formatAmount(coinTransaction.cost, currency, false)

            inflatedView.tvFirstTxnTimeAndExchange.text = resourceProvider.getString(R.string.transactionTimeWithExchange,
                    formatter.formatTransactionDate(coinTransaction.transactionTime), coinTransaction.exchange)
        } else {
            inflatedView.clFirstTransaction.visibility = View.GONE
        }

        if (coinTransactionList.size > 1) {
            val coinTransaction = coinTransactionList[1]

            if (coinTransaction.transactionType == TRANSACTION_TYPE_SELL) {
                transactionType = resourceProvider.getString(R.string.sell)
            }

            inflatedView.tvFirstTxnTypeAndQuantity.text = resourceProvider.getString(R.string.transactionTypeWithQuantity,
                    transactionType, coinTransaction.quantity.toPlainString())

            inflatedView.tvFirstTxnCost.text = formatter.formatAmount(coinTransaction.cost, currency, false)
            inflatedView.tvFirstTxnTimeAndExchange.text = resourceProvider.getString(R.string.transactionTimeWithExchange,
                    formatter.formatTransactionDate(coinTransaction.transactionTime), coinTransaction.exchange)
        } else {
            inflatedView.clSecondTransaction.visibility = View.GONE
        }

        if (coinTransactionList.size > 2) {
            val coinTransaction = coinTransactionList[2]

            if (coinTransaction.transactionType == TRANSACTION_TYPE_SELL) {
                transactionType = resourceProvider.getString(R.string.sell)
            }

            inflatedView.tvFirstTxnTypeAndQuantity.text = resourceProvider.getString(R.string.transactionTypeWithQuantity,
                    transactionType, coinTransaction.quantity.toPlainString())
            inflatedView.tvFirstTxnCost.text = formatter.formatAmount(coinTransaction.cost, currency, false)
            inflatedView.tvFirstTxnTimeAndExchange.text = resourceProvider.getString(R.string.transactionTimeWithExchange,
                    formatter.formatTransactionDate(coinTransaction.transactionTime), coinTransaction.exchange)
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