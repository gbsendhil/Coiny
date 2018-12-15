package com.binarybricks.coiny.stories

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.binarybricks.coiny.R
import com.binarybricks.coiny.stories.coinsearch.CoinSearchActivity
import com.binarybricks.coiny.stories.dashboard.CoinDashboardFragment

class HomeActivity : AppCompatActivity() {

    companion object {
        @JvmStatic
        fun buildLaunchIntent(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
        }
    }

    private var nextMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        switchToDashboard(savedInstanceState)

    }


    // Menu icons are inflated just as they were with actionbar
    override fun onCreateOptionsMenu(menu: Menu): Boolean { // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home_menu, menu)

        nextMenuItem = menu.findItem(R.id.action_search)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_search -> {
                startActivity(CoinSearchActivity.buildLaunchIntent(this))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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
}
