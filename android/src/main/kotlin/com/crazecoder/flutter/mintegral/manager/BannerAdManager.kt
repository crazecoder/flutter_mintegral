package com.crazecoder.flutter.mintegral.manager

import android.app.Activity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.crazecoder.flutter.mintegral.utils.LogUtil
import com.mbridge.msdk.out.BannerAdListener
import com.mbridge.msdk.out.BannerSize
import com.mbridge.msdk.out.MBBannerView
import com.mbridge.msdk.out.MBridgeIds

class BannerAdManager(private val activity: Activity, private val isFactory: Boolean = false) :
    BannerAdListener {

    private val cache: MutableMap<String, MBBannerView> = HashMap()
    private var unifiedBannerLayoutParams: ViewGroup.LayoutParams

    init {
        val screenWidth = activity.resources.displayMetrics.widthPixels
        unifiedBannerLayoutParams = LinearLayout.LayoutParams(
            screenWidth,
            Math.round(screenWidth / 6.4f)
        )
    }

    fun createBanner(
        placementId: String?,
        adUnitId: String,
        bannerSizeType: Int = BannerSize.SMART_TYPE,
        refreshTime: Int = 30,
        width: Int = 1294,
        height: Int = 720
    ): MBBannerView {
        val mtgBannerView = if (cache.containsKey(adUnitId) && cache[adUnitId] != null) {
            cache[adUnitId]!!
        } else {
            val view = MBBannerView(activity).apply {
                init(BannerSize(bannerSizeType, width, height), placementId, adUnitId)
                setRefreshTime(refreshTime)
                setAllowShowCloseBtn(true)
                setBannerAdListener(this@BannerAdManager)
            }
            cache[adUnitId] = view
            view
        }
        return mtgBannerView
    }

    fun show(
        placementId: String?,
        adUnitId: String,
        token: String? = null,
        bannerSizeType: Int?,
        refreshTime: Int?,
    ) {
        var bannerAd = cache[adUnitId]
        if (bannerAd == null) {
            bannerAd = createBanner(
                placementId,
                adUnitId,
                bannerSizeType ?: BannerSize.SMART_TYPE,
                refreshTime = refreshTime ?: 30
            )
        }
        if (token.isNullOrEmpty()) {
            bannerAd.load()
        } else {
            bannerAd.loadFromBid(token)
        }
        val content = LinearLayout(activity)
        content.orientation = LinearLayout.VERTICAL
        content.gravity = Gravity.BOTTOM
        content.addView(bannerAd, unifiedBannerLayoutParams)
        activity.addContentView(
            content,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }

    fun dispose(adUnitId: String) {
        val bannerAd = cache[adUnitId]
        if (bannerAd != null) {
            bannerAd.release()
            val contentView = bannerAd.parent as View
            if (contentView == null || contentView.parent !is ViewGroup) return

            val contentParent = (contentView.parent) as ViewGroup
            contentParent.removeView(contentView)
            cache.remove(adUnitId)
        }
    }

    fun release(adUnitId: String) {
        val bannerAd = cache[adUnitId]
        if (bannerAd != null) {
            bannerAd.release()
            cache.remove(adUnitId)
        }
    }

    override fun onLoadFailed(ids: MBridgeIds, msg: String) {
        LogUtil.e(this, "on load failed$msg")
    }

    override fun onLoadSuccessed(ids: MBridgeIds) {
        LogUtil.e(this, "on load successed")
    }

    override fun onClick(ids: MBridgeIds) {
        LogUtil.e(this, "onAdClick")
    }

    override fun onLeaveApp(ids: MBridgeIds) {
        LogUtil.e(this, "leave app")
    }

    override fun showFullScreen(ids: MBridgeIds) {
        LogUtil.e(this, "showFullScreen")
    }

    override fun closeFullScreen(ids: MBridgeIds) {
        LogUtil.e(this, "closeFullScreen")
    }

    override fun onLogImpression(ids: MBridgeIds) {
        LogUtil.e(this, "onLogImpression")
    }

    override fun onCloseBanner(ids: MBridgeIds) {
        LogUtil.e(this, "onCloseBanner")
        if (isFactory)
            release(adUnitId = ids.unitId)
        else
            dispose(ids.unitId)
    }
}