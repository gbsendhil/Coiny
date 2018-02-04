package com.binarybricks.coinhood.stories.launch

import LaunchContract
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import com.binarybricks.coinhood.CoinHoodApplication
import com.binarybricks.coinhood.R
import com.binarybricks.coinhood.components.historicalchartmodule.LaunchPresenter
import com.binarybricks.coinhood.data.PreferenceHelper
import com.binarybricks.coinhood.data.database.DbController
import com.binarybricks.coinhood.data.database.getTop5CoinsToWatch
import com.binarybricks.coinhood.network.schedulers.SchedulerProvider
import com.binarybricks.coinhood.stories.dashboard.CoinDashboardActivity
import com.binarybricks.coinhood.utils.defaultCurrency
import com.binarybricks.coinhood.utils.defaultExchange
import com.mynameismidori.currencypicker.CurrencyPicker
import kotlinx.android.synthetic.main.activity_launch.btnChooseCurrency
import kotlinx.android.synthetic.main.activity_launch.continueButton
import kotlinx.android.synthetic.main.activity_launch.toolbar
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
        if (!PreferenceHelper.getPreference(this, PreferenceHelper.IS_LAUNCH_FTU_SHOWN,
                false)
        ) { // get list of all coins
            launchPresenter.getAllSupportedCoins()

            // get list of all exchanges
            launchPresenter.getAllSupportedExchanges()

            initializeUI()

            // FTU shown
            PreferenceHelper.setPreference(this, PreferenceHelper.IS_LAUNCH_FTU_SHOWN, true)
        } else {
            startActivity(CoinDashboardActivity.buildLaunchIntent(this))
            finish()
        }
    }

    private fun initializeUI() { // show  list of all currencies and option to choose one, default on phone locale
        btnChooseCurrency.setOnClickListener {
            openCurrencyPicker()
        }
        continueButton.setOnClickListener {
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

    private fun openCurrencyPicker() {
        val picker = CurrencyPicker.newInstance("Select Currency")  // dialog title

        picker.setListener { name, code, symbol, flagDrawableResID ->
            Timber.d("Currency code selected $name,$code")
            PreferenceHelper.setPreference(this, PreferenceHelper.DEFAULT_CURRENCY, code)
            continueButton.visibility = View.VISIBLE
            picker.dismiss() // Show currency that is picked.

            // add top 5 coins in watch list
            DbController(CoinHoodApplication.database).insertCoinsInWatchList(
                getTop5CoinsToWatch(defaultExchange,
                    PreferenceHelper.getPreference(this, code, defaultCurrency)), schedulerProvider)
        }

        picker.show(supportFragmentManager, "CURRENCY_PICKER")
    }
}
