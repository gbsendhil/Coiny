package com.binarybricks.coinhood.stories.coindetails

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.binarybricks.coinhood.data.database.entities.WatchedCoin

/**
 * Created by pranay airan on 2/11/18.
 */

class CoinDetailsPagerAdapter(val watchedCoinList: List<WatchedCoin>?, fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment? {
        if (watchedCoinList != null) {
            val coinDetailsFragment = CoinDetailsFragment()
            coinDetailsFragment.arguments = CoinDetailsFragment.getArgumentBundle(watchedCoinList[position])
            return coinDetailsFragment
        }
        return null
    }

    override fun getCount(): Int {
        return watchedCoinList?.size ?: 0
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return watchedCoinList?.get(position)?.coin?.symbol ?: super.getPageTitle(position)
    }
}