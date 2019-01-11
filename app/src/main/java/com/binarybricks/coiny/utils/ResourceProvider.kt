package com.binarybricks.coiny.utils

import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.annotation.PluralsRes
import android.support.annotation.StringRes

/**
 * Created by Pranay Airan 1/16/18.
 */
interface ResourceProvider {

    fun getString(@StringRes resId: Int): String

    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String

    fun getQuantityString(@PluralsRes resId: Int, quantity: Int): String

    fun getQuantityString(@PluralsRes resId: Int, quantity: Int, vararg formatArgs: Any): String

    fun getColor(resId: Int): Int

    fun getDrawable(@DrawableRes resId: Int): Drawable?
}
