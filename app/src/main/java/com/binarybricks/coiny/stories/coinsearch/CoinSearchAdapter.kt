package com.binarybricks.coiny.stories.coinsearch

/**
Created by Pranay Airan 1/26/18.
 */


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import com.binarybricks.coiny.R
import com.binarybricks.coiny.data.database.entities.WatchedCoin
import com.binarybricks.coiny.network.BASE_CRYPTOCOMPARE_IMAGE_URL
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import jp.wasabeef.picasso.transformations.GrayscaleTransformation

class CoinSearchAdapter(val searchList: List<WatchedCoin>) : RecyclerView.Adapter<CoinSearchAdapter.ResultViewHolder>(), Filterable {

    var filterSearchList: List<WatchedCoin> = searchList
    private lateinit var picasso: Picasso

    private val cropCircleTransformation by lazy {
        CropCircleTransformation()
    }

    private val grayscaleTransformation by lazy {
        GrayscaleTransformation()
    }

    private var mListener: OnSearchItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.coin_search_item, parent, false)
        picasso = Picasso.with(view.context)

        return ResultViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ResultViewHolder, position: Int) {
        viewHolder.tvCoinName.text = filterSearchList[position].coin.coinName
        viewHolder.tvCoinSymbol.text = filterSearchList[position].coin.symbol

        picasso.load(BASE_CRYPTOCOMPARE_IMAGE_URL + "${filterSearchList[position].coin.imageUrl}?width=50").error(R.mipmap.ic_launcher_round)
            .transform(cropCircleTransformation)
            .transform(grayscaleTransformation)
            .into(viewHolder.ivCoin)
    }

    override fun getItemCount(): Int {
        return filterSearchList.size
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(searchQuery: CharSequence): Filter.FilterResults {

                val filterString = searchQuery.toString().trim().toLowerCase()

                val results = Filter.FilterResults()

                val list = searchList

                val count = list.size
                val filteredList = mutableListOf<WatchedCoin>()

                (0 until count)
                    .filter {
                        // Filter on the name
                        list[it].coin.coinName.contains(filterString, true) ||
                                list[it].coin.symbol.contains(filterString, true)
                    }
                    .mapTo(filteredList) { list[it] }

                results.values = filteredList
                results.count = filteredList.size

                return results
            }

            override fun publishResults(charSequence: CharSequence, results: Filter.FilterResults) {
                filterSearchList = results.values as MutableList<WatchedCoin>
                notifyDataSetChanged()
            }
        }
    }

    fun setOnSearchItemClickListener(listener: OnSearchItemClickListener) {
        mListener = listener
    }

    interface OnSearchItemClickListener {
        fun onSearchItemClick(view: View, position: Int, text: String)
    }

    inner class ResultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCoinName: TextView = view.findViewById(R.id.tvCoinName)
        val tvCoinSymbol: TextView = view.findViewById(R.id.tvCoinSymbol)
        val ivCoin: ImageView = view.findViewById(R.id.ivCoin)

        init {
            // add second text
            view.setOnClickListener {
                mListener?.let { searchListner ->
                    searchListner.onSearchItemClick(it, layoutPosition, tvCoinSymbol.text.toString())
                }
            }
        }
    }

}