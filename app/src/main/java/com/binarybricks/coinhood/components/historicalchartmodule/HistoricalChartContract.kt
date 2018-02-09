import com.binarybricks.coinhood.network.models.CoinPrice
import com.binarybricks.coinhood.network.models.CryptoCompareHistoricalResponse
import com.binarybricks.coinhood.stories.BaseView

/**
 * Created by pranay airan on 1/17/18.
 */

interface HistoricalChartContract {

    interface View : BaseView {
        fun showOrHideChartLoadingIndicator(showLoading: Boolean = true)
        fun addCoinAndAnimateCoinPrice(coinPrice: CoinPrice?)
        fun onHistoricalDataLoaded(period: String, historicalDataPair: Pair<List<CryptoCompareHistoricalResponse.Data>, CryptoCompareHistoricalResponse.Data?>)
    }

    interface Presenter {
        fun loadHistoricalData(period: String, fromCurrency: String, toCurrency: String)
    }
}