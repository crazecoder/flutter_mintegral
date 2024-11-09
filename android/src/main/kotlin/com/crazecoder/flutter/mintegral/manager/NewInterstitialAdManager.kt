package com.crazecoder.flutter.mintegral.manager

import android.app.Activity
import com.crazecoder.flutter.mintegral.utils.LogUtil
import com.mbridge.msdk.MBridgeConstans
import com.mbridge.msdk.newinterstitial.out.MBBidNewInterstitialHandler
import com.mbridge.msdk.newinterstitial.out.MBNewInterstitialHandler
import com.mbridge.msdk.newinterstitial.out.NewInterstitialListener
import com.mbridge.msdk.out.MBridgeIds
import com.mbridge.msdk.out.RewardInfo

class NewInterstitialAdManager(
    private val activity: Activity,
    private val isLoadAndShow: Boolean = true
) : NewInterstitialListener {
    private var mMBNewInterstitialHandler: MBNewInterstitialHandler? = null
    private var mMBBidNewInterstitialHandler: MBBidNewInterstitialHandler? = null

    fun loadAd(
        adUnitId: String,
        placementId: String,
        bidToken: String? = null,
    ) {
        if (bidToken.isNullOrEmpty()) {
            mMBNewInterstitialHandler =
                MBNewInterstitialHandler(activity, placementId, adUnitId).apply {
                    setInterstitialVideoListener(this@NewInterstitialAdManager)
                    load()
                }
        } else {
            mMBBidNewInterstitialHandler =
                MBBidNewInterstitialHandler(activity, placementId, adUnitId).apply {
                    setInterstitialVideoListener(this@NewInterstitialAdManager)
                    loadFromBid(bidToken)
                }

        }
    }

    fun show() {
        mMBNewInterstitialHandler?.apply {
            if (isReady) show()
        }
        mMBBidNewInterstitialHandler?.apply {
            if (isBidReady) showFromBid()
        }
    }

    override fun onLoadCampaignSuccess(ids: MBridgeIds?) {
        /**
         * 广告已填充
         * @param ids 封装的广告id对象
         */
        LogUtil.i(this, "onLoadCampaignSuccess: " + Thread.currentThread() + " " + ids.toString())
    }

    override fun onResourceLoadSuccess(ids: MBridgeIds?) {
        /**
         * 广告资源加载成功，可以播放
         * @param ids 封装的广告id对象
         */
        LogUtil.i(this, "onResourceLoadSuccess: " + Thread.currentThread() + " " + ids.toString())
        if (isLoadAndShow)
            show()
    }

    override fun onResourceLoadFail(ids: MBridgeIds?, errorMsg: String?) {
        /**
         * 广告加载失败
         * @param errorMsg 加载错误原因
         */
        LogUtil.e(this, "onResourceLoadFail errorMsg: " + errorMsg + " " + ids.toString())
    }

    override fun onAdShow(ids: MBridgeIds?) {
        /**
         * 广告成功展示
         */
        LogUtil.i(this, "onAdShow: " + ids.toString());
    }

    override fun onAdClose(ids: MBridgeIds?, info: RewardInfo?) {
        /**
         * 广告关闭时调用
         * @param info.isCompleteView如果为true，则表示已完全观看了视频
         */
        LogUtil.i(
            this,
            "onAdClose: " + "isCompleteView：" + info?.isCompleteView + " " + ids.toString()
        )
    }

    override fun onShowFail(ids: MBridgeIds?, errorMsg: String?) {
        /**
         * 广告播放失败
         * @param errorMsg 错误原因
         */
        LogUtil.e(this, "onShowFail: " + errorMsg + " " + ids.toString())
    }

    override fun onAdClicked(ids: MBridgeIds?) {
        /**
         * 广告被点击
         * @param 封装的广告id对象
         */
        LogUtil.i(this, "onAdClicked: " + ids.toString())
    }

    override fun onVideoComplete(ids: MBridgeIds?) {
        /**
         * 广告播放完成时调用
         * @param ids 封装的广告id对象
         */
        LogUtil.i(this, "onVideoComplete: " + ids.toString())
    }

    override fun onAdCloseWithNIReward(ids: MBridgeIds?, info: RewardInfo?) {

        /**
         * 如果开发人员设置了IV奖励，则在广告关闭时调用.
         *
         * @param 封装的广告id对象
         * @param info.isCompleteView() 是否完全观看
         */
        LogUtil.i(
            this,
            ("onAdCloseWithNIReward: " + ids.toString()).toString() + "  " + info.toString()
        )

        LogUtil.i(
            this,
            if (info?.isCompleteView == true) "Video playback/playable is complete." else "Video playback/playable is not complete."
        )

        val rewardAlertStatus = info?.rewardAlertStatus

        if (rewardAlertStatus == MBridgeConstans.IVREWARDALERT_STATUS_NOTSHOWN) {
            LogUtil.e(this, "The dialog is not show.")
        }

        if (rewardAlertStatus == MBridgeConstans.IVREWARDALERT_STATUS_CLICKCONTINUE) {
            LogUtil.e(this, "The dialog's continue button clicked.")
        }

        if (rewardAlertStatus == MBridgeConstans.IVREWARDALERT_STATUS_CLICKCANCEL) {
            LogUtil.e(this, "The dialog's cancel button clicked.")
        }
    }

    override fun onEndcardShow(ids: MBridgeIds?) {
        /**
         * 展示广告落地页时调用
         * @param ids 封装的广告id对象
         */
        LogUtil.i(this, "onEndcardShow: " + ids.toString())
    }
}