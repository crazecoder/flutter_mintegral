package com.crazecoder.flutter.mintegral.manager

import android.app.Activity
import com.crazecoder.flutter.mintegral.utils.LogUtil
import com.mbridge.msdk.MBridgeConstans
import com.mbridge.msdk.MBridgeConstans.NATIVE_VIDEO_SUPPORT
import com.mbridge.msdk.foundation.entity.CampaignEx
import com.mbridge.msdk.out.Campaign
import com.mbridge.msdk.out.Frame
import com.mbridge.msdk.out.MBBidNativeHandler
import com.mbridge.msdk.out.MBNativeHandler
import com.mbridge.msdk.out.NativeListener


class NativeAdManager(private val activity: Activity,private val onAdLoaded:(CampaignEx,MBNativeHandler?,MBBidNativeHandler?)-> Unit) : NativeListener.NativeAdListener,
    NativeListener.NativeTrackingListener {
    private var mbNativeHandler: MBNativeHandler? = null
    private var mbBidNativeHandler: MBBidNativeHandler? = null

    private var width:Int = 0
    private var height:Int = 0

    fun show(
        adUnitId: String,
        placementId: String,
        bidToken: String? = null,
        width:Int? = 0,
        height:Int? = 0
    ) {
        this.width = width?:0
        this.height = height?:0
        if (bidToken.isNullOrEmpty()) {
            val properties = MBNativeHandler.getNativeProperties(placementId, adUnitId)

            mbNativeHandler = MBNativeHandler(properties.apply {
                //期望获取的广告数量
//                properties.put(MBridgeConstans.PROPERTIES_AD_NUM, AD_NUM)
                //填写true即可
//                properties[MBridgeConstans.NATIVE_VIDEO_SUPPORT] = true
                if (width != 0 && height != 0) {
                    //如果想要获取原生广告视频时长，则至少添加如下两对Key之一
                    this[MBridgeConstans.NATIVE_VIDEO_WIDTH] = width
                    this[MBridgeConstans.NATIVE_VIDEO_HEIGHT] = height
                }
                LogUtil.i(this@NativeAdManager,this.toString())
            }, activity).apply {
                setAdListener(this@NativeAdManager)
                trackingListener = this@NativeAdManager
                load()
            }

        } else {
            val properties = MBBidNativeHandler.getNativeProperties(placementId, adUnitId)
            mbBidNativeHandler = MBBidNativeHandler(properties.apply {
                this[NATIVE_VIDEO_SUPPORT] = true
                if (width != 0 && height != 0) {
                    //如果想要获取原生广告视频时长，则至少添加如下两对Key之一
                    this[MBridgeConstans.NATIVE_VIDEO_WIDTH] = width
                    this[MBridgeConstans.NATIVE_VIDEO_HEIGHT] = height
                }
            }, activity).apply {
                setAdListener(this@NativeAdManager)
                trackingListener = this@NativeAdManager
                bidLoad(bidToken)
            }
        }

    }

    fun release(){
        mbBidNativeHandler?.bidRelease()
        mbNativeHandler?.release()
    }

    override fun onAdLoaded(list: List<Campaign?>?, i: Int) {
        /**
         * 广告加载成功时调用
         */
        if (!list.isNullOrEmpty()) {
            val campaign = list[0]!! as CampaignEx
            onAdLoaded(campaign,mbNativeHandler,mbBidNativeHandler)
        }
    }

    override fun onAdLoadError(s: String) {
        /**
         * 广告加载失败时调用
         */
        LogUtil.e(this, "onAdLoadError: $s")
    }

    override fun onAdClick(campaign: Campaign?) {
        /**
         * 广告点击时调用
         */
    }

    override fun onAdFramesLoaded(list: List<Frame?>?) {
        /**
         * 点击广告时调用（可以忽略）
         */
    }

    override fun onLoggingImpression(i: Int) {
        /**
         * 广告展示时调用
         */
    }

    override fun onInterceptDefaultLoadingDialog(): Boolean {
        /**
         * 截取显示默认的加载对话框
         */
        return false
    }

    override fun onShowLoading(campaign: Campaign?) {
        /**
         * 显示默认对话框时调用
         */
        LogUtil.i(this, "onShowLoading---")
    }

    override fun onDismissLoading(campaign: Campaign?) {
        /**
         * 默认对话框关闭时调用
         */
        LogUtil.i(this, "onDismissLoading---")
    }

    override fun onDownloadStart(campaign: Campaign?) {
        /**
         * 广告开始下载时调用
         */
        LogUtil.i(this, "onDownloadStart---")
    }

    override fun onDownloadFinish(campaign: Campaign?) {
        /**
         * 广告下载完成后调用
         */
        LogUtil.i(this, "onDownloadFinish---")
    }

    override fun onDownloadProgress(i: Int) {
        /**
         * 广告下载时调用
         */
    }

    override fun onStartRedirection(campaign: Campaign?, s: String) {
        /**
         * 广告开始重定向时调用
         */
        LogUtil.i(this, "onStartRedirection---$s")
    }

    override fun onFinishRedirection(campaign: Campaign?, s: String) {
        /**
         * 广告结束重定向时调用
         */
        LogUtil.i(this, "onFinishRedirection---$s")
    }

    override fun onRedirectionFailed(campaign: Campaign?, s: String) {
        /**
         * 广告完成重定向失败时调用
         */
        LogUtil.e(this, "onRedirectionFailed---$s")
    }
}