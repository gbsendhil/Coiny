package com.binarybricks.coiny.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coiny.R
import com.binarybricks.coiny.data.database.entities.Coin
import com.binarybricks.coiny.stories.transaction.CoinTransactionActivity
import kotlinx.android.synthetic.main.coin_add_transaction_module.view.*

/**
 * Created by Pragya Agrawal
 *
 * Simple class that wraps all logic related to showing Add transaction on coin details screen
 */

class AddCoinModule {

    fun init(layoutInflater: LayoutInflater, parent: ViewGroup?): View {
        return layoutInflater.inflate(R.layout.coin_add_transaction_module, parent, false)
    }

    fun addCoinListner(inflatedView: View, coin: Coin) {
        inflatedView.btnAddTransaction.setOnClickListener {
            inflatedView.context.startActivity(CoinTransactionActivity.buildLaunchIntent(inflatedView.context, coin))
        }
    }

    class AddCoinModuleData(val coin: Coin)
}