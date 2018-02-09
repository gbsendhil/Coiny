package com.binarybricks.coinhood.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coinhood.R

/**
 * Created by Pragya Agrawal
 *
 * Simple class that wraps all logic related to showing Add transaction on coin details screen
 */

class AddCoinModule {

    fun init(layoutInflater: LayoutInflater, parent: ViewGroup?): View {
        return layoutInflater.inflate(R.layout.coin_add_transaction_module, parent, false)
    }

    class AddCoinModuleData
}