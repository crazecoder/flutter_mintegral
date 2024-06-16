package com.crazecoder.flutter.mintegral.utils;

import android.app.Activity;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.text.TextUtils;


import com.mbridge.msdk.out.MBRewardVideoHandler;
import com.mbridge.msdk.out.MBridgeIds;
import com.mbridge.msdk.out.RewardInfo;
import com.mbridge.msdk.out.RewardVideoListener;
import com.mbridge.msdk.videocommon.download.NetStateOnReceive;

import io.flutter.Log;


public class RewardVideoUtil {
    private static final String TAG = "RewardVideoUtil";
    private static RewardVideoUtil instance;
    private MBRewardVideoHandler mMTGRewardVideoHandler;
    private NetStateOnReceive mNetStateOnReceive;
    private Activity activity;

    private RewardVideoUtil() {
    }

    public static RewardVideoUtil getInstance() {
        if (instance == null) {
            instance = new RewardVideoUtil();
        }
        return instance;
    }

    public RewardVideoUtil setActivity(Activity activity) {
        this.activity = activity;
        return instance;
    }

    //    public void load(String adUnitId, String placementId, String userId) {
//        initHandler(adUnitId, placementId, userId);
//        mMTGRewardVideoHandler.load();
//    }
    public void show(String adUnitId, String placementId, String userId, String rewardId) {
        initHandler(adUnitId, placementId, userId, rewardId);
        mMTGRewardVideoHandler.load();
    }

    private void initHandler(String adUnitId, String placementId, final String userId, final String rewardId) {
        try {
            // Declare network status for downloading video
            if (mNetStateOnReceive == null) {
                mNetStateOnReceive = new NetStateOnReceive();
                IntentFilter filter = new IntentFilter();
                filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                activity.registerReceiver(mNetStateOnReceive, filter);
            }

            mMTGRewardVideoHandler = new MBRewardVideoHandler(activity, placementId,adUnitId);
//            mMTGRewardVideoHandler = MIntegralSDKManager.getInstance().createRewardVideoHandler(activity, placementId, adUnitId);

            mMTGRewardVideoHandler.setRewardVideoListener(new RewardVideoListener() {

                @Override
                public void onLoadSuccess(MBridgeIds ids) {
                    Log.e(TAG, "onLoadSuccess: " + (TextUtils.isEmpty(placementId) ? "" : placementId) + "  " + ids.getUnitId());
                }

                @Override
                public void onVideoLoadSuccess(MBridgeIds ids) {
                    Log.e(TAG, "onVideoLoadSuccess: " + (TextUtils.isEmpty(placementId) ? "" : placementId) + "  " + ids.getUnitId());
                    if (mMTGRewardVideoHandler.isReady()) {
                        mMTGRewardVideoHandler.show(rewardId, userId);
                    }
                }

                @Override
                public void onVideoLoadFail(MBridgeIds ids,String errorMsg) {
                    Log.e(TAG, "onVideoLoadFail errorMsg: " + errorMsg);
                }

                @Override
                public void onShowFail(MBridgeIds ids,String errorMsg) {
                    Log.e(TAG, "onShowFail: " + errorMsg);
                }

                @Override
                public void onAdShow(MBridgeIds ids) {
                    Log.e(TAG, "onAdShow");
                }

                @Override
                public void onAdClose(MBridgeIds ids, RewardInfo rewardInfo) {
                    Log.e(TAG, "onAdClose rewardinfo : " + "RewardName:" + rewardInfo.getRewardName() + "RewardAmout:" + rewardInfo.getRewardAmount() + " isCompleteView：" + rewardInfo.isCompleteView());
                }

                @Override
                public void onVideoAdClicked(MBridgeIds ids) {
                    Log.e(TAG, "onVideoAdClicked : " + (TextUtils.isEmpty(placementId) ? "" : placementId) + "  " + ids.getUnitId());
                }

                @Override
                public void onVideoComplete(MBridgeIds ids) {
                    Log.e(TAG, "onVideoComplete : " + (TextUtils.isEmpty(placementId) ? "" : placementId) + "  " + ids.getUnitId());
                }

                @Override
                public void onEndcardShow(MBridgeIds ids) {
                    Log.e(TAG, "onEndcardShow : " + (TextUtils.isEmpty(placementId) ? "" : placementId) + "  " + ids.getUnitId());
                }

            });
            mMTGRewardVideoHandler.setRewardPlus(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
