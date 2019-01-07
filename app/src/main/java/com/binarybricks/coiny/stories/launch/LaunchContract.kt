import com.binarybricks.coiny.stories.BaseView

/**
Created by Pranay Airan 2/3/18.
 */

interface LaunchContract {

    interface View : BaseView {
        fun onCoinsLoaded()
        fun onAllSupportedCoinsLoaded()
    }

    interface Presenter {
        fun loadAllCoins()
        fun getAllSupportedCoins(defaultCurrency: String)
    }
}