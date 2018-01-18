import com.binarybricks.coinhood.network.models.Coin
import com.binarybricks.coinhood.network.models.CryptoCompareHistoricalResponse
import com.intuit.qbse.components.mvp.BaseView

/**
 * Created by pranay airan on 1/17/18.
 */

interface HistoricalChartContract {

    interface View : BaseView {
        fun showOrHideChartLoadingIndicator(showLoading: Boolean = true)
        fun addCoinAndAnimateCoinPrice(coin: Coin?)
        fun onHistoricalDataLoaded(period: String, historicalDataPair: Pair<List<CryptoCompareHistoricalResponse.Data>, CryptoCompareHistoricalResponse.Data?>)
    }

    interface Presenter {

        fun loadCurrentCoinPrice(fromCurrency: String, toCurrency: String)
        fun loadHistoricalData(period: String, fromCurrency: String, toCurrency: String)
    }
}