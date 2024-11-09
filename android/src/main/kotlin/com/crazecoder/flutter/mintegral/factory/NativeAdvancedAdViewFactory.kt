package com.crazecoder.flutter.mintegral.factory

import android.app.Activity
import android.content.Context
import android.view.View
import com.crazecoder.flutter.mintegral.manager.MIntegralSDKManager
import com.crazecoder.flutter.mintegral.manager.NativeAdvancedAdManager
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory

class NativeAdvancedAdViewFactory(private val activity: Activity) :
    PlatformViewFactory(StandardMessageCodec.INSTANCE) {
    private val nativeAdvancedAdManager = NativeAdvancedAdManager(activity)

    override fun create(context: Context?, viewId: Int, args: Any?): PlatformView {
        val param = args as Map<String, Any>
        val placementId = param["placementId"]?.toString()
        val adUnitId = param["adUnitId"]?.toString()
        val width = param["width"] as Double?
        val height = param["height"] as Double?
        val styleJson = param["styleJson"]

        val view = nativeAdvancedAdManager.createAdView(
            placementId!!, adUnitId!!, width?.toInt(),
            height?.toInt(), styleJson?.toString()
        )
        if (param["isBidding"] == true) {
            MIntegralSDKManager().fetchBidToken(placementId, adUnitId) { token ->
                nativeAdvancedAdManager.show(token)
            }
        } else {
            nativeAdvancedAdManager.show()
        }

        return object : PlatformView {
            override fun getView(): View {
                return view
            }

            override fun dispose() {
                nativeAdvancedAdManager.onDestroy()
            }

        }
    }
}