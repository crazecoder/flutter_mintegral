package com.crazecoder.flutter.mintegral

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.crazecoder.flutter.mintegral.activity.SplashAdActivity
import com.crazecoder.flutter.mintegral.manager.BannerAdManager
import com.crazecoder.flutter.mintegral.manager.MIntegralSDKManager
import com.crazecoder.flutter.mintegral.manager.NewInterstitialAdManager
import com.crazecoder.flutter.mintegral.manager.RewardVideoAdManager
import com.crazecoder.flutter.mintegral.factory.BannerViewFactory
import com.crazecoder.flutter.mintegral.factory.NativeAdViewFactory
import com.crazecoder.flutter.mintegral.factory.NativeAdvancedAdViewFactory
import com.crazecoder.flutter.mintegral.factory.SplashAdViewFactory
import com.mbridge.msdk.mbbid.out.BannerBidRequestParams
import com.mbridge.msdk.mbbid.out.SplashBidRequestParams
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.FlutterPlugin.FlutterPluginBinding
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler

/**
 * FlutterMintegralPlugin
 */
class FlutterMintegralPlugin : FlutterPlugin, MethodCallHandler,
    ActivityAware {
    /** The MethodChannel that will the communication between Flutter and native Android
     *
     * This local reference serves to register the plugin with the Flutter Engine and unregister it
     * when the Flutter Engine is detached from the Activity */
    private var context: Context? = null
    private var activity: Activity? = null
    private var channel: MethodChannel? = null
    private var flutterPluginBinding: FlutterPluginBinding? = null
    private val sdkManager = MIntegralSDKManager()
    private var bannerAdManager: BannerAdManager? = null
    private var rewardVideoAdManager: RewardVideoAdManager? = null
    private var newInterstitialAdManager: NewInterstitialAdManager? = null


    override fun onAttachedToEngine(flutterPluginBinding: FlutterPluginBinding) {
        this.flutterPluginBinding = flutterPluginBinding
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        if (call.method == "initAdSDK") {
            if (call.hasArgument("appId") && call.hasArgument("appKey")) {
                val appId = call.argument<String>("appId")
                val appKey = call.argument<String>("appKey")
                val isProtectGDPR = call.argument<Boolean>("isProtectGDPR")!!
                val isProtectCCPA = call.argument<Boolean>("isProtectCCPA")!!
                sdkManager.init(activity!!, appId, appKey, isProtectGDPR, isProtectCCPA)
            }
            result.success(null)
        } else if (call.method == "preload") {
            val adUnitId = call.argument<Any>("adUnitId").toString()
            val placementId = call.argument<Any>("placementId").toString()
            val layoutType = call.argument<Int?>("layoutType")
            sdkManager.preload(adUnitId, placementId, layoutType)
            result.success(null)
        } else if (call.method == "startSplashAd") {
            val adUnitId = call.argument<Any>("adUnitId").toString()
            val placementId = call.argument<Any>("placementId").toString()
            val launchBackgroundIdStr = call.argument<String>("launchBackgroundId")
            val isBidding = call.argument<Boolean>("isBidding")!!
            val launchBackgroundId: Int
            if (TextUtils.isEmpty(launchBackgroundIdStr)) {
                launchBackgroundId = -1
            } else {
                val res = activity!!.resources
                launchBackgroundId =
                    res.getIdentifier(launchBackgroundIdStr, "drawable", activity!!.packageName)
            }
            val intent = Intent(activity, SplashAdActivity::class.java)
            intent.putExtra("adUnitId", adUnitId)
            intent.putExtra("placementId", placementId)
            intent.putExtra("launchBackgroundId", launchBackgroundId)
            if (isBidding) {
                val params = SplashBidRequestParams(placementId, adUnitId, true, 1, 200, 200)
                sdkManager.fetchBidToken(params) { token ->
                    intent.putExtra("biddingToken", token)
                    activity!!.startActivity(intent)
                    result.success(null)
                }
            } else {
                activity!!.startActivity(intent)
                result.success(null)
            }

        } else if (call.method == "showBannerAD") {
            if (bannerAdManager == null) bannerAdManager = BannerAdManager(activity!!)
            val adUnitId = call.argument<Any>("adUnitId").toString()
            val placementId = call.argument<Any>("placementId").toString()
            val isBidding = call.argument<Boolean>("isBidding")
            val bannerSizeType = call.argument<Int>("bannerSizeType")
            val refreshTime = call.argument<Int>("refreshTime")
            if (isBidding == true) {
                val params = BannerBidRequestParams(placementId, adUnitId, 1280, 80)
                sdkManager.fetchBidToken(params) { token ->
                    bannerAdManager?.show(placementId, adUnitId, token,bannerSizeType,refreshTime = refreshTime)
                    result.success(null)
                }
                return
            }
            bannerAdManager?.show(placementId, adUnitId,bannerSizeType=bannerSizeType,refreshTime = refreshTime)
            result.success(null)
        } else if (call.method == "disposeBannerAD") {
            val adUnitId = call.argument<Any>("adUnitId").toString()
            bannerAdManager?.dispose(adUnitId)
            result.success(null)
        } else if (call.method == "showNativeAdvancedAD") {
            val adUnitId = call.argument<Any>("adUnitId").toString()
            val placementId = call.argument<Any>("placementId").toString()

            result.success(null)
        } else if (call.method == "showRewardVideoAD") {
            val adUnitId = call.argument<Any>("adUnitId").toString()
            val placementId = call.argument<Any>("placementId").toString()
            val userId = call.argument<String>("userId")
            val extraData = call.argument<String>("extraData")
            val isBidding = call.argument<Boolean>("isBidding")
            val isJustPlus = call.argument<Boolean>("isJustPlus")
            if (rewardVideoAdManager == null)
                rewardVideoAdManager = RewardVideoAdManager(activity!!, userId, extraData)
            if (isBidding == true) {
                sdkManager.fetchBidToken(placementId,adUnitId){token->
                    rewardVideoAdManager?.show(adUnitId, placementId,token,isJustPlus)
                }
            }else{
                rewardVideoAdManager?.show(adUnitId, placementId, isJustPlus = isJustPlus)
            }
            result.success(null)
        } else if (call.method == "showNewInterstitialAD") {
            val adUnitId = call.argument<Any>("adUnitId").toString()
            val placementId = call.argument<Any>("placementId").toString()
            val isBidding = call.argument<Boolean>("isBidding")
            if (newInterstitialAdManager == null)
                newInterstitialAdManager = NewInterstitialAdManager(activity!!)
            if (isBidding == true) {
                sdkManager.fetchBidToken(placementId,adUnitId){token->
                    newInterstitialAdManager?.loadAd(adUnitId, placementId,token)
                }
            }else{
                newInterstitialAdManager?.loadAd(adUnitId, placementId)
            }
            result.success(null)
        }else {
            result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(binding: FlutterPluginBinding) {
        channel!!.setMethodCallHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
        channel =
            MethodChannel(flutterPluginBinding!!.binaryMessenger, "flutter_mintegral")
        flutterPluginBinding?.platformViewRegistry?.registerViewFactory("m-banner",BannerViewFactory(activity!!))
        flutterPluginBinding?.platformViewRegistry?.registerViewFactory("m-native-advanced-ad",NativeAdvancedAdViewFactory(activity!!))
        flutterPluginBinding?.platformViewRegistry?.registerViewFactory("m-native-ad",NativeAdViewFactory(activity!!))
        flutterPluginBinding?.platformViewRegistry?.registerViewFactory("m-splash-ad",SplashAdViewFactory(flutterPluginBinding!!.binaryMessenger,activity!!))

        channel!!.setMethodCallHandler(this)
        context = flutterPluginBinding?.applicationContext
    }

    override fun onDetachedFromActivityForConfigChanges() {
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    }

    override fun onDetachedFromActivity() {
        flutterPluginBinding = null
    }

}
