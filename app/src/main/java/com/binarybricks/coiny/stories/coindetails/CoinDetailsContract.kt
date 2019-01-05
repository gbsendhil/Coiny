import com.binarybricks.coiny.data.database.entities.WatchedCoin
import com.binarybricks.coiny.stories.BaseView

/**
Created by Pranay Airan
 */

interface CoinDetailsContract {

    interface View : BaseView {
        fun showOrHideLoadingIndicator(showLoading: Boolean = true)
        fun onWatchedCoinLoaded(coin: WatchedCoin?)
    }

    interface Presenter {
        fun getWatchedCoinFromSymbol(symbol: String)
    }
}