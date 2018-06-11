import com.binarybricks.coiny.data.database.entities.CoinTransaction
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
        fun onWatchedCoinsAndTransactionsLoaded(watchedCoinList: List<WatchedCoin>, coinTransactionList: List<CoinTransaction>)
        fun onCoinPricesLoaded(coinPriceListMap: HashMap<String, CoinPrice>)
    }

    interface Presenter {
        fun loadWatchedCoinsAndTransactions()
        fun loadCoinsPrices(fromCurrencySymbol: String, toCurrencySymbol: String)
        fun getAllSupportedExchanges()
    }
}