package com.crazecoder.flutter.mintegral.utils;

import android.app.Activity;
import android.graphics.Point;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.mbridge.msdk.out.BannerAdListener;
import com.mbridge.msdk.out.BannerSize;
import com.mbridge.msdk.out.MBBannerView;
import com.mbridge.msdk.out.MBridgeIds;

import java.util.HashMap;
import java.util.Map;

public class BannerUtil implements BannerAdListener {
    private static final String TAG = "BannerUtil";
    private Activity activity;
    private Map<String, MBBannerView> cache;
    private static BannerUtil instance;

    private BannerUtil() {
        cache = new HashMap<>();
    }

    public static BannerUtil getInstance() {
        if (instance == null) {
            instance = new BannerUtil();
        }
        return instance;
    }
    public BannerUtil setActivity(Activity activity){
        this.activity = activity;
        return instance;
    }

    public void createBanner(String placementId, String adUnitId) {
        MBBannerView mtgBannerView = new MBBannerView(activity);
        mtgBannerView.init(new BannerSize(BannerSize.DEV_SET_TYPE, 1294, 720), placementId, adUnitId);
        mtgBannerView.setAllowShowCloseBtn(true);
        mtgBannerView.setRefreshTime(15);
        mtgBannerView.setBannerAdListener(this);

        cache.put(adUnitId, mtgBannerView);
//        return bannerAd;
    }

    public void show(String adUnitId) {
        MBBannerView bannerAd = cache.get(adUnitId);
        if (bannerAd != null) {
            bannerAd.load();
            LinearLayout content = new LinearLayout(activity);
            content.setOrientation(LinearLayout.VERTICAL);
            content.setGravity(Gravity.BOTTOM);
            content.addView(bannerAd, getUnifiedBannerLayoutParams());
            activity.addContentView(
                    content,
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    public void dispose(String adUnitId) {
        MBBannerView bannerAd = cache.get(adUnitId);
        if (bannerAd != null) {
            bannerAd.release();
            View contentView = (View) bannerAd.getParent();
            if (contentView == null || !(contentView.getParent() instanceof ViewGroup)) return;

            ViewGroup contentParent = (ViewGroup) (contentView.getParent());
            contentParent.removeView(contentView);
            cache.remove(adUnitId);
        }
    }

    private LinearLayout.LayoutParams getUnifiedBannerLayoutParams() {
        Point screenSize = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(screenSize);
        return new LinearLayout.LayoutParams(screenSize.x, Math.round(screenSize.x / 6.4F));
    }

    @Override
    public void onLoadFailed(MBridgeIds ids, String msg) {
        Log.e(TAG, "on load failed"+msg);

    }

    @Override
    public void onLoadSuccessed(MBridgeIds ids) {

        Log.e(TAG, "on load successed");
    }

    @Override
    public void onClick(MBridgeIds ids) {
        Log.e(TAG, "onAdClick");
    }

    @Override
    public void onLeaveApp(MBridgeIds ids) {
        Log.e(TAG, "leave app");
    }

    @Override
    public void showFullScreen(MBridgeIds ids) {
        Log.e(TAG, "showFullScreen");
    }

    @Override
    public void closeFullScreen(MBridgeIds ids) {
        Log.e(TAG, "closeFullScreen");
    }

    @Override
    public void onLogImpression(MBridgeIds ids) {
        Log.e(TAG, "onLogImpression");
    }

    @Override
    public void onCloseBanner(MBridgeIds ids) {
        Log.e(TAG, "onCloseBanner");
    }
}
