package com.hour24.tb.view.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.hour24.tb.R
import com.hour24.tb.adapter.MainAdapter
import com.hour24.tb.const.APIConst
import com.hour24.tb.databinding.MainActivityBinding
import com.hour24.tb.interfaces.Initialize
import com.hour24.tb.model.DocumentItem
import com.hour24.tb.model.SearchModel
import com.hour24.tb.retrofit.KakaoService
import com.hour24.tb.retrofit.RetrofitCall
import com.hour24.tb.retrofit.RetrofitRequest
import com.hour24.tb.utils.Utils
import com.hour24.tb.utils.Logger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), Initialize {

    private val TAG: String = MainActivity::class.java.name

    private lateinit var mBinding: MainActivityBinding
    private lateinit var mViewModel: ViewModel

    // Array, Adapter, layout manager
    private val mList: ArrayList<DocumentItem> = ArrayList()
    private lateinit var mAdapter: MainAdapter
    private var mLayoutManager: LinearLayoutManager = LinearLayoutManager(this)

    // paging
    private var mCurPageNo: Int = 0 // 페이지 넘버
    private val mPageSize: Int = 10 // 가져올 아이템 갯수
    private var mIsLoading: Boolean = false
    private var mIsLast: Boolean = false // 마지막 페이지 여부
    private var mLastItemVisibleFlag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        Utils.getHashKey(this@MainActivity)

        initDataBinding()
        initLayout(null)
        initVariable()
        initEventListener()

        mViewModel.search(APIConst.TYPE_BLOG, "서")
    }

    /**
     * 데이터 바인딩
     */
    override fun initDataBinding() {

        mViewModel = ViewModel()
        mBinding.viewModel = mViewModel

    }

    /**
     * 기본적인 레이아웃
     */
    override fun initLayout(view: View?) {

        mBinding.rvMain.layoutManager = mLayoutManager

    }

    /**
     * 기타 변수
     */
    override fun initVariable() {

        mAdapter = MainAdapter(this, mList, supportFragmentManager)
        mBinding.rvMain.adapter = mAdapter

    }

    /**
     * 이벤트 리스너
     */
    override fun initEventListener() {

        try {

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    /**
     * ViewModel
     */
    inner class ViewModel {

        fun onClick(v: View) {
            when (v.id) {
                R.id.iv_search -> {
                    // 검색버튼
                    search(APIConst.TYPE_BLOG, mBinding.etSearch.text.toString())
                }
            }
        }

        /**
         * 자동완성 검색
         */
        fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            try {

                Logger.e(TAG, s.toString())
                search(APIConst.TYPE_BLOG, s.toString())

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        /**
         * 검색
         */
        public fun search(type: String, s: String) {

            val service = RetrofitRequest.createRetrofitJSONService(this@MainActivity, KakaoService::class.java, APIConst.HOST)
            val call = service.reqKakaoSearch(
                    type,
                    s,
                    "accuracy",
                    1,
                    25)

            RetrofitCall.enqueueWithRetry(call, object : Callback<SearchModel> {

                override fun onResponse(call: Call<SearchModel>, response: Response<SearchModel>) {

                    try {

                        if (response.isSuccessful) {
                            val resData = response.body()

                            if (resData != null) {
                                Logger.e(TAG, resData.toString())

                                mList.addAll(resData.documents)

                                mList.forEachIndexed { index, model ->
                                    model.type = type
                                }

                                mAdapter.notifyDataSetChanged()

                            }
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<SearchModel>, t: Throwable) {
                    Logger.e(TAG, "onFailure : " + t.message)
                }
            })
        }

        /**
         * RecyclerView.OnScrollListener
         */
        var mOnScrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastItemVisibleFlag) {
                    if (!mIsLoading && !mIsLast) {
//                        getData(false)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                if (!mIsLoading) {

                    val visibleItemCount = mLayoutManager.childCount
                    val totalItemCount = mLayoutManager.itemCount
                    val firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition()

                    mLastItemVisibleFlag = if (dy > 0) {
                        //현재 화면에 보이는 첫번째 리스트 아이템의 번호(firstVisibleItem) + 현재 화면에 보이는 리스트 아이템의 갯수(visibleItemCount)가 리스트 전체의 갯수(totalItemCount) -1 보다 크거나 같을때
                        totalItemCount > 0 && firstVisibleItem + visibleItemCount >= totalItemCount
                    } else {
                        // 전체 카운트랑 화면에 보여지는 카운트랑 같을때
                        visibleItemCount == totalItemCount
                    }

                }
            }

        }

    }

}
