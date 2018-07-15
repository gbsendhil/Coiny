package com.binarybricks.coiny.stories

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.binarybricks.coiny.R
import com.binarybricks.coiny.stories.dashboard.CoinDashboardFragment
import com.binarybricks.coiny.utils.dpToPx
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    companion object {
        @JvmStatic
        fun buildLaunchIntent(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        switchToDashboard(savedInstanceState)

        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.actionHome -> {
                    switchToDashboard(savedInstanceState)
                }

                R.id.actionAlert -> {
                    switchToAlerts()
                }

                R.id.actionSettings -> {
                    switchToSettings()
                }
            }
            return@setOnNavigationItemSelectedListener true
        }

        bottomNavigation.setTextVisibility(false)
        bottomNavigation.enableAnimation(false)
        bottomNavigation.itemHeight = dpToPx(baseContext, 48)
        bottomNavigation.setIconSize(dpToPx(baseContext, 12).toFloat(), dpToPx(baseContext, 12).toFloat())
    }

    private fun switchToDashboard(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val coinDashboardFragment = CoinDashboardFragment()

            supportFragmentManager.beginTransaction()
                .replace(R.id.containerLayout, coinDashboardFragment, CoinDashboardFragment.TAG)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun switchToAlerts() {
    }

    private fun switchToSettings() {
    }
}
