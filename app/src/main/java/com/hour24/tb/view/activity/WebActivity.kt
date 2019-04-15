package com.hour24.tb.view.activity

import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.hour24.tb.R
import com.hour24.tb.databinding.WebActivityBinding
import com.hour24.tb.interfaces.Initialize
import com.hour24.tb.interfaces.WebViewListener
import com.hour24.tb.model.WebModel

class WebActivity : AppCompatActivity(), Initialize, SwipeRefreshLayout.OnRefreshListener {

    private val TAG = WebActivity::class.java.name

    private lateinit var mBinding: WebActivityBinding
    private var mViewModel: ViewModel = ViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.web_activity)

        initDataBinding()
        initLayout(null)
        initVariable()
        initEventListener()

    }

    override fun initDataBinding() {
        mBinding.viewModel = mViewModel
    }

    override fun initLayout(view: View?) {

        // WebView init
        mBinding.webView.initView(this@WebActivity)

        // Refresh
        mBinding.refresh.setOnRefreshListener(this)

    }

    override fun initVariable() {

        try {

            val intent = intent
            mViewModel.mModel.set(intent.getSerializableExtra(WebModel::class.java.name) as WebModel)

            // load
            mBinding.webView.loadUrl(mViewModel.mModel.get()?.url)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onRefresh() {
        mBinding.webView.reload()
    }

    override fun initEventListener() {

        mBinding.webView.setOnPageStatusListener(object : WebViewListener.OnPageStatusListener {
            override fun onPageStarted(url: String) {
                mBinding.refresh.isRefreshing = true
            }

            override fun onPageFinished(url: String) {
                mBinding.refresh.isRefreshing = false
            }
        })

        mBinding.webView.setOnProgressChangedListener { progress ->
            if (progress == 100) {
                mBinding.refresh.isRefreshing = false
            }
        }
    }

    /**
     * View Model
     */
    inner class ViewModel {

        val mModel: ObservableField<WebModel> = ObservableField()

        fun onClick(v: View) {

            when (v.id) {
                R.id.iv_back -> finish()
            }
        }
    }

}
