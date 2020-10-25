package com.crazecoder.flutter.mintegral.utils;

import android.app.Activity;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.mintegral.msdk.MIntegralConstans;
import com.mintegral.msdk.interstitialvideo.out.InterstitialVideoListener;
import com.mintegral.msdk.interstitialvideo.out.MTGInterstitialVideoHandler;
import com.mintegral.msdk.videocommon.download.NetStateOnReceive;

import io.flutter.Log;


public class InterstitialVideoUtil {
    private static final String TAG = "InterstitialVideoUtil";
    private static InterstitialVideoUtil instance;
    private MTGInterstitialVideoHandler mMtgInterstitalVideoHandler;
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

            mMtgInterstitalVideoHandler = new MTGInterstitialVideoHandler(activity, placementId, adUnitId);
            mMtgInterstitalVideoHandler.setInterstitialVideoListener(new InterstitialVideoListener() {

                @Override
                public void onLoadSuccess(String placementId, String unitId) {
                    Log.e(TAG, "onLoadSuccess:" + Thread.currentThread());
                }

                @Override
                public void onVideoLoadSuccess(String placementId, String unitId) {
                    Log.e(TAG, "onVideoLoadSuccess:" + Thread.currentThread());
                    if (mMtgInterstitalVideoHandler != null && mMtgInterstitalVideoHandler.isReady()) {
                        mMtgInterstitalVideoHandler.show();
                    }
                }

                @Override
                public void onVideoLoadFail(String errorMsg) {
                    Log.e(TAG, "onVideoLoadFail errorMsg:" + errorMsg);
                }

                @Override
                public void onShowFail(String errorMsg) {
                    Log.e(TAG, "onShowFail=" + errorMsg);
                }

                @Override
                public void onAdShow() {
                    Log.e(TAG, "onAdShow");
                }

                @Override
                public void onAdClose(boolean isCompleteView) {
                    Log.e(TAG, "onAdClose rewardinfo :" + "isCompleteView：" + isCompleteView);
                }

                @Override
                public void onVideoAdClicked(String placementId, String unitId) {
                    Log.e(TAG, "onVideoAdClicked");
                }

                @Override
                public void onVideoComplete(String placementId, String unitId) {
                    Log.e(TAG, "onVideoComplete");
                }

                /**
                 * If you called {@link MTGInterstitialVideoHandler#setIVRewardEnable(int, int)}
                 * will callback this method.
                 * You can decide whether to give users a reward based on the return value.
                 *
                 * @param isComplete complete status.
                 *                   This parameter indicates whether the video or playable has finished playing.
                 *
                 * @param rewardAlertStatus interstitialVideo reward  alert  window status.
                 *                          This parameter is used to indicate the status of the alert dialog.
                 *                          If the dialog is not shown, it will return {@link MIntegralConstans#IVREWARDALERT_STATUS_NOTSHOWN}
                 *                          If the user clicks the dialog's continue button, it will return {@link MIntegralConstans#IVREWARDALERT_STATUS_CLICKCONTINUE}
                 *                          If the user clicks the dialog's cancel button, it will return {@link MIntegralConstans#IVREWARDALERT_STATUS_CLICKCANCEL}
                 *
                 */
                @Override
                public void onAdCloseWithIVReward(boolean isComplete, int rewardAlertStatus) {
                    Log.e(TAG, "onAdCloseWithIVReward");
                    Log.e(TAG, isComplete ? "Video playback/playable is complete." : "Video playback/playable is not complete.");

                    if (rewardAlertStatus == MIntegralConstans.IVREWARDALERT_STATUS_NOTSHOWN) {
                        Log.e(TAG, "The dialog is not show.");
                    }

                    if (rewardAlertStatus == MIntegralConstans.IVREWARDALERT_STATUS_CLICKCONTINUE) {
                        Log.e(TAG, "The dialog's continue button clicked.");
                    }

                    if (rewardAlertStatus == MIntegralConstans.IVREWARDALERT_STATUS_CLICKCANCEL) {
                        Log.e(TAG, "The dialog's cancel button clicked.");
                    }
                }

                @Override
                public void onEndcardShow(String placementId, String unitId) {
                    Log.e(TAG, "onEndcardShow");
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
