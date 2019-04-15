package com.hour24.tb.view.viewholder

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import com.hour24.tb.R
import com.hour24.tb.const.DataConst
import com.hour24.tb.const.RequestConst
import com.hour24.tb.databinding.MainSearchBinding
import com.hour24.tb.model.DocumentItem
import com.hour24.tb.utils.Logger
import com.hour24.tb.utils.TextFormatUtils
import com.hour24.tb.view.activity.DetailActivity
import java.text.SimpleDateFormat
import java.util.*


class SearchViewHolder(private val mActivity: Activity,
                       private val mBinding: MainSearchBinding)
    : RecyclerView.ViewHolder(mBinding.root) {

    private val TAG = SearchViewHolder::class.java.name
    private val mView: View = mBinding.root


    init {

    }

    fun onBindView(item: DocumentItem, position: Int) {

        try {

            // 아이템 바인딩
            val viewModel = ViewModel()

            // position 삽입
            item.position = position
            viewModel.mModel = item

            mBinding.viewModel = viewModel

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    inner class ViewModel {

        var mModel: DocumentItem? = null

        fun onClick(v: View, model: DocumentItem) {
            when (v.id) {
                R.id.rl_search -> {
                    // 상세로 이동
                    val intent = Intent(mActivity, DetailActivity::class.java)
                    intent.putExtra(DocumentItem::class.java.name, model)
                    mActivity.startActivityForResult(intent, RequestConst.INTENT_DETAIL)
                }
            }
        }

        /**
         * 타이틀 추출
         */
        fun getName(model: DocumentItem): String {
            return TextFormatUtils.getName(model)
        }

        /**
         * yyyy-MM-dd'T'HH:mm:ssXXX 날짜 변경
         * 어제, 오늘, 그외 (YYYY년 MM월 DD일)
         */
        fun getDate(date: Date): String {
            return TextFormatUtils.getDate(mActivity, date)
        }


    }

}
