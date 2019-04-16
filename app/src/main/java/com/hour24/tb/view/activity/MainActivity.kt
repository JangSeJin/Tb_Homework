package com.hour24.tb.view.activity

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.ListPopupWindow
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView
import com.hour24.tb.R
import com.hour24.tb.adapter.MainAdapter
import com.hour24.tb.const.DataConst
import com.hour24.tb.databinding.MainActivityBinding
import com.hour24.tb.interfaces.Initialize
import com.hour24.tb.model.DocumentItem
import com.hour24.tb.model.SearchModel
import com.hour24.tb.retrofit.KakaoService
import com.hour24.tb.retrofit.RetrofitCall
import com.hour24.tb.retrofit.RetrofitRequest
import com.hour24.tb.room.AppDatabase
import com.hour24.tb.room.recent.Recent
import com.hour24.tb.utils.Logger
import com.hour24.tb.utils.ObjectUtils
import com.hour24.tb.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), Initialize {

    private val TAG: String = MainActivity::class.java.name

    private lateinit var mBinding: MainActivityBinding
    private lateinit var mViewModel: ViewModel

    // Array, Adapter, layout manager
    private lateinit var mAdapter: MainAdapter
    private var mLayoutManager: LinearLayoutManager = LinearLayoutManager(this)
    private val mList: ArrayList<DocumentItem> = ArrayList()

    // paging
    private var mCurPageNo: Int = 1 // 페이지 넘버
    private val mPageSize: Int = 10 // 가져올 아이템 갯수
    private var mIsLoading: Boolean = false
    private var mIsLast: Boolean = false // 마지막 페이지 여부
    private var mLastItemVisibleFlag: Boolean = false

    var mFilterType = DataConst.FILTER_ALL // 필터
    var mSortType = DataConst.SORT_TITLE // 정렬

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        Utils.getHashKey(this@MainActivity)

        initDataBinding()
        initLayout(null)
        initVariable()
        initEventListener()

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

        // manager
        mBinding.rvMain.layoutManager = mLayoutManager

    }

    /**
     * 기타 변수
     */
    override fun initVariable() {

        // Main Adapter
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

                        actionSearch(true)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    return@OnEditorActionListener true
                }

                false
            })

            // 최근검색 포커싱
            mBinding.etSearch.onFocusChangeListener = View.OnFocusChangeListener { v, b ->

                if (!b) {
                    return@OnFocusChangeListener
                }

                val loadData = @SuppressLint("StaticFieldLeak")
                object : AsyncTask<Unit, Unit, MutableList<Recent>>() {

                    override fun doInBackground(vararg params: Unit?): MutableList<Recent> {
                        return AppDatabase.getInstance(this@MainActivity).recentDao().selectAll()

                    }

                    override fun onPostExecute(result: MutableList<Recent>) {
                        super.onPostExecute(result)

                        val list = ArrayList<String>()
                        result.forEach({
                            list.add(it.search)
                        })

                        val popupWindow = ListPopupWindow(v.context)
                        popupWindow.anchorView = v
                        popupWindow.setAdapter(ArrayAdapter(v.context, R.layout.main_recent_item, list))
                        popupWindow.setOnItemClickListener { parent, view, menuPosition, id ->

                            try {
                                mBinding.etSearch.setText(list[menuPosition])

                                actionSearch(true)
                                popupWindow.dismiss()

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        popupWindow.show()
                    }
                }
                loadData.execute()

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 검색 프로세스
     */
    fun actionSearch(clear: Boolean) {

        try {

            // 포커스 해제
            mBinding.etSearch.clearFocus()

            // 리스트 클리어 여부
            if (clear) {
                mList.clear()
            }

            // 검색어 공백 체크
            val text = mBinding.etSearch.text.toString()
            if (ObjectUtils.isEmpty(text)) {
                Snackbar.make(mBinding.root, R.string.main_empty_text, Snackbar.LENGTH_SHORT).show()
                return
            }

            // 필터체크
            when (mFilterType) {

                DataConst.FILTER_BLOG, DataConst.FILTER_CAFE -> {
                    mViewModel.search(mFilterType, text)
                }

                else -> {
                    // 모든 필터 검색
                    mViewModel.search(DataConst.FILTER_BLOG, text)
                    mViewModel.search(DataConst.FILTER_CAFE, text)
                }
            }

            // 최근검색 저장
            AppDatabase.insert(text)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * ViewModel
     */
    inner class ViewModel {

        // 최근검색
        var mModel: Recent? = null

        fun onClick(v: View) {
            when (v.id) {
                R.id.iv_search -> {
                    // 검색버튼
                    actionSearch(true)
                }
            }
        }

        /**
         * 검색
         */
        fun search(filter: String, s: String) {

            val service = RetrofitRequest.createRetrofitJSONService(this@MainActivity, KakaoService::class.java, DataConst.HOST)
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

                                    // 필터 처리
                                    if (!ObjectUtils.isEmpty(model.blogname)) {
                                        model.filter = DataConst.FILTER_BLOG
                                    } else if (!ObjectUtils.isEmpty(model.cafename)) {
                                        model.filter = DataConst.FILTER_CAFE
                                    }

                                    // Date 처리
                                    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSX", Locale.KOREA)
                                    simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
                                    model.date = simpleDateFormat.parse(model.datetime) // api 에서 받아온 날짜

                                }

                                mList.addAll(list)

                                // 정렬
                                sort()

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
         * 정렬
         */
        fun sort() {
            try {

                if (mSortType == DataConst.SORT_TITLE) {
                    mList.sortBy { it.title }
                } else if (mSortType == DataConst.SORT_DATETIME) {
                    mList.sortBy { it.date }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        /**
         * RecyclerView.OnScrollListener
         */
        var mOnScrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastItemVisibleFlag) {
                    if (!mIsLoading && !mIsLast) {
                        actionSearch(false)
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
