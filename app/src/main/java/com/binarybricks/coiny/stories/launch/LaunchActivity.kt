package com.binarybricks.coiny.stories.launch

import LaunchContract
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.binarybricks.coiny.CoinyApplication
import com.binarybricks.coiny.R
import com.binarybricks.coiny.components.historicalchartmodule.LaunchPresenter
import com.binarybricks.coiny.data.PreferenceHelper
import com.binarybricks.coiny.network.schedulers.SchedulerProvider
import com.binarybricks.coiny.stories.CryptoCompareRepository
import com.binarybricks.coiny.stories.HomeActivity
import com.binarybricks.coiny.utils.IntroPageTransformer
import com.binarybricks.coiny.utils.defaultCurrency
import com.mynameismidori.currencypicker.CurrencyPicker
import kotlinx.android.synthetic.main.activity_launch.*
import timber.log.Timber

class LaunchActivity : AppCompatActivity(), LaunchContract.View {

    private val schedulerProvider: SchedulerProvider by lazy {
        SchedulerProvider.getInstance()
    }
    private val coinRepo by lazy {
        CryptoCompareRepository(schedulerProvider, CoinyApplication.database)
    }

    private val launchPresenter: LaunchPresenter by lazy {
        LaunchPresenter(schedulerProvider, coinRepo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        setSupportActionBar(toolbar as Toolbar?)

        launchPresenter.attachView(this)
        lifecycle.addObserver(launchPresenter)

        // determine if this is first time, if yes then show the animations else move away
        if (!PreferenceHelper.getPreference(this, PreferenceHelper.IS_LAUNCH_FTU_SHOWN, false)) {
            initializeUI()

            launchPresenter.loadCoinsFromAPIInBackground()
            // FTU shown
            PreferenceHelper.setPreference(this, PreferenceHelper.IS_LAUNCH_FTU_SHOWN, true)
        } else {
            startActivity(HomeActivity.buildLaunchIntent(this))
            finish()
        }
    }

    private fun initializeUI() { // show  list of all currencies and option to choose one, default on phone locale

        // Set an Adapter on the ViewPager
        introPager.adapter = IntroAdapter(supportFragmentManager)

        // Set a PageTransformer
        introPager.setPageTransformer(false, IntroPageTransformer())
    }

    override fun onBackPressed() {
        Timber.i("Suppressing back press")
    }

    override fun onNetworkError(errorMessage: String) {
        Timber.e(errorMessage)
    }

    fun openCurrencyPicker() {
        val picker = CurrencyPicker.newInstance("Select Currency") // dialog title

        picker.setListener { name, code, _, _ ->
            Timber.d("Currency code selected $name,$code")
            PreferenceHelper.setPreference(this, PreferenceHelper.DEFAULT_CURRENCY, code)

            picker.dismiss() // Show currency that is picked.
            val currency = PreferenceHelper.getPreference(this, code, defaultCurrency)

            // get list of all coins
            launchPresenter.getAllSupportedCoins(currency)
        }

        picker.show(supportFragmentManager, "CURRENCY_PICKER")
    }

    // TODO add a progress dialog for this
    override fun onAllSupportedCoinsLoaded() {
        startActivity(HomeActivity.buildLaunchIntent(this))
        finish()
    }

    inner class IntroAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> IntroFragment.newInstance(R.raw.smiley_stack, "5000 Coins",
                    "Track more than 5000 coins, across 132 exchanges.", position, false) // 5000 curencies

                1 -> IntroFragment.newInstance(R.raw.graph, "Track Gains",
                    "Add transactions, track profit and loss. All at 1 place.", position, false) // Track transactions

                2 -> IntroFragment.newInstance(R.raw.bell, "Price Alert",
                    "Get price change alerts, for your favourite currencies.", position, true) // alert
                else -> IntroFragment.newInstance(R.raw.lock, "Highly Secure",
                    "Completely offline and secure. Your data never leaves the device.", position, true) // Secure
            }
        }

        override fun getCount(): Int {
            return 4
        }
    }
}
