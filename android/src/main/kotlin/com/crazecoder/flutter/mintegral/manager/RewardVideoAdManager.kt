package com.crazecoder.flutter.mintegral.manager

import android.app.Activity
import com.crazecoder.flutter.mintegral.utils.LogUtil
import com.mbridge.msdk.out.MBBidRewardVideoHandler
import com.mbridge.msdk.out.MBRewardVideoHandler
import com.mbridge.msdk.out.MBridgeIds
import com.mbridge.msdk.out.RewardInfo
import com.mbridge.msdk.out.RewardVideoListener


class RewardVideoAdManager(
    private val activity: Activity,
    private val userId: String? = null,
    private val extraData: String? = null,
) : RewardVideoListener {
    private var mMTGRewardVideoHandler: MBRewardVideoHandler? = null
    private var mMBBidRewardVideoHandler: MBBidRewardVideoHandler? = null

    fun show(
        adUnitId: String,
        placementId: String,
        bidToken: String? = null,
        isJustPlus: Boolean? = false
    ) {
        initHandler(adUnitId, placementId, isBidding = !bidToken.isNullOrEmpty(),isJustPlus)
        if (bidToken.isNullOrEmpty()) {
            mMTGRewardVideoHandler?.load()
        } else {
            mMBBidRewardVideoHandler?.loadFromBid(bidToken)
        }
    }

    private fun initHandler(
        adUnitId: String,
        placementId: String,
        isBidding: Boolean = false,
        isJustPlus: Boolean? = false
    ) {
//        //静音
//        mMBRewardVideoHandler.playVideoMute(MBridgeConstans.REWARD_VIDEO_PLAY_MUTE);
//        //默认非静音
//        mMBRewardVideoHandler.playVideoMute(MBridgeConstans.REWARD_VIDEO_PLAY_NOT_MUTE);
//        val mBridgeSDK: MBridgeSDK = MBridgeSDKFactory.getMBridgeSDK()
//        //静音
//        mBridgeSDK.setPlayVideoMute(MBSupportMuteAdType.REWARD_VIDEO, MBMuteState.MUTE)
//        //解除静音
//        mBridgeSDK.setPlayVideoMute(MBSupportMuteAdType.REWARD_VIDEO, MBMuteState.UN_MUTE)
        if (isBidding) {
            mMTGRewardVideoHandler = null
            mMBBidRewardVideoHandler =
                MBBidRewardVideoHandler(activity, placementId, adUnitId).apply {
                    setRewardVideoListener(this@RewardVideoAdManager)
                    //该API仅表示接受投放Reward plus的广告
                    setRewardPlus(isJustPlus==true)
                }

        } else {
            mMBBidRewardVideoHandler = null
//            mMTGRewardVideoHandler = MIntegralSDKManager.getInstance().createRewardVideoHandler(activity, placementId, adUnitId);
            mMTGRewardVideoHandler = MBRewardVideoHandler(activity, placementId, adUnitId).apply {
                setRewardVideoListener(this@RewardVideoAdManager)
                setRewardPlus(isJustPlus==true)
            }
        }

    }

    private fun showAd(){
        mMTGRewardVideoHandler?.apply {
            if (isReady) {
                show(userId, extraData)
//                show(userId)
//                show(userId, extraData)
            }
        }
        mMBBidRewardVideoHandler?.apply {
            if (isBidReady) {
                showFromBid(userId, extraData)
//                showFromBid()//客户端奖励回调 无需传递参数
//                showFromBid(userId)//userId在服务器回调中用到
//                showFromBid(userId, extraData) //String extraData在服务器回调中用到，用于传递的额外信息
            }
        }
    }

    override fun onLoadSuccess(ids: MBridgeIds) {
        LogUtil.e(
            this,
            "onLoadSuccess: " + ids.placementId + "  " + ids.unitId
        )
    }

    override fun onVideoLoadSuccess(ids: MBridgeIds) {
        LogUtil.e(this, "onVideoLoadSuccess: " + ids.placementId + "  " + ids.unitId)
        showAd()
    }

    override fun onVideoLoadFail(ids: MBridgeIds, errorMsg: String) {
        LogUtil.e(
            this,
            "onVideoLoadFail errorMsg: $errorMsg"
        )
    }

    override fun onShowFail(ids: MBridgeIds, errorMsg: String) {
        LogUtil.e(
            this, "onShowFail: $errorMsg"
        )
    }

    override fun onAdShow(ids: MBridgeIds) {
        LogUtil.e(
            this, "onAdShow"
        )
    }

    override fun onAdClose(ids: MBridgeIds, rewardInfo: RewardInfo) {
        LogUtil.e(
            this,
            "onAdClose rewardinfo : " + "RewardName:" + rewardInfo.rewardName + "RewardAmout:" + rewardInfo.rewardAmount + " isCompleteView：" + rewardInfo.isCompleteView
        )
    }

    override fun onVideoAdClicked(ids: MBridgeIds) {
        LogUtil.e(
            this,
            "onVideoAdClicked : " + ids.placementId + "  " + ids.unitId
        )
    }

    override fun onVideoComplete(ids: MBridgeIds) {
        LogUtil.e(
            this,
            "onVideoComplete : " + ids.placementId + "  " + ids.unitId
        )
    }

    override fun onEndcardShow(ids: MBridgeIds) {
        LogUtil.e(
            this,
            "onEndcardShow : " + ids.placementId + "  " + ids.unitId
        )
    }
}
