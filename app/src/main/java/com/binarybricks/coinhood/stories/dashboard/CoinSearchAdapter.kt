package com.binarybricks.coinhood.stories.dashboard

/**
 Created by Pranay Airan 1/26/18.
 */


import android.content.Context
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import com.binarybricks.coinhood.R
import com.lapism.searchview.SearchHistoryTable
import com.lapism.searchview.SearchItem
import com.lapism.searchview.SearchView
import java.util.*

class CoinSearchAdapter : RecyclerView.Adapter<CoinSearchAdapter.ResultViewHolder>, Filterable {

    protected val mHistoryDatabase: SearchHistoryTable
    var mDatabaseKey: Int? = null
    var key: CharSequence = ""
        protected set
    protected var mSuggestions: List<SearchItem> = ArrayList()
    var resultsList: List<SearchItem> = ArrayList()
        protected set
    protected var mListener: OnSearchItemClickListener? = null

    // ---------------------------------------------------------------------------------------------
    var suggestionsList: List<SearchItem>
        get() = mSuggestions
        set(suggestionsList) {
            mSuggestions = suggestionsList
            resultsList = suggestionsList
        }

    constructor(context: Context) {
        mHistoryDatabase = SearchHistoryTable(context)
        filter.filter("")
    }

    constructor(context: Context, suggestions: List<SearchItem>) {
        mSuggestions = suggestions
        resultsList = suggestions
        mHistoryDatabase = SearchHistoryTable(context)
        filter.filter("")
    }

    // ---------------------------------------------------------------------------------------------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.search_item, parent, false)
        return ResultViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ResultViewHolder, position: Int) {
        val item = resultsList[position]

        viewHolder.icon.setImageResource(item.iconResource)
        viewHolder.icon.setColorFilter(SearchView.getIconColor(), PorterDuff.Mode.SRC_IN)
        viewHolder.text.typeface = Typeface.create(SearchView.getTextFont(), SearchView.getTextStyle())
        viewHolder.text.setTextColor(SearchView.getTextColor())

        val itemText = item.text.toString()
        val itemTextLower = itemText.toLowerCase(Locale.getDefault())

        if (itemTextLower.contains(key) && !TextUtils.isEmpty(key)) {
            val s = SpannableString(itemText)
            s.setSpan(ForegroundColorSpan(SearchView.getTextHighlightColor()), itemTextLower.indexOf(key.toString()), itemTextLower.indexOf(key.toString()) + key.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            viewHolder.text.setText(s, TextView.BufferType.SPANNABLE)
        } else {
            viewHolder.text.text = item.text
        }
    }

    override fun getItemCount(): Int {
        return resultsList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    // ---------------------------------------------------------------------------------------------
    override fun getFilter(): Filter {
        return object : Filter() {

            var mFilterKey: CharSequence = ""

            override fun performFiltering(constraint: CharSequence): Filter.FilterResults {
                val filterResults = Filter.FilterResults()

                key = constraint.toString().toLowerCase(Locale.getDefault())
                mFilterKey = key

                if (!TextUtils.isEmpty(constraint)) {
                    val results = ArrayList<SearchItem>()
                    val history = ArrayList<SearchItem>()
                    val databaseAllItems = mHistoryDatabase.getAllItems(mDatabaseKey)

                    if (!databaseAllItems.isEmpty()) {
                        history.addAll(databaseAllItems)
                    }
                    history.addAll(mSuggestions)

                    for (item in history) {
                        val string = item.text.toString().toLowerCase(Locale.getDefault())
                        if (string.contains(key)) {
                            results.add(item)
                        }
                    }

                    if (results.size > 0) {
                        filterResults.values = results
                        filterResults.count = results.size
                    }
                }

                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
                if (mFilterKey == key) {
                    var dataSet: MutableList<SearchItem> = ArrayList()

                    if (results.count > 0) {
                        val result = results.values as ArrayList<*>
                        for (`object` in result) {
                            if (`object` is SearchItem) {
                                dataSet.add(`object`)
                            }
                        }
                    } else {
                        if (TextUtils.isEmpty(key)) {
                            val allItems = mHistoryDatabase.getAllItems(mDatabaseKey)
                            if (!allItems.isEmpty()) {
                                dataSet = allItems
                            }
                        }
                    }

                    setData(dataSet)
                }
            }
        }
    }

    fun setDatabaseKey(key: Int?) {
        mDatabaseKey = key
        filter.filter("")
    }

    fun setData(data: List<SearchItem>) {
        if (resultsList.isEmpty()) {
            resultsList = data
            if (data.size != 0) {
                notifyItemRangeInserted(0, data.size)
            }
        } else {
            val previousSize = resultsList.size
            val nextSize = data.size
            resultsList = data
            if (previousSize == nextSize && nextSize != 0) {
                notifyItemRangeChanged(0, previousSize)
            } else if (previousSize > nextSize) {
                if (nextSize == 0) {
                    notifyItemRangeRemoved(0, previousSize)
                } else {
                    notifyItemRangeChanged(0, nextSize)
                    notifyItemRangeRemoved(nextSize - 1, previousSize)
                }
            } else {
                notifyItemRangeChanged(0, previousSize)
                notifyItemRangeInserted(previousSize, nextSize - previousSize)
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
        val icon: ImageView = view.findViewById(R.id.search_icon)
        val text: TextView = view.findViewById(R.id.search_text)

        init {
            // add second text
            view.setOnClickListener {
                if (mListener != null) {
                    mListener!!.onSearchItemClick(it, layoutPosition, text.text.toString())
                }
            }
        }
    }

}