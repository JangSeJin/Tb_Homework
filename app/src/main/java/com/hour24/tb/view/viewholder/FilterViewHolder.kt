package com.hour24.tb.view.viewholder

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import com.hour24.tb.databinding.MainFilterBinding
import com.hour24.tb.interfaces.Initialize
import com.hour24.tb.model.SearchModel


class FilterViewHolder(private val mActivity: Activity,
                       private val mBinding: MainFilterBinding)
    : RecyclerView.ViewHolder(mBinding.root), Initialize {

    public val TAG = FilterViewHolder::class.java.name
    private val mView: View = mBinding.root

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

    }

    override fun initEventListener() {

    }

    fun onBindView() {

        try {

            val viewModel = ViewModel()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    inner class ViewModel {


    }

}
