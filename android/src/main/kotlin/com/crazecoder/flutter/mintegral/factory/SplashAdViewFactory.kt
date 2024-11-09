package com.crazecoder.flutter.mintegral.factory

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import com.crazecoder.flutter.mintegral.manager.MIntegralSDKManager
import com.crazecoder.flutter.mintegral.manager.SplashAdManager
import com.mbridge.msdk.mbbid.out.SplashBidRequestParams
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory


class SplashAdViewFactory(private val messenger: BinaryMessenger, private val activity: Activity):PlatformViewFactory(StandardMessageCodec.INSTANCE) {

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun create(context: Context?, viewId: Int, args: Any?): PlatformView {
        val param = args as Map<String, Any>
        val placementId = param["placementId"]?.toString()
        val adUnitId = param["adUnitId"]?.toString()
        val channelName = "splash_$adUnitId"

        val methodChannel = MethodChannel(messenger, channelName)
        val view = FrameLayout(activity)
        view.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val splashAdManager = SplashAdManager(activity, view, onLoadSuccessed = {
            methodChannel.invokeMethod("onLoadSuccessed",null)
        }){
            methodChannel.invokeMethod("onDismissAndFailed",null)
        }
        if (param["isBidding"]==true){
            val params = SplashBidRequestParams(placementId, adUnitId, true, 1, 200, 200)
            MIntegralSDKManager().fetchBidToken(params) { token ->
                splashAdManager.show(placementId, adUnitId, token)
            }
        }else{
            splashAdManager.show(placementId,adUnitId)
        }
//        activity.registerActivityLifecycleCallbacks(object :
//            Application.ActivityLifecycleCallbacks {
//            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
//            }
//
//            override fun onActivityStarted(activity: Activity) {
//            }
//
//            override fun onActivityResumed(activity: Activity) {
//                splashAdManager.onResume()
//            }
//
//            override fun onActivityPaused(activity: Activity) {
//               splashAdManager.onPause()
//            }
//
//            override fun onActivityStopped(activity: Activity) {
//                splashAdManager.onPause()
//            }
//
//            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
//            }
//
//            override fun onActivityDestroyed(activity: Activity) {
//                splashAdManager.onDestroy()
//            }
//
//        })
        return object :PlatformView{
            override fun getView(): View? {
                if (view.parent != null) {
                    (view.parent as ViewGroup).removeView(view)
                }
                return view
            }

            override fun dispose() {
                splashAdManager.onDestroy()
            }

        }
    }
}