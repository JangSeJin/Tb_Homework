package com.hour24.tb.view.viewholder

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import com.hour24.tb.R
import com.hour24.tb.const.DataConst
import com.hour24.tb.databinding.MainSearchBinding
import com.hour24.tb.model.DocumentItem
import com.hour24.tb.utils.Logger
import java.text.SimpleDateFormat
import java.util.*


class SearchViewHolder(private val mActivity: Activity,
                       private val mBinding: MainSearchBinding)
    : RecyclerView.ViewHolder(mBinding.root) {

    private val TAG = SearchViewHolder::class.java.name
    private val mView: View = mBinding.root


    init {

    }

    fun onBindView(item: DocumentItem) {

        try {

            // 아이템 바인딩
            val viewModel = ViewModel()
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
//                R.id.ll_board, R.id.ll_comment -> {
//                    // 게시글 상세로 이동
//
//                    // 현재 list position 삽입
//                    model.position = mPosition
//
//                    val intent = Intent(mActivity, VideoDetailActivity::class.java)
//                    intent.putExtra(BoardItem::class.java.name, model)
//                    mActivity.startActivityForResult(intent, RequestConst.INTENT_DETAIL)
//
//                }
            }
        }

        /**
         * 타이틀 추출
         */
        fun getName(model: DocumentItem): String {

            try {

                Logger.e(TAG, model.filter)

                return if (model.filter == DataConst.FILTER_BLOG) {
                    model.blogname
                } else {
                    model.cafename
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return ""
        }

        /**
         * yyyy-MM-dd'T'HH:mm:ssXXX 날짜 변경
         * 어제, 오늘, 그외 (YYYY년 MM월 DD일)
         */
        fun getDate(date: Date): String {

            try {

                val nowDate: Date = Date() // 현재

                // 날자비교
                val calDate = nowDate.time - date.time
                val calDateDays = (calDate / (24 * 60 * 60 * 1000)).toInt()

                return when (calDateDays) {
                    0 -> mActivity.getString(R.string.main_today)
                    1 -> mActivity.getString(R.string.main_yesterday)
                    else -> {
                        val format = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
                        format.format(date)
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            return ""
        }


    }

}
