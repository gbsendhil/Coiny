import com.binarybricks.coiny.data.database.entities.Coin
import com.binarybricks.coiny.data.database.entities.WatchedCoin
import com.binarybricks.coiny.network.models.CoinPrice
import com.binarybricks.coiny.stories.BaseView
import java.util.*

/**
Created by Pranay Airan
 */

interface CoinDashboardContract {

    interface View : BaseView {
        fun showOrHideLoadingIndicator(showLoading: Boolean = true)
        fun onWatchedCoinsLoaded(watchedCoinList: List<WatchedCoin>?)
        fun onCoinPricesLoaded(coinPriceListMap: HashMap<String, CoinPrice>)
        fun onSupportedCoinsLoaded(coinList: List<Coin>)
    }

    interface Presenter {
        fun loadWatchedCoins()
        fun loadCoinsPrices(fromCurrencySymbol: String, toCurrencySymbol: String)
        fun loadAllSupportedCoins()
        fun getAllSupportedExchanges()
    }
}