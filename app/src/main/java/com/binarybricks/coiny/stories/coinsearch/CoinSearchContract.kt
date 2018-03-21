import com.binarybricks.coiny.data.database.entities.WatchedCoin
import com.binarybricks.coiny.stories.BaseView

/**
Created by Pranay Airan
 */

interface CoinSearchContract {

    interface View : BaseView {
        fun showOrHideLoadingIndicator(showLoading: Boolean = true)
        fun onCoinsLoaded(coinList: List<WatchedCoin>)
    }

    interface Presenter {
        fun loadAllCoins()
    }
}