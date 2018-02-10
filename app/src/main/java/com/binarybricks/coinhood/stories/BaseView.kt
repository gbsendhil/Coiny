package com.binarybricks.coinhood.stories

/**
 * A base view interface used for all views,
 * and to signal network errors.
 */
interface BaseView {

    /**
     * Callback to signal a network error
     **/
    fun onNetworkError(errorMessage: String)
}