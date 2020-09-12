package com.crazecoder.flutter.mintegral.utils;

import android.app.Activity;
import android.graphics.Point;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mintegral.msdk.out.BannerAdListener;
import com.mintegral.msdk.out.BannerSize;
import com.mintegral.msdk.out.MTGBannerView;

import java.util.HashMap;
import java.util.Map;

public class BannerUtil implements BannerAdListener {
    private static final String TAG = "BannerUtil";
    private Activity activity;
    private Map<String, MTGBannerView> cache;
    private static BannerUtil instance;

    private BannerUtil(Activity activity) {
        this.activity = activity;
        cache = new HashMap<>();
    }

    public static BannerUtil getInstance(Activity activity) {
        if (instance == null) {
            instance = new BannerUtil(activity);
        }
        return instance;
    }

    public void getBanner(String placementId, String adUnitId) {
        //创建横幅广告：adUnitId：开发者在讯飞AI营销云平台(http://www.voiceads.cn/)申请的横幅广告位 ID
        MTGBannerView mtgBannerView = new MTGBannerView(activity);
        mtgBannerView.init(new BannerSize(BannerSize.DEV_SET_TYPE, 1294, 720), placementId, adUnitId);
        mtgBannerView.setAllowShowCloseBtn(true);
        mtgBannerView.setRefreshTime(15);

        cache.put(adUnitId, mtgBannerView);
//        return bannerAd;
    }

    public void show(String adUnitId) {
        MTGBannerView bannerAd = cache.get(adUnitId);
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
        MTGBannerView bannerAd = cache.get(adUnitId);
        if (bannerAd != null) {
            bannerAd.release();
            View contentView = (View) bannerAd.getParent();
            if (contentView == null || !(contentView.getParent() instanceof ViewGroup)) return;

            ViewGroup contentParent = (ViewGroup) (contentView.getParent());
            contentParent.removeView(contentView);
        }
    }

    private LinearLayout.LayoutParams getUnifiedBannerLayoutParams() {
        Point screenSize = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(screenSize);
        return new LinearLayout.LayoutParams(screenSize.x, Math.round(screenSize.x / 6.4F));
    }

    @Override
    public void onLoadFailed(String msg) {
        Log.e(TAG, "on load failed"+msg);

    }

    @Override
    public void onLoadSuccessed() {

        Log.e(TAG, "on load successed");
    }

    @Override
    public void onClick() {
        Log.e(TAG, "onAdClick");
    }

    @Override
    public void onLeaveApp() {
        Log.e(TAG, "leave app");
    }

    @Override
    public void showFullScreen() {
        Log.e(TAG, "showFullScreen");
    }

    @Override
    public void closeFullScreen() {
        Log.e(TAG, "closeFullScreen");
    }

    @Override
    public void onLogImpression() {
        Log.e(TAG, "onLogImpression");
    }

    @Override
    public void onCloseBanner() {
        Log.e(TAG, "onCloseBanner");
    }
}
