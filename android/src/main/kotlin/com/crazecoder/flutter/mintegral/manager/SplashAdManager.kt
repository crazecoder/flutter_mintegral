package com.crazecoder.flutter.mintegral.manager

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.crazecoder.flutter.mintegral.R
import com.crazecoder.flutter.mintegral.utils.LogUtil
import com.mbridge.msdk.mbbid.out.SplashBidRequestParams
import com.mbridge.msdk.out.MBSplashHandler
import com.mbridge.msdk.out.MBSplashLoadListener
import com.mbridge.msdk.out.MBSplashShowListener
import com.mbridge.msdk.out.MBridgeIds


class SplashAdManager(
    private val activity: Activity,
    private var adContainer: View? = null,
    private val closeText:String? = null,
    private val onLoadSuccessed: (() -> Unit)? = null,
    private val onDismissAndFailed: () -> Unit,
) : MBSplashLoadListener, MBSplashShowListener {
    private var mtgSplashHandler: MBSplashHandler? = null
    private val closeView: TextView by lazy { TextView(activity).apply {
        text = closeText
        gravity = Gravity.CENTER
        setTextColor(Color.WHITE)
        layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    } }

    init {
        if (adContainer == null) {
            adContainer = activity.window.decorView as ViewGroup
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun show(placementId: String?, adUnitId: String?, bidToken: String? = null) {
        if (placementId.isNullOrEmpty() || adUnitId.isNullOrEmpty()) {
            LogUtil.e(this, "error: placementId or adUnitId  is null")
            activity.finish()
            return
        }

        mtgSplashHandler = MBSplashHandler(placementId, adUnitId)
        mtgSplashHandler?.setLoadTimeOut(10)
        //        mtgSplashHandler.setLogoView(textView, 100, 100);
        mtgSplashHandler?.setSplashLoadListener(this)

        mtgSplashHandler?.setSplashShowListener(this)
        if (!closeText.isNullOrEmpty()) {
            val viewgroup = RelativeLayout(activity).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                addView(closeView)
            }

            mtgSplashHandler?.setDevCloseView(viewgroup)
            closeView.background = activity.resources.getDrawable(R.drawable.oval_button_background,null)
            closeView.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    mtgSplashHandler?.onDestroy()
                    onDismissAndFailed()
                }
                false
            }
        }

        if (bidToken.isNullOrEmpty()) {
            mtgSplashHandler?.loadAndShow(adContainer as ViewGroup)
        } else {
            val params = SplashBidRequestParams(placementId, adUnitId, true, 1, 200, 200)
            MIntegralSDKManager().fetchBidToken(params) { token ->
                mtgSplashHandler?.loadAndShowByToken(
                    token,
                    adContainer as ViewGroup
                )
            }
        }
    }

    fun onResume() {
        mtgSplashHandler?.onResume()
    }

    fun onPause() {
        mtgSplashHandler?.onPause()
    }

    fun onDestroy() {
        mtgSplashHandler?.onDestroy()
    }

    override fun onLoadSuccessed(ids: MBridgeIds, reqType: Int) {
        LogUtil.e(this, "onLoadSuccessed$reqType")
        onLoadSuccessed?.let {
            it.invoke()
        }
    }

    override fun onLoadFailed(ids: MBridgeIds, msg: String, reqType: Int) {
        LogUtil.e(this, "onLoadFailed$msg$reqType")
        onDismissAndFailed()
    }

    override fun isSupportZoomOut(mBridgeIds: MBridgeIds, b: Boolean) {
        LogUtil.e(this, "isSupportZoomOut:$b")
    }

    override fun onShowSuccessed(ids: MBridgeIds) {
        LogUtil.e(this, "onShowSuccessed")
    }

    override fun onShowFailed(ids: MBridgeIds, msg: String) {
        LogUtil.e(this, "onShowFailed$msg")
        onDismissAndFailed()
    }

    override fun onAdClicked(ids: MBridgeIds) {
        LogUtil.e(this, "onAdClicked")
    }

    override fun onDismiss(ids: MBridgeIds, type: Int) {
        LogUtil.e(this, "onDismiss$type")
        onDismissAndFailed()
    }

    override fun onAdTick(ids: MBridgeIds, millisUntilFinished: Long) {
        LogUtil.e(
            this,
            "onAdTick$millisUntilFinished"
        )
        closeView.text = "$closeText ${millisUntilFinished / 1000}"
    }

    override fun onZoomOutPlayStart(mBridgeIds: MBridgeIds) {
        LogUtil.e(this, "onZoomOutPlayStart")
    }

    override fun onZoomOutPlayFinish(mBridgeIds: MBridgeIds) {
        LogUtil.e(this, "onZoomOutPlayFinish")
    }
}