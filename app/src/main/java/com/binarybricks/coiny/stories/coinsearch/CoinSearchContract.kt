import com.binarybricks.coiny.network.models.CCCoin
import com.binarybricks.coiny.stories.BaseView

/**
Created by Pranay Airan
 */

interface CoinSearchContract {

    interface View : BaseView {
        fun showOrHideLoadingIndicator(showLoading: Boolean = true)
        fun onCoinsLoaded(coinList: ArrayList<CCCoin>)
    }

    interface Presenter {
        fun loadAllCoins()
    }
}