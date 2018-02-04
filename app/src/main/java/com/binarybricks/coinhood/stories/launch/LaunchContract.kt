import com.intuit.qbse.components.mvp.BaseView

/**
 * Created by pranay airan on 2/3/18.
 */

interface LaunchContract {

    interface View : BaseView

    interface Presenter {
        fun getAllSupportedCoins()
        fun getAllSupportedExchanges()
    }
}