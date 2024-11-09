package com.crazecoder.flutter.mintegral.activity

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.annotation.DrawableRes
import com.crazecoder.flutter.mintegral.R
import com.crazecoder.flutter.mintegral.manager.MIntegralSDKManager
import com.crazecoder.flutter.mintegral.manager.SplashAdManager
import com.mbridge.msdk.mbbid.out.SplashBidRequestParams
import com.mbridge.msdk.out.MBSplashHandler
import com.mbridge.msdk.out.MBSplashLoadListener
import com.mbridge.msdk.out.MBSplashShowListener
import com.mbridge.msdk.out.MBridgeIds
import io.flutter.Log

abstract class SplashAdAbstractActivity : Activity() {
    private var adUnitId: String? = null
    private var appId: String? = null
    private var appKey: String? = null
    private var placementId: String? = null
    private var biddingToken: String? = null


    private lateinit var adContainer: View
    private lateinit var adManager: SplashAdManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        //隐去状态栏部分（电池等图标和一起修饰部分）
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )
        setContentView(R.layout.splash_ad)
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            String processName = getProcessName();
//            String packageName = getPackageName();
//            if (!packageName.equals(processName) ) {
//                WebView.setDataDirectorySuffix(processName);
//            }
//        }
        adContainer = this.findViewById(R.id.ad_container)

        appId = intent.getStringExtra("appId")
        appKey = intent.getStringExtra("appKey")
        adUnitId = intent.getStringExtra("adUnitId")
        placementId = intent.getStringExtra("placementId")
        biddingToken = intent.getStringExtra("biddingToken")

        var launchBackgroundId = intent.getIntExtra("launchBackgroundId", -1)
        if (launchBackground != null) {
            launchBackgroundId = launchBackground!!
        }
        if (launchBackgroundId != -1) {
            adContainer.setBackground(
                resources.getDrawable(
                    launchBackgroundId
                )
            )
        }
        if (TextUtils.isEmpty(appId)) {
            appId = getAppId()
        }
        if (TextUtils.isEmpty(placementId)) {
            placementId = adPlacementId
        }
        if (TextUtils.isEmpty(appKey)) {
            appKey = getAppKey()
        }
        if (TextUtils.isEmpty(adUnitId)) {
            adUnitId = getAdUnitId()
        }

        adManager = SplashAdManager(this, adContainer = adContainer){
            finish()
        }
        if (isBidding() == true && biddingToken.isNullOrEmpty()) {
            val params = SplashBidRequestParams(placementId, adUnitId, true, 1, 200, 200)
            MIntegralSDKManager().fetchBidToken(params) { token ->
                adManager.show(placementId, adUnitId, token)
            }
        } else {
            adManager.show(placementId, adUnitId, biddingToken)
        }
    }


    override fun onResume() {
        super.onResume()
        adManager.onResume()
    }

    override fun onPause() {
        super.onPause()
        adManager.onPause()
    }

    override fun onDestroy() {
        adManager.onDestroy()
        super.onDestroy()
    }

    /**
     * 开屏页一定要禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    protected abstract fun getAppId(): String?

    protected abstract fun getAppKey(): String?

    protected abstract fun getAdUnitId(): String?

    protected abstract val adPlacementId: String?

    protected abstract fun isBidding(): Boolean?

    @get:DrawableRes
    protected abstract val launchBackground: Int?

}
