package com.hour24.tb.utils

import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

object ImageLoadUtils {

    private val TAG = ImageLoadUtils::class.java.name

    /**
     * @param view ImageView
     * @param url  이미지 주소
     * @author 장세진
     * @description 단순 이미지 로드
     */
    @JvmStatic
    @BindingAdapter("loadImage")
    fun setLoadImage(view: ImageView, url: Any?) {
        try {
            loadImage(view, url)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * @param view ImageView
     * @param url  이미지 주소
     * @author 장세진
     * @description 단순 이미지 로드
     */
    @JvmStatic
    @BindingAdapter("loadImage")
    fun setLoadImage(view: ImageView, url: Int?) {
        try {
            loadImage(view, url)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * @param view ImageView
     * @param url  이미지 주소
     * @author 장세진
     * @description 단순 이미지 로드
     */
    @JvmStatic
    private fun loadImage(view: ImageView, url: Any?) {

        try {

            if (ObjectUtils.isEmpty(url)) {
                return
            }

            view.post(Runnable {

                Glide.with(view)
                        .load(url)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                                Logger.e(TAG, e!!.message + " / " + url)
                                return false
                            }

                            override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                                return false
                            }
                        })
                        .into(view)

            })

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * width, height, uri 이미지 로더
     *
     * @param view
     * @param uri
     * @param width
     * @param height
     */
    @JvmStatic
    @BindingAdapter("loadImage", "width", "height")
    fun setLoadImage(view: ImageView, uri: Uri, width: Float, height: Float) {
        loadImage(view, uri, width, height)
    }

    /**
     * width, height, uri 이미지 로더
     *
     * @param view
     * @param url
     * @param width
     * @param height
     */
    @JvmStatic
    @BindingAdapter("loadImage", "width", "height")
    fun setLoadImage(view: ImageView, url: String, width: Float, height: Float) {
        loadImage(view, url, width, height)
    }

    /**
     * @param view
     * @param url
     * @param width
     * @param height
     */
    private fun loadImage(view: ImageView, url: Any?, width: Float, height: Float) {


        try {

            if (ObjectUtils.isEmpty(url)) {
                return
            }

            Glide.with(view)
                    .load(url)
                    .apply(RequestOptions()
                            .override(width.toInt(), height.toInt())
                            .centerCrop())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                            Logger.e(TAG, e!!.message + " / " + url)
                            return false
                        }

                        override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                            return false
                        }
                    })
                    .into(view)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
