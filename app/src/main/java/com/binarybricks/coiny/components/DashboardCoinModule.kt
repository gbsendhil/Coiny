package com.binarybricks.coiny.components

import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binarybricks.coiny.R
import com.binarybricks.coiny.data.database.entities.WatchedCoin
import com.binarybricks.coiny.network.BASE_CRYPTOCOMPARE_IMAGE_URL
import com.binarybricks.coiny.network.models.CoinPrice
import com.binarybricks.coiny.stories.coindetails.CoinDetailsPagerActivity
import com.binarybricks.coiny.utils.Formatters
import com.binarybricks.coiny.utils.chartAnimationDuration
import com.binarybricks.coiny.utils.getDefaultExchangeText
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import jp.wasabeef.picasso.transformations.GrayscaleTransformation
import kotlinx.android.synthetic.main.dashboard_coin_module.view.*
import java.util.*

/**
 * Created by Pranay Airan
 */

class DashboardCoinModule(private val toCurrency: String) {

    private lateinit var picasso: Picasso

    private val currency by lazy {
        Currency.getInstance(toCurrency)
    }

    private val formatter by lazy {
        Formatters()
    }

    private val cropCircleTransformation by lazy {
        CropCircleTransformation()
    }

    private val grayscaleTransformation by lazy {
        GrayscaleTransformation()
    }

    fun init(layoutInflater: LayoutInflater, parent: ViewGroup?): View {
        val inflatedView = layoutInflater.inflate(R.layout.dashboard_coin_module, parent, false)
        picasso = Picasso.with(inflatedView.context)
        return inflatedView
    }

    fun showCoinInfo(inflatedView: View, dashboardCoinModuleData: DashboardCoinModuleData) {

        val coin = dashboardCoinModuleData.watchedCoin.coin
        val coinPrice = dashboardCoinModuleData.coinPrice

        val imageUrl = BASE_CRYPTOCOMPARE_IMAGE_URL + "${coin.imageUrl}?width=60"

        picasso.load(imageUrl).error(R.mipmap.ic_launcher_round)
            .transform(cropCircleTransformation)
            .transform(grayscaleTransformation)
            .into(inflatedView.ivCoin)

        inflatedView.tvCoinName.text = coin.coinName

        if (dashboardCoinModuleData.watchedCoin.purchased) {
            // do a different flow
            inflatedView.tvExchangeName.visibility = View.GONE
        } else {
            inflatedView.tvExchangeName.visibility = View.VISIBLE
            inflatedView.tvExchangeName.text = getDefaultExchangeText(dashboardCoinModuleData.watchedCoin.exchange, inflatedView.context)
        }

        if (coinPrice != null) {
            inflatedView.pbLoading.hide()

            animateCoinPrice(inflatedView, coinPrice.price)

            inflatedView.tvCoinPair.text = "${coinPrice.fromSymbol}/${coinPrice.toSymbol}"

            // adjust color logic here for text

            inflatedView.coinCard.setOnClickListener {
                inflatedView.context.startActivity(CoinDetailsPagerActivity.buildLaunchIntent(inflatedView.context, dashboardCoinModuleData.watchedCoin))
            }
        }
    }

    data class DashboardCoinModuleData(val watchedCoin: WatchedCoin, var coinPrice: CoinPrice?)


    private fun animateCoinPrice(inflatedView: View, amount: String?) {
        if (amount != null) {
            val chartCoinPriceAnimation = ValueAnimator.ofFloat(0f, amount.toFloat())
            chartCoinPriceAnimation.duration = chartAnimationDuration
            chartCoinPriceAnimation.addUpdateListener({ updatedAnimation ->
                val animatedValue = updatedAnimation.animatedValue as Float
                inflatedView.tvCoinPrice.text = formatter.formatAmount(animatedValue.toString(), currency)
                inflatedView.tvCoinPrice.tag = animatedValue
            })
            chartCoinPriceAnimation.start()
        }
    }
}