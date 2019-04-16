package com.hour24.tb.view.activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.hour24.tb.R
import com.hour24.tb.databinding.DetailActivityBinding
import com.hour24.tb.interfaces.Initialize
import com.hour24.tb.model.DocumentItem
import com.hour24.tb.model.WebModel
import com.hour24.tb.room.AppDatabase
import com.hour24.tb.room.read.Read
import com.hour24.tb.utils.TextFormatUtils
import java.util.*

class DetailActivity : AppCompatActivity(), Initialize {

    private val TAG = DetailActivity::class.java.name

    private lateinit var mBinding: DetailActivityBinding
    private val mViewModel: ViewModel = ViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.detail_activity)

        initDataBinding()
        initLayout(null)
        initVariable()
        initEventListener()

    }


    override fun initDataBinding() {

        mBinding.viewModel = mViewModel

    }

    override fun initLayout(view: View?) {

    }

    override fun initVariable() {

        try {

            val intent = intent
            mViewModel.mModel.set(intent.getSerializableExtra(DocumentItem::class.java.name) as DocumentItem)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun initEventListener() {

    }

    /**
     * View Model
     */
    inner class ViewModel {

        val mModel: ObservableField<DocumentItem> = ObservableField()

        fun onClick(v: View) {

            when (v.id) {
                R.id.iv_back -> finish()
            }
        }

        fun onClick(v: View, model: DocumentItem) {

            when (v.id) {
                R.id.iv_web -> {
                    // 웹페이지 이동
                    val web = WebModel(model.title, model.url)

                    val intent = Intent(this@DetailActivity, WebActivity::class.java)
                    intent.putExtra(WebModel::class.java.name, web)
                    startActivity(intent)

                    // URL 저장
                    AsyncTask.execute({
                        AppDatabase.getInstance(this@DetailActivity).readDAO().insert(Read(model.url))
                    })
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
            return TextFormatUtils.getDate(this@DetailActivity, date)
        }
    }

}
