package com.crazecoder.flutter.mintegral.manager

import android.app.Activity
import com.crazecoder.flutter.mintegral.utils.LogUtil
import com.mbridge.msdk.MBridgeConstans
import com.mbridge.msdk.MBridgeSDK
import com.mbridge.msdk.mbbid.out.BidListennning
import com.mbridge.msdk.mbbid.out.BidManager
import com.mbridge.msdk.mbbid.out.BidResponsed
import com.mbridge.msdk.mbbid.out.CommonBidRequestParams
import com.mbridge.msdk.out.MBridgeSDKFactory
import com.mbridge.msdk.out.SDKInitStatusListener
import io.flutter.BuildConfig


/**
 * Mintegral Android SDK Manager
 */
class MIntegralSDKManager {
    fun init(
        activity: Activity,
        appId: String?,
        appKey: String?,
        isProtectGDPR: Boolean,
        isProtectCCPA: Boolean
    ) {
        MBridgeConstans.DEVELOPER_CUSTOM_PACKAGE = activity.packageName
        val sdk: MBridgeSDK = MBridgeSDKFactory.getMBridgeSDK()
        val map = sdk.getMBConfigurationMap(appId, appKey, BuildConfig.DEBUG).apply {
//            if (this.containsKey(MBridgeConstans.KEY_WORD)){
//                this[MBridgeConstans.KEY_WORD] = "vpn"
//            }else{
//                put(MBridgeConstans.KEY_WORD, "vpn")
//            }
        }
        sdk.setConsentStatus(
            activity,
            if (isProtectGDPR) MBridgeConstans.IS_SWITCH_OFF else MBridgeConstans.IS_SWITCH_ON
        )
        sdk.init(map, activity)
        sdk.init(map, activity.application, object : SDKInitStatusListener {
            override fun onInitSuccess() {
                LogUtil.e("SDKInitStatus", "onInitSuccess")
            }

            override fun onInitFail(msg: String) {
                LogUtil.e("SDKInitStatus", "onInitFail:$msg")
            }
        })
        sdk.setDoNotTrackStatus(activity, isProtectCCPA)
    }

    fun fetchBidToken(params: CommonBidRequestParams, onToken: (String) -> Unit) {
        val manager = BidManager(params)
        manager.setBidListener(object : BidListennning {
            override fun onFailed(msg: String) {
                LogUtil.e(this@MIntegralSDKManager, msg)
            }

            override fun onSuccessed(bidResponsed: BidResponsed) {
                onToken.invoke(bidResponsed.bidToken)
            }
        })
        manager.bid()
    }

    fun fetchBidToken(placementId: String, adUnitId: String, onToken: (String) -> Unit) {
        val manager = BidManager(placementId, adUnitId)
        manager.setBidListener(object : BidListennning {
            override fun onFailed(msg: String) {
                LogUtil.e(this, msg)
            }

            override fun onSuccessed(bidResponsed: BidResponsed) {
                onToken.invoke(bidResponsed.bidToken)
            }
        })
        manager.bid()
    }

    fun preload(
        adUnitId: String,
        placementId: String,
        layoutType: Int?
    ) {
        val sdk: MBridgeSDK = MBridgeSDKFactory.getMBridgeSDK()
        val preloadMap: MutableMap<String, Any> = HashMap()
        layoutType?.let {
            preloadMap[MBridgeConstans.PROPERTIES_LAYOUT_TYPE] = it
        }
        preloadMap[MBridgeConstans.PROPERTIES_UNIT_ID] = adUnitId
        preloadMap[MBridgeConstans.PROPERTIES_AD_NUM] = 1
        preloadMap[MBridgeConstans.PLACEMENT_ID] = placementId
        sdk.preload(preloadMap)
    }
}
