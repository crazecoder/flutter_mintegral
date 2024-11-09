package com.crazecoder.flutter.mintegral.manager

import android.app.Activity
import android.view.ViewGroup
import com.crazecoder.flutter.mintegral.utils.LogUtil
import com.mbridge.msdk.MBridgeConstans
import com.mbridge.msdk.out.AutoPlayMode
import com.mbridge.msdk.out.MBMultiStateEnum
import com.mbridge.msdk.out.MBNativeAdvancedHandler
import com.mbridge.msdk.out.MBridgeIds
import com.mbridge.msdk.out.NativeAdvancedAdListener
import org.json.JSONObject


class NativeAdvancedAdManager(private val activity: Activity): NativeAdvancedAdListener {
    private var mbNativeAdvancedHandler: MBNativeAdvancedHandler? = null

    fun createAdView(placementId:String,adUnitId:String,width: Int? = 320,height:Int? = 250,styleJson:String? = null): ViewGroup {
        mbNativeAdvancedHandler =
            MBNativeAdvancedHandler(activity, placementId, adUnitId).apply {
                setAdListener(this@NativeAdvancedAdManager)
                //广告位大小设置 推荐: 320 x 250 比例
                setNativeViewSize(width?:320, height?:250)
                // 默认静音
                setPlayMuteState(MBridgeConstans.REWARD_VIDEO_PLAY_MUTE)
                //设置关闭按钮的状态
                setCloseButtonState(MBMultiStateEnum.positive)
                autoLoopPlay(AutoPlayMode.PLAY_WHEN_NETWORK_IS_AVAILABLE)
                if (!styleJson.isNullOrEmpty()){
                    val jsonObject = JSONObject(styleJson)
                    setViewElementStyle(jsonObject)
                }
            }
        return mbNativeAdvancedHandler!!.adViewGroup
    }

    fun show(bidToken:String? = null){
        if (bidToken.isNullOrEmpty()){
            mbNativeAdvancedHandler?.load()
        }else{
            mbNativeAdvancedHandler?.loadByToken(bidToken)
        }
    }

    fun onResume(){
        mbNativeAdvancedHandler?.onResume()
    }
    fun onPause(){
        mbNativeAdvancedHandler?.onPause()
    }

    fun onDestroy(){
        mbNativeAdvancedHandler?.release()
    }

    override fun onLoadSuccessed(ids: MBridgeIds) {
        // 广告资源加载成功且WebView渲染成功
        LogUtil.e(this, "onLoadSuccessed: $ids")
    }

    override fun onLoadFailed(ids: MBridgeIds, msg: String) {
        // 加载失败
        LogUtil.e(this, "onLoadFailed: $msg  $ids")
    }

    override fun onLogImpression(ids: MBridgeIds) {
        // 展示成功
        LogUtil.e(this, "onLogImpression: $ids")
    }

    override fun onClick(ids: MBridgeIds) {
        // 点击广告
        LogUtil.e(this, "onClick: $ids")
    }

    override fun onLeaveApp(ids: MBridgeIds) {
        // 离开app
        LogUtil.e(this, "onLeaveApp: $ids")
    }

    override fun showFullScreen(ids: MBridgeIds) {
        // 进入全屏 （只有走mraid协议的素材才会有这个回调）
        LogUtil.e(this, "showFullScreen: $ids")
    }

    override fun closeFullScreen(ids: MBridgeIds) {
        // 退出全屏 （只有走mraid协议的素材才会有这个回调）
        LogUtil.e(this, "closeFullScreen: $ids")
    }

    override fun onClose(ids: MBridgeIds) {
        // 关闭广告视图
        LogUtil.e(this, "onDismiss: $ids")
    }
}