package com.crazecoder.flutter.mintegral.factory

import android.app.Activity
import android.content.Context
import android.view.View
import com.crazecoder.flutter.mintegral.manager.BannerAdManager
import com.crazecoder.flutter.mintegral.manager.MIntegralSDKManager
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory

class BannerViewFactory(private val activity: Activity) :
    PlatformViewFactory(StandardMessageCodec.INSTANCE) {
    private val bannerAdManager = BannerAdManager(activity,true)
    override fun create(context: Context?, viewId: Int, args: Any?): PlatformView {
        val param = args as Map<String, Any>

        val placementId = param["placementId"]?.toString()

        val adUnitId = param["adUnitId"]?.toString()
        val bannerSizeType = param["bannerSizeType"]
        val width = param["width"]
        val height = param["height"]

        val bannerView = bannerAdManager.createBanner(
            placementId,
            adUnitId!!,
            bannerSizeType = bannerSizeType as Int,
            width = (width as Double).toInt(),
            height = (height as Double).toInt()
        )

        if (param["isBidding"] == true) {
            MIntegralSDKManager().fetchBidToken(placementId!!, adUnitId) { token ->
                bannerView.loadFromBid(token)
            }
        } else {
            bannerView.load()
        }

        return object : PlatformView {
            override fun getView(): View {
                return bannerView
            }

            override fun dispose() {
                bannerAdManager.release(adUnitId)
            }
        }
    }
}