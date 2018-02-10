import com.binarybricks.coinhood.network.models.CryptoPanicNews
import com.binarybricks.coinhood.stories.BaseView

/**
 * Created by Pragya Agrawal
 */

interface CryptoNewsContract {

    interface View : BaseView {
        fun showOrHideLoadingIndicator(showLoading: Boolean = true)
        fun onNewsLoaded(cryptoPanicNews: CryptoPanicNews)
    }

    interface Presenter {
        fun getCryptoNews(coinSymbol: String)
    }
}