package com.hour24.tb.view.viewholder

import android.databinding.ObservableField
import android.support.v7.widget.ListPopupWindow
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ArrayAdapter
import com.hour24.tb.R
import com.hour24.tb.const.APIConst
import com.hour24.tb.databinding.MainFilterBinding
import com.hour24.tb.interfaces.Initialize
import com.hour24.tb.utils.Utils
import com.hour24.tb.view.activity.MainActivity


class FilterViewHolder(private val mActivity: MainActivity,
                       private val mBinding: MainFilterBinding)
    : RecyclerView.ViewHolder(mBinding.root), Initialize {

    public val TAG = FilterViewHolder::class.java.name
    private val mView: View = mBinding.root
    private val mViewModel: ViewModel = ViewModel()

    private val mTypeList = ArrayList<String>()

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

        // 초기값
        mViewModel.mFilter.set(APIConst.FILTER_ALL)

        // list box에 보여줄 데이터 미리 세팅
        mTypeList.add(APIConst.FILTER_ALL)
        mTypeList.add(APIConst.FILTER_BLOG)
        mTypeList.add(APIConst.FILTER_CAFE)

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

            try {

                when (v.id) {

                    R.id.tv_filter -> { // 필터 클릭

                        mActivity.runOnUiThread(Runnable {

                            val popupWindow = ListPopupWindow(v.context)
                            popupWindow.width = Utils.getDpFromPx(v.context, 400.toFloat()).toInt()
                            popupWindow.anchorView = v
                            popupWindow.setAdapter(ArrayAdapter(v.context, R.layout.main_filter_item, mTypeList))

                            popupWindow.setOnItemClickListener { parent, view, menuPosition, id ->

                                try {

                                    val filter: String = mTypeList[menuPosition]
                                    mFilter.set(filter)
                                    mActivity.mFilterType = filter
                                    mActivity.checkFilter(true)

                                    popupWindow.dismiss()

                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }

                            popupWindow.show()

                        })

                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }

}
