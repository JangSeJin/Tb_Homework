package com.hour24.tb.adapter

import android.app.Activity
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.NonNull
import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hour24.tb.R
import com.hour24.tb.const.Style
import com.hour24.tb.databinding.MainFilterBinding
import com.hour24.tb.databinding.MainSearchBinding
import com.hour24.tb.model.DocumentItem
import com.hour24.tb.view.activity.MainActivity
import com.hour24.tb.view.viewholder.FilterViewHolder
import com.hour24.tb.view.viewholder.SearchViewHolder
import java.util.*


/**
 * 메인 어뎁터
 */
class MainAdapter constructor(
        @NonNull private val mActivity: Activity,
        @NonNull private val mList: ArrayList<DocumentItem>?,
        @NonNull private val mFragmentManager: FragmentManager)
    : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private val TAG = MainAdapter::class.java.name

    init {
        setHasStableIds(true)
    }

    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {

        return if (position == 0) {
            Style.MAIN.FILTER
        } else {
            Style.MAIN.ITEM
        }

    }

    override fun getItemId(position: Int): Long {
        return mList?.get(position)!!.hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var layoutId: Int = R.layout.common_empty

        try {

            when (viewType) {

            // 필터
                Style.MAIN.FILTER ->
                    layoutId = R.layout.main_filter

            // 검색 아이템
                Style.MAIN.ITEM ->
                    layoutId = R.layout.main_search
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        val view = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(mActivity), layoutId, parent, false).root
        return ViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        try {

            val item: DocumentItem? = mList?.get(position)

            when (getItemViewType(position)) {

            // 필터
                Style.MAIN.FILTER ->
                    holder.filter!!.onBindView()

            // 검색 아이템
                Style.MAIN.ITEM ->
                    holder.searchItem!!.onBindView(item!!)

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    inner class ViewHolder constructor(
            private val mItemView: View?,
            private val viewType: Int?)
        : RecyclerView.ViewHolder(mItemView!!) {

        var binding: ViewDataBinding? = null

        var filter: FilterViewHolder? = null // 필터 영역
        var searchItem: SearchViewHolder? = null // 검색 아이템 영역

        init {

            binding = DataBindingUtil.bind(mItemView!!)

            when (viewType) {
                Style.MAIN.FILTER -> // 필터
                    filter = FilterViewHolder(mActivity as MainActivity, binding as MainFilterBinding)

                Style.MAIN.ITEM ->  // 식단
                    searchItem = SearchViewHolder(mActivity, binding as MainSearchBinding)
            }

        }
    }
}
