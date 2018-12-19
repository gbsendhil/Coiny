package com.binarybricks.coiny.stories

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.binarybricks.coiny.R
import com.binarybricks.coiny.stories.dashboard.CoinDashboardFragment
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

                R.id.actionSearch -> {
                    switchToSearch()
                }

                R.id.actionSettings -> {
                    switchToSettings()
                }
            }
            return@setOnNavigationItemSelectedListener true
        }


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

    private fun switchToSearch() {
    }

    private fun switchToSettings() {
    }
}
