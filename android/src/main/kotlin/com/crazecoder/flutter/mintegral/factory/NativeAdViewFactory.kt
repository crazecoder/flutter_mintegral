package com.crazecoder.flutter.mintegral.factory

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.crazecoder.flutter.mintegral.R
import com.crazecoder.flutter.mintegral.manager.MIntegralSDKManager
import com.crazecoder.flutter.mintegral.manager.NativeAdManager
import com.crazecoder.flutter.mintegral.utils.LogUtil
import com.mbridge.msdk.nativex.view.MBMediaView
import com.mbridge.msdk.widget.MBAdChoice
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory

class NativeAdViewFactory(private val activity: Activity) :
    PlatformViewFactory(StandardMessageCodec.INSTANCE) {

    override fun create(context: Context?, viewId: Int, args: Any?): PlatformView {
        val param = args as Map<String, Any>
        val placementId = param["placementId"]?.toString()
        val adUnitId = param["adUnitId"]?.toString()
        val width = param["width"] as Double?
        val height = param["height"] as Double?
        val view = LayoutInflater.from(activity).inflate(R.layout.native_ad, null)
        val ivIcon: ImageView by lazy { view.findViewById<ImageView>(R.id.custom_icon) }
        val mbMediaView: MBMediaView by lazy { view.findViewById<MBMediaView>(R.id.custom_media) }
        val mbAdChoice: MBAdChoice by lazy { view.findViewById<MBAdChoice>(R.id.custom_choice) }
        val nativeAdManager =
            NativeAdManager(activity) { campaign, mbNativeHandler, mbBidNativeHandler ->
                //            val videoLength = campaign.videoLength

                //根据campaign.getAdchoiceSizeHeight（）和campaign.getAdchoiceSizeWidth（）和campaign.adChoiceIconSize设置adChoice的大小
                val params = mbAdChoice.layoutParams as RelativeLayout.LayoutParams
                LogUtil.d(
                    this,
                    "h&w: " + campaign.adchoiceSizeHeight + "/" + campaign.adchoiceSizeWidth
                )
//            params.height = campaign.adchoiceSizeHeight
//            params.width = campaign.adchoiceSizeWidth
//            mbAdChoice.layoutParams = params
                mbAdChoice.setCampaign(campaign)
//            var (videoWs, videoHs) = campaign.videoResolution.split("x")
//            if (width>0){
//                val videoW = Integer.parseInt(videoWs)
//                val videoH = Integer.parseInt(videoHs)
//                if (videoW/videoH<width/height){
//                    val params1 = mbMediaView.layoutParams as RelativeLayout.LayoutParams
//                    params1.width = width
//                    params1.height = height
//                    mbMediaView.layoutParams = params1
//                }
//            }
                mbMediaView.setNativeAd(campaign)

                // 您可以使用自己的视图来显示ImageView，但是如果是视频，则必须使用MBMediaView来显示
//            if (!campaign.iconUrl.isNullOrEmpty()) {
//                ivIcon.visibility = View.VISIBLE
//                Glide.with(activity.window.decorView)
//                    .load(campaign.imageUrl)
//                    .fitCenter()
//                    .apply(
//                        RequestOptions.centerCropTransform()
//                        .transform( RoundedCorners(10)))
//                    .into(ivIcon)
//                mbNativeHandler!!.registerView(view, campaign)
//            }else{
//                ivIcon.visibility = View.GONE
//            }
                val views = ArrayList<View>()
//            if (!campaign.appName.isNullOrEmpty()){
//                view.findViewById<TextView>(R.id.custom_title).apply {
//                    visibility =  View.VISIBLE
//                    text = campaign.appName
//                    views.add(this)
//                }
//            }
//            if (!campaign.appDesc.isNullOrEmpty()){
//                view.findViewById<TextView>(R.id.custom_desc).apply {
//                    visibility = View.VISIBLE
//                    text = campaign.appDesc
//                    views.add(this)
//                }
//            }

                views.add(view)

                mbNativeHandler?.registerView(view, views, campaign)
                mbBidNativeHandler?.registerView(view, views, campaign)
                LogUtil.d(this, "register finish: $adUnitId")
            }

        if (param["isBidding"] == true) {
            MIntegralSDKManager().fetchBidToken(placementId!!, adUnitId!!) { token ->
                nativeAdManager.show(
                    adUnitId,
                    placementId,
                    token,
                    width = width?.toInt(),
                    height = height?.toInt()
                )
            }
        } else {
            nativeAdManager.show(
                adUnitId!!,
                placementId!!,
                width = width?.toInt(),
                height = height?.toInt()
            )
        }

        return object : PlatformView {
            override fun getView(): View {
                return view
            }

            override fun dispose() {
                nativeAdManager.release()
            }

        }
    }
}