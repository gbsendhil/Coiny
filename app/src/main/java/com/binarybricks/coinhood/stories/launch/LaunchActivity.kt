package com.binarybricks.coinhood.stories.launch

import LaunchContract
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.binarybricks.coinhood.CoinHoodApplication
import com.binarybricks.coinhood.R
import com.binarybricks.coinhood.components.historicalchartmodule.LaunchPresenter
import com.binarybricks.coinhood.data.PreferenceHelper
import com.binarybricks.coinhood.network.schedulers.SchedulerProvider
import com.binarybricks.coinhood.stories.dashboard.CoinDashboardActivity
import kotlinx.android.synthetic.main.activity_launch.*
import timber.log.Timber

class LaunchActivity : AppCompatActivity(), LaunchContract.View {

    private val schedulerProvider: SchedulerProvider by lazy {
        SchedulerProvider.getInstance()
    }
    private val launchPresenter: LaunchPresenter by lazy {
        LaunchPresenter(schedulerProvider, CoinHoodApplication.database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        setSupportActionBar(toolbar as Toolbar?)

        launchPresenter.attachView(this)
        lifecycle.addObserver(launchPresenter)

        // determine if this is first time, if yes then show the animations else move away
        if (!PreferenceHelper.getPreference(this, PreferenceHelper.IS_LAUNCH_FTU_SHOWN, false)) {
            // get list of all coins
            launchPresenter.getAllSupportedCoins()

            // get list of all exchanges
            launchPresenter.getAllSupportedExchanges()

            // show  list of all currencies and option to choose one, default on phone locale

            // add 5 coins in watch list

            // FTU shown
        } else {
            startActivity(CoinDashboardActivity.buildLaunchIntent(this))
            finish()
        }
    }

    override fun onBackPressed() {
        Timber.i("Suppressing back press")
    }

    override fun onNetworkError(errorMessage: String) {
        Timber.e(errorMessage)
    }
}
