package com.binarybricks.coinhood.stories

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.binarybricks.coinhood.R
import com.binarybricks.coinhood.stories.coindetails.CoinDetailsActivity
import com.lapism.searchview.SearchAdapter
import com.lapism.searchview.SearchItem
import com.lapism.searchview.SearchView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var nextMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<View>(R.id.toolbar)
        setSupportActionBar(toolbar as Toolbar?)

        supportActionBar?.title = ""

        setupSearchView()

        btc.setOnClickListener {
            startActivity(CoinDetailsActivity.buildLaunchIntent(this, btc.text.toString()))
        }

        eth.setOnClickListener {
            startActivity(CoinDetailsActivity.buildLaunchIntent(this, eth.text.toString()))
        }

        ltc.setOnClickListener {
            startActivity(CoinDetailsActivity.buildLaunchIntent(this, ltc.text.toString()))
        }

        xrp.setOnClickListener {
            startActivity(CoinDetailsActivity.buildLaunchIntent(this, xrp.text.toString()))
        }
    }

    // Menu icons are inflated just as they were with actionbar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home_menu, menu)

        nextMenuItem = menu.findItem(R.id.action_search)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.action_search -> {
                searchView.open(true) // enable or disable animation
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupSearchView() {
        searchView.version = SearchView.Version.MENU_ITEM
        searchView.versionMargins = SearchView.VersionMargins.MENU_ITEM
        searchView.theme = SearchView.Theme.DARK

        searchView.hint = "Search Coin"
        searchView.setVoice(false)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        searchView.setNavigationIconClickListener(View.OnClickListener {
            searchView.close(true)
        })

        searchView.setOnOpenCloseListener(object : SearchView.OnOpenCloseListener {
            override fun onOpen(): Boolean {
                nextMenuItem?.isVisible = false
                return true
            }

            override fun onClose(): Boolean {
                nextMenuItem?.isVisible = true
                return true
            }
        })

        val suggestionsList = ArrayList<SearchItem>()
        suggestionsList.add(SearchItem("Bitcoin"))
        suggestionsList.add(SearchItem("Bitcoin Cash"))
        suggestionsList.add(SearchItem("Litcoin"))

        val searchAdapter = SearchAdapter(this, suggestionsList)
        searchAdapter.setOnSearchItemClickListener(object : SearchAdapter.OnSearchItemClickListener {
            override fun onSearchItemClick(view: View, position: Int, text: String) {
//                mHistoryDatabase.addItem(SearchItem(text))
                searchView.close(false)
            }
        })
        searchView.adapter = searchAdapter
    }
}
