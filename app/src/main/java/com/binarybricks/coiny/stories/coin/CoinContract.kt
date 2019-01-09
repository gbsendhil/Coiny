import com.binarybricks.coiny.data.database.entities.CoinTransaction
import com.binarybricks.coiny.data.database.entities.WatchedCoin
import com.binarybricks.coiny.network.models.CoinPrice
import com.binarybricks.coiny.stories.BaseView

/**
Created by Pranay Airan
 */

interface CoinContract {

    interface View : BaseView {
        fun onCoinPriceLoaded(coinPrice: CoinPrice?, watchedCoin: WatchedCoin)
        fun onRecentTransactionLoaded(coinTransactionList: List<CoinTransaction>)
        fun onCoinWatchedStatusUpdated(watched: Boolean, coinSymbol: String)
    }

    interface Presenter {
        fun loadCurrentCoinPrice(watchedCoin: WatchedCoin, toCurrency: String)
        fun loadRecentTransaction(symbol: String)
        fun updateCoinWatchedStatus(watched: Boolean, coinID: String, coinSymbol: String)
    }
}