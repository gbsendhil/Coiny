package com.binarybricks.coiny.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by pairan on 3/3/18.
 */
abstract class Module {

    abstract fun init(layoutInflater: LayoutInflater, parent: ViewGroup?): View
    abstract fun cleanUp()
}