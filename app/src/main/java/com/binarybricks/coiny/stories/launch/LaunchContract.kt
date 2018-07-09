import com.binarybricks.coiny.stories.BaseView

/**
Created by Pranay Airan 2/3/18.
 */

interface LaunchContract {

    interface View : BaseView {
        fun onAllSupportedCoinsLoaded()
    }

    interface Presenter {
        fun loadCoinsFromAPIInBackground()
        fun getAllSupportedCoins(defaultCurrency: String)
    }
}