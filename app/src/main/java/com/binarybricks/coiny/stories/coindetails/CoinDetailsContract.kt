import com.binarybricks.coiny.data.database.entities.WatchedCoin
import com.binarybricks.coiny.network.models.CoinPrice
import com.binarybricks.coiny.stories.BaseView

/**
Created by Pranay Airan
 */

interface CoinDetailsContract {

    interface View : BaseView {
        fun showOrHideLoadingIndicator(showLoading: Boolean = true)
        fun onCoinPriceLoaded(coinPrice: CoinPrice?, watchedCoin: WatchedCoin)
    }

    interface Presenter {
        fun loadCurrentCoinPrice(watchedCoin: WatchedCoin, toCurrency: String)
    }
}