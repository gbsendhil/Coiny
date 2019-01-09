import com.binarybricks.coiny.data.database.entities.WatchedCoin
import com.binarybricks.coiny.stories.BaseView

/**
Created by Pranay Airan
 */

interface CoinDetailsPagerContract {

    interface View : BaseView {
        fun onWatchedCoinsLoaded(watchedCoinList: List<WatchedCoin>?)
    }

    interface Presenter {
        fun loadWatchedCoins()
    }
}