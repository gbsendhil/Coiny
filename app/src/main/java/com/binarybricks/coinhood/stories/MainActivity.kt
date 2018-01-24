package com.binarybricks.coinhood.stories

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.binarybricks.coinhood.R
import com.binarybricks.coinhood.stories.coindetails.CoinDetailsActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btc.setOnClickListener {
            startActivity(CoinDetailsActivity.buildLaunchIntent(this, btc.text.toString()))
        }

        eth.setOnClickListener {
            startActivity(CoinDetailsActivity.buildLaunchIntent(this, eth.text.toString()))
        }

        ltc.setOnClickListener {
            startActivity(CoinDetailsActivity.buildLaunchIntent(this, ltc.text.toString()))
        }

        xrp.setOnClickListener {
            startActivity(CoinDetailsActivity.buildLaunchIntent(this, xrp.text.toString()))
        }
    }
}
