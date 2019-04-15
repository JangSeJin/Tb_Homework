package com.hour24.tb.view.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
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
import com.hour24.tb.utils.ObjectUtils
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
    private var mCurPageNo: Int = 1 // 페이지 넘버
    private val mPageSize: Int = 25 // 가져올 아이템 갯수
    private var mIsLoading: Boolean = false
    private var mIsLast: Boolean = false // 마지막 페이지 여부
    private var mLastItemVisibleFlag: Boolean = false

    var mFilterType = APIConst.FILTER_ALL // 필터

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        Utils.getHashKey(this@MainActivity)

        initDataBinding()
        initLayout(null)
        initVariable()
        initEventListener()

        mBinding.etSearch.setText("강남")
        checkFilter(true)
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

            mBinding.etSearch.imeOptions = EditorInfo.IME_ACTION_SEARCH
            mBinding.etSearch.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    try {

                        val text = v.text.toString()
                        if (!ObjectUtils.isEmpty(text)) {
                            checkFilter(true)
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    return@OnEditorActionListener true
                }

                false
            })

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 검색하기전 필터 체크
     */
    public fun checkFilter(clear: Boolean) {

        if (clear) {
            mList.clear()
        }

        val text = mBinding.etSearch.text.toString()

        when (mFilterType) {

            APIConst.FILTER_BLOG, APIConst.FILTER_CAFE -> {
                mViewModel.search(mFilterType, text)
            }

            else -> {
                // 모든 필터 검색
                mViewModel.search(APIConst.FILTER_BLOG, text)
                mViewModel.search(APIConst.FILTER_CAFE, text)
            }
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
                    checkFilter(true)
                }
            }
        }

//        /**
//         * 자동완성 검색
//         */
//        fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//
//            try {
//
//                Logger.e(TAG, s.toString())
//                search(APIConst.TYPE_BLOG, s.toString())
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//
//        }

        /**
         * 검색
         */
        public fun search(filter: String, s: String) {

            val service = RetrofitRequest.createRetrofitJSONService(this@MainActivity, KakaoService::class.java, APIConst.HOST)
            val call = service.reqKakaoSearch(
                    filter.toLowerCase(),
                    s,
                    "accuracy",
                    mCurPageNo,
                    mPageSize)

            RetrofitCall.enqueueWithRetry(call, object : Callback<SearchModel> {

                override fun onResponse(call: Call<SearchModel>, response: Response<SearchModel>) {

                    try {

                        if (response.isSuccessful) {
                            val resData = response.body()

                            if (resData != null) {

                                Logger.e(TAG, resData.toString())

                                val list = resData.documents
                                list.forEachIndexed { index, model ->

                                    if (!ObjectUtils.isEmpty(model.blogname)) {
                                        model.filter = APIConst.FILTER_BLOG
                                    }

                                    if (!ObjectUtils.isEmpty(model.cafename)) {
                                        model.filter = APIConst.FILTER_CAFE
                                    }

                                }

                                mList.addAll(list)

                                // 정렬
                                mList.sortBy { it.title }

                                mAdapter.notifyDataSetChanged()

                                mCurPageNo++

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
                        checkFilter(false)
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
