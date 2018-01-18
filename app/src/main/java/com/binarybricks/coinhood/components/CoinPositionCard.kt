package com.binarybricks.coinhood.components

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import com.binarybricks.coinhood.R

/**
 * @author Pragya Agrawal on January 13, 2018
 *         Copyright (C) 2018 Ebates. All rights reserved.
 */

class CoinPositionCard : ConstraintLayout {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun init() {
        val layoutInflater = LayoutInflater.from(context)
        layoutInflater.inflate(R.layout.coin_position_card_component, this)
    }
}