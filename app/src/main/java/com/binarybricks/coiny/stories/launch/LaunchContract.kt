import com.binarybricks.coiny.stories.BaseView

/**
Created by Pranay Airan 2/3/18.
 */

interface LaunchContract {

    interface View : BaseView

    interface Presenter {
        fun getAllSupportedCoins()
        fun addTop5CoinsInWishlist(defaultCurrency: String)
    }
}