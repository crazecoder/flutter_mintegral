package com.crazecoder.flutter.mintegral.utils;

import android.app.Activity;


import com.mbridge.msdk.MBridgeConstans;
import com.mbridge.msdk.interactiveads.out.InteractiveAdsListener;
import com.mbridge.msdk.interactiveads.out.MBInteractiveHandler;

import java.util.HashMap;

import io.flutter.Log;

public class InteractiveAdUtil {
    private static final String TAG = "InteractiveAdUtil";

    private static InteractiveAdUtil instance;
    private MBInteractiveHandler mInterstitialHandler;
    private Activity activity;


    private InteractiveAdUtil() {
    }

    public static InteractiveAdUtil getInstance() {
        if (instance == null) {
            instance = new InteractiveAdUtil();
        }
        return instance;
    }
    public InteractiveAdUtil setActivity(Activity activity){
        this.activity = activity;
        return instance;
    }
    public void show(String adUnitId,String placementId){
        initHandler(adUnitId,placementId);
        mInterstitialHandler.load();
    }
    private void initHandler(String adUnitId,String placementId) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        // 设置广告位ID
        hashMap.put(MBridgeConstans.PROPERTIES_UNIT_ID, adUnitId);
        hashMap.put(MBridgeConstans.PLACEMENT_ID, placementId);
        mInterstitialHandler = new MBInteractiveHandler(activity, hashMap);
        mInterstitialHandler.setInteractiveAdsListener(new InteractiveAdsListener() {
            @Override
            public void onInteractivelLoadSuccess(int restype) {
                Log.e(TAG, "onInteractivelLoadSuccess");
                mInterstitialHandler.show();
            }

            @Override
            public void onInterActiveMaterialLoadSuccess() {
                Log.e(TAG, "onInterActiveMaterialLoadSuccess");
            }

            @Override
            public void onInteractiveLoadFail(String errorMsg) {
                Log.e(TAG, "onInteractiveLoadFail");
            }

            @Override
            public void onInteractiveShowSuccess() {
                Log.e(TAG, "onInteractiveShowSuccess");
            }

            @Override
            public void onInteractiveShowFail(String errorMsg) {
                Log.e(TAG, "onInteractiveShowFail " + errorMsg);
            }

            @Override
            public void onInteractiveClosed() {
                Log.e(TAG, "onInteractiveClosed");
            }

            @Override
            public void onInteractiveAdClick() {
                Log.e(TAG, "onInteractiveAdClick");
            }

            @Override
            public void onInteractivePlayingComplete(boolean isComplete) {
                Log.e(TAG, "onInteractivePlayingComplete isComplete = " + isComplete);
            }
        });
    }
}
