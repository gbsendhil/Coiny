package com.binarybricks.coiny.stories.launch

import LaunchContract
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import com.binarybricks.coiny.CoinyApplication
import com.binarybricks.coiny.R
import com.binarybricks.coiny.components.historicalchartmodule.LaunchPresenter
import com.binarybricks.coiny.data.PreferenceHelper
import com.binarybricks.coiny.network.schedulers.SchedulerProvider
import com.binarybricks.coiny.stories.dashboard.CoinDashboardActivity
import com.binarybricks.coiny.utils.defaultCurrency
import com.mynameismidori.currencypicker.CurrencyPicker
import kotlinx.android.synthetic.main.activity_launch.*
import timber.log.Timber

class LaunchActivity : AppCompatActivity(), LaunchContract.View {

    private val schedulerProvider: SchedulerProvider by lazy {
        SchedulerProvider.getInstance()
    }
    private val launchPresenter: LaunchPresenter by lazy {
        LaunchPresenter(schedulerProvider, CoinyApplication.database)
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

        picker.setListener { name, code, _, _ ->
            Timber.d("Currency code selected $name,$code")
            PreferenceHelper.setPreference(this, PreferenceHelper.DEFAULT_CURRENCY, code)

            continueButton.visibility = View.VISIBLE
            picker.dismiss() // Show currency that is picked.

            launchPresenter.addTop5CoinsInWishlist(PreferenceHelper.getPreference(this, code, defaultCurrency))
        }

        picker.show(supportFragmentManager, "CURRENCY_PICKER")
    }
}
