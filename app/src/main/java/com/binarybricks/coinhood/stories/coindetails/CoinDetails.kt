package com.binarybricks.coinhood.stories.coindetails

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.binarybricks.coinhood.R
import com.binarybricks.coinhood.network.schedulers.SchedulerProvider
import com.binarybricks.coinhood.utils.ResourceProviderImpl
import kotlinx.android.synthetic.main.activity_coin_details.*

class CoinDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_details)

        val schedulerProvider = SchedulerProvider.getInstance()
        val resourceProvider = ResourceProviderImpl(applicationContext)

        val coinDetailList: MutableList<Any> = ArrayList()
        coinDetailList.add("")

        rvCoinDetails.layoutManager = LinearLayoutManager(this)
        rvCoinDetails.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        rvCoinDetails.adapter = CoinDetailsAdapter(this, coinDetailList, schedulerProvider, resourceProvider)
    }
}
