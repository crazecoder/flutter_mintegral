package com.crazecoder.flutter.mintegral.utils;

import android.app.Activity;
import android.content.IntentFilter;
import android.net.ConnectivityManager;


import com.mbridge.msdk.MBridgeConstans;
import com.mbridge.msdk.interstitialvideo.out.InterstitialVideoListener;
import com.mbridge.msdk.interstitialvideo.out.MBInterstitialVideoHandler;
import com.mbridge.msdk.out.MBridgeIds;
import com.mbridge.msdk.out.RewardInfo;
import com.mbridge.msdk.videocommon.download.NetStateOnReceive;

import io.flutter.Log;


public class InterstitialVideoUtil {
    private static final String TAG = "InterstitialVideoUtil";
    private static InterstitialVideoUtil instance;
    private MBInterstitialVideoHandler mMtgInterstitalVideoHandler;
    private NetStateOnReceive mNetStateOnReceive;
    private Activity activity;

    private InterstitialVideoUtil() {
    }

    public static InterstitialVideoUtil getInstance() {
        if (instance == null) {
            instance = new InterstitialVideoUtil();
        }
        return instance;
    }

    public InterstitialVideoUtil setActivity(Activity activity) {
        this.activity = activity;
        return instance;
    }

    public void show(String adUnitId, String placementId) {
        initHandler(adUnitId, placementId);
        if (mMtgInterstitalVideoHandler != null) {
            mMtgInterstitalVideoHandler.load();
        }

    }

    private void initHandler(String adUnitId, String placementId) {
        try {
            // Declare network status for downloading video
            if (mNetStateOnReceive == null) {
                mNetStateOnReceive = new NetStateOnReceive();
                IntentFilter filter = new IntentFilter();
                filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                activity.registerReceiver(mNetStateOnReceive, filter);
            }

            mMtgInterstitalVideoHandler = new MBInterstitialVideoHandler(activity, placementId, adUnitId);
            mMtgInterstitalVideoHandler.setInterstitialVideoListener(new InterstitialVideoListener() {

                @Override
                public void onLoadSuccess(MBridgeIds ids) {
                    Log.e(TAG, "onLoadSuccess:" + Thread.currentThread());
                }

                @Override
                public void onVideoLoadSuccess(MBridgeIds ids) {
                    Log.e(TAG, "onVideoLoadSuccess:" + Thread.currentThread());
                    if (mMtgInterstitalVideoHandler != null && mMtgInterstitalVideoHandler.isReady()) {
                        mMtgInterstitalVideoHandler.show();
                    }
                }

                @Override
                public void onVideoLoadFail(MBridgeIds ids,String errorMsg) {
                    Log.e(TAG, "onVideoLoadFail errorMsg:" + errorMsg);
                }

                @Override
                public void onShowFail(MBridgeIds ids,String errorMsg) {
                    Log.e(TAG, "onShowFail=" + errorMsg);
                }

                @Override
                public void onAdShow(MBridgeIds ids) {
                    Log.e(TAG, "onAdShow");
                }

                @Override
                public void onAdClose(MBridgeIds ids, RewardInfo rewardInfo) {
                    Log.e(TAG, "onAdClose rewardinfo :" + rewardInfo.isCompleteView());
                }

                @Override
                public void onVideoAdClicked(MBridgeIds ids) {
                    Log.e(TAG, "onVideoAdClicked");
                }

                @Override
                public void onVideoComplete(MBridgeIds ids) {
                    Log.e(TAG, "onVideoComplete");
                }

                @Override
                public void onAdCloseWithIVReward(MBridgeIds ids, RewardInfo rewardInfo) {
                    Log.e(TAG, "onAdCloseWithIVReward");
                    Log.e(TAG, rewardInfo.isCompleteView() ? "Video playback/playable is complete." : "Video playback/playable is not complete.");
                    int rewardAlertStatus = rewardInfo.getRewardAlertStatus();
                    if (rewardAlertStatus == MBridgeConstans.IVREWARDALERT_STATUS_NOTSHOWN) {
                        Log.e(TAG, "The dialog is not show.");
                    }

                    if (rewardAlertStatus == MBridgeConstans.IVREWARDALERT_STATUS_CLICKCONTINUE) {
                        Log.e(TAG, "The dialog's continue button clicked.");
                    }

                    if (rewardAlertStatus == MBridgeConstans.IVREWARDALERT_STATUS_CLICKCANCEL) {
                        Log.e(TAG, "The dialog's cancel button clicked.");
                    }
                }

                @Override
                public void onEndcardShow(MBridgeIds ids) {
                    Log.e(TAG, "onEndcardShow");
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
