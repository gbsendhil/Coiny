import com.binarybricks.coinhood.network.models.CoinPrice
import com.intuit.qbse.components.mvp.BaseView

/**
 * Created by pranay airan on 1/17/18.
 */

interface CoinDetailContract {

    interface View : BaseView {
        fun showOrHideLoadingIndicator(showLoading: Boolean = true)
        fun onCoinDataLoaded(coinPrice: CoinPrice?)
    }

    interface Presenter {
        fun loadCurrentCoinPrice(fromCurrency: String, toCurrency: String)
    }
}