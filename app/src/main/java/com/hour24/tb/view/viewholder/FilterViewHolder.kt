package com.hour24.tb.view.viewholder

import android.databinding.ObservableField
import android.support.v7.widget.ListPopupWindow
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ArrayAdapter
import com.hour24.tb.R
import com.hour24.tb.const.DataConst
import com.hour24.tb.databinding.MainFilterBinding
import com.hour24.tb.interfaces.Initialize
import com.hour24.tb.utils.Utils
import com.hour24.tb.view.activity.MainActivity
import android.support.v7.app.AlertDialog
import com.hour24.tb.utils.Logger
import com.hour24.tb.utils.ObjectUtils


class FilterViewHolder(private val mActivity: MainActivity,
                       private val mBinding: MainFilterBinding)
    : RecyclerView.ViewHolder(mBinding.root), Initialize {

    public val TAG = FilterViewHolder::class.java.name
    private val mView: View = mBinding.root
    private val mViewModel: ViewModel = ViewModel()

    private val mFilterList = ArrayList<String>()
    private var mSortList = ArrayList<String>()

    init {

        initDataBinding()
        initLayout(mView)
        initVariable()
        initEventListener()

    }

    override fun initDataBinding() {

    }

    override fun initLayout(view: View?) {

    }

    override fun initVariable() {

        // 필터
        mViewModel.mFilter.set(DataConst.FILTER_ALL) // 초기값
        mFilterList.add(DataConst.FILTER_ALL)
        mFilterList.add(DataConst.FILTER_BLOG)
        mFilterList.add(DataConst.FILTER_CAFE)

        // 정렬
        mSortList.add(DataConst.SORT_TITLE)
        mSortList.add(DataConst.SORT_DATETIME)

    }

    override fun initEventListener() {

    }

    fun onBindView() {

        try {

            mBinding.viewModel = mViewModel

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    inner class ViewModel {

        public val mFilter: ObservableField<String> = ObservableField()

        fun onClick(v: View) {

            mActivity.runOnUiThread({

                try {

                    when (v.id) {

                        R.id.tv_filter -> { // 필터 클릭


                            val popupWindow = ListPopupWindow(v.context)
                            popupWindow.anchorView = v
                            popupWindow.setAdapter(ArrayAdapter(v.context, R.layout.main_filter_item, mFilterList))

                            popupWindow.setOnItemClickListener { parent, view, menuPosition, id ->

                                try {

                                    val filter: String = mFilterList[menuPosition]
                                    mFilter.set(filter)

                                    // 재검색
                                    mActivity.mFilterType = filter
                                    mActivity.actionSearch(true)

                                    popupWindow.dismiss()

                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }

                            popupWindow.show()

                        }

                        R.id.iv_sort -> { // 정렬 클릭

                            var tempSort: String? = null // 정렬 임시값

                            val listItems = arrayOf(DataConst.SORT_TITLE, DataConst.SORT_DATETIME)
                            val checkedItem =
                                    if (mActivity.mSortType == DataConst.SORT_TITLE) {
                                        0
                                    } else {
                                        1
                                    }

                            val builder =
                                    AlertDialog.Builder(mActivity)
                                            .setTitle(mActivity.getString(R.string.main_sort_title))
                                            .setSingleChoiceItems(listItems, checkedItem) { dialog, which ->
                                                // Radio 클릭
                                                tempSort = mSortList[which]
                                            }
                                            .setPositiveButton(mActivity.getString(R.string.main_sort_close)) { dialog, which ->
                                                dialog.dismiss()
                                            }
                                            .setNegativeButton(mActivity.getString(R.string.main_sort_adjust)) { dialog, which ->

                                                // 재검색
                                                if (!ObjectUtils.isEmpty(tempSort)) {
                                                    mActivity.mSortType = tempSort!!
                                                    mActivity.actionSearch(true)

                                                    dialog.dismiss()
                                                }

                                            }

                            val dialog = builder.create()
                            dialog.show()


                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            })


        }

    }

}
