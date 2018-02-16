import com.binarybricks.coinhood.data.database.entities.WatchedCoin
import com.binarybricks.coinhood.stories.BaseView

/**
Created by Pranay Airan
 */

interface CoinDetailsPagerContract {

    interface View : BaseView {
        fun showOrHideLoadingIndicator(showLoading: Boolean = true)
        fun onWatchedCoinsLoaded(watchedCoinList: List<WatchedCoin>?)
    }

    interface Presenter {
        fun loadWatchedCoins()
    }
}