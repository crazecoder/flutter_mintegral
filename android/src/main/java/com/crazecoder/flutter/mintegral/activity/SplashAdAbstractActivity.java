package com.crazecoder.flutter.mintegral.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.DrawableRes;


import com.crazecoder.flutter.mintegral.R;
import com.mbridge.msdk.MBridgeConstans;
import com.mbridge.msdk.MBridgeSDK;
import com.mbridge.msdk.out.MBSplashHandler;
import com.mbridge.msdk.out.MBSplashLoadListener;
import com.mbridge.msdk.out.MBSplashShowListener;
import com.mbridge.msdk.out.MBridgeIds;
import com.mbridge.msdk.out.MBridgeSDKFactory;
import com.mbridge.msdk.out.SDKInitStatusListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.flutter.Log;

public abstract class SplashAdAbstractActivity extends Activity {
    private static final String TAG = "SplashAbstractActivity";
    private MBSplashHandler mtgSplashHandler;
    private String adUnitId;
    private String appId;
    private String appKey;
    private String placementId;

    private boolean isFlutterStart = false;

    private View adContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐去状态栏部分（电池等图标和一起修饰部分）
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_ad);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            String processName = getProcessName();
//            String packageName = getPackageName();
//            if (!packageName.equals(processName) ) {
//                WebView.setDataDirectorySuffix(processName);
//            }
//        }
        adContainer = this.findViewById(R.id.ad_container);

        appId = getIntent().getStringExtra("appId");
        appKey = getIntent().getStringExtra("appKey");
        adUnitId = getIntent().getStringExtra("adUnitId");
        placementId = getIntent().getStringExtra("placementId");

        int launchBackgroundId = getIntent().getIntExtra("launchBackgroundId", -1);
        if (launchBackgroundId == -1 && getLaunchBackground() != null)
            launchBackgroundId = getLaunchBackground();
        if (launchBackgroundId != -1)
            adContainer.setBackground(getResources().getDrawable(launchBackgroundId));
        if (TextUtils.isEmpty(appId)) {
            appId = getAppId();
        } else {
            isFlutterStart = true;
        }
        if (TextUtils.isEmpty(placementId)) {
            placementId = getAdPlacementId();
        }
        if (TextUtils.isEmpty(appKey)) {
            appKey = getAppKey();
        }
        if (TextUtils.isEmpty(adUnitId)) {
            adUnitId = getAdUnitId();
        }
        if (isFlutterStart) {
            fetchSplashAD();
            return;
        }
        // 如果targetSDKVersion >= 23，就要申请好权限。如果您的App没有适配到Android6.0（即targetSDKVersion < 23），那么只需要在这里直接调用fetchSplashAD接口。
        if (Build.VERSION.SDK_INT >= 23&&isCheckPermission()) {
            checkAndRequestPermission();
        } else {
            initSdk(appId, appKey);
        }

    }

    private void initSdk(String appId, String appKey) {
        MBridgeSDK sdk = MBridgeSDKFactory.getMBridgeSDK();
        Map<String, String> map = sdk.getMBConfigurationMap(appId, appKey);
        sdk.setConsentStatus(this, isProtectGDPR() ? MBridgeConstans.IS_SWITCH_OFF : MBridgeConstans.IS_SWITCH_ON);

        sdk.init(map, this, new SDKInitStatusListener() {
            @Override
            public void onInitSuccess() {
                Log.e(TAG, "onInitSuccess");
                fetchSplashAD();
            }

            @Override
            public void onInitFail(String msg) {
                Log.e(TAG, "onInitFail:"+msg);
            }
        });
        sdk.setDoNotTrackStatus(isProtectCCPA());
    }

    /**
     * ----------非常重要----------
     * <p>
     * Android6.0以上的权限适配简单示例：
     * <p>
     * 如果targetSDKVersion >= 23，那么必须要申请到所需要的权限，再调用广点通SDK，否则广点通SDK不会工作。
     * <p>
     * Demo代码里是一个基本的权限申请示例，请开发者根据自己的场景合理地编写这部分代码来实现权限申请。
     * 注意：下面的`checkSelfPermission`和`requestPermissions`方法都是在Android6.0的SDK中增加的API，如果您的App还没有适配到Android6.0以上，则不需要调用这些方法，直接调用广点通SDK即可。
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void checkAndRequestPermission() {
        List<String> lackedPermission = new ArrayList<String>();
        if (!(checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
        }

        // 权限都已经有了，那么直接调用SDK
        if (lackedPermission.size() == 0) {
            initSdk(appId, appKey);
        } else {
            // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            requestPermissions(requestPermissions, 1024);
        }
    }

    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1024 && hasAllPermissionsGranted(grantResults)) {
            initSdk(appId, appKey);
        } else {
            // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
            Toast.makeText(this, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            finish();
        }
    }

    /**
     * 拉取开屏广告，开屏广告的构造方法有3种，详细说明请参考开发者文档。
     */
    private void fetchSplashAD() {
        if (TextUtils.isEmpty(placementId)) {
            Log.e(TAG, "error: placementId is null");
            finish();
            return;
        }
        mtgSplashHandler = new MBSplashHandler(placementId, adUnitId);
        mtgSplashHandler.setLoadTimeOut(10);
//        mtgSplashHandler.setLogoView(textView, 100, 100);
        mtgSplashHandler.setSplashLoadListener(new MBSplashLoadListener() {
            @Override
            public void onLoadSuccessed(MBridgeIds ids, int reqType) {
                Log.e(TAG, "onLoadSuccessed" + reqType);
            }

            @Override
            public void onLoadFailed(MBridgeIds ids,String msg, int reqType) {
                Log.e(TAG, "onLoadFailed" + msg + reqType);
                finish();
            }

            @Override
            public void isSupportZoomOut(MBridgeIds mBridgeIds, boolean b) {
                Log.e(TAG, "isSupportZoomOut:" +b);
            }
        });

        mtgSplashHandler.setSplashShowListener(new MBSplashShowListener() {
            @Override
            public void onShowSuccessed(MBridgeIds ids) {
                Log.e(TAG, "onShowSuccessed");
            }

            @Override
            public void onShowFailed(MBridgeIds ids,String msg) {
                Log.e(TAG, "onShowFailed" + msg);
                finish();
            }

            @Override
            public void onAdClicked(MBridgeIds ids) {
                Log.e(TAG, "onAdClicked");
            }

            @Override
            public void onDismiss(MBridgeIds ids,int type) {
                Log.e(TAG, "onDismiss" + type);
                finish();
            }

            @Override
            public void onAdTick(MBridgeIds ids,long millisUntilFinished) {
                Log.e(TAG, "onAdTick" + millisUntilFinished);
            }

            @Override
            public void onZoomOutPlayStart(MBridgeIds mBridgeIds) {
                Log.e(TAG, "onZoomOutPlayStart");
            }

            @Override
            public void onZoomOutPlayFinish(MBridgeIds mBridgeIds) {
                Log.e(TAG, "onZoomOutPlayFinish");
            }
        });

        mtgSplashHandler.loadAndShow((ViewGroup) adContainer);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mtgSplashHandler != null) {
            mtgSplashHandler.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mtgSplashHandler != null) {
            mtgSplashHandler.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        if (mtgSplashHandler != null) {
            mtgSplashHandler.onDestroy();
        }
        super.onDestroy();
    }

    /**
     * 开屏页一定要禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected abstract boolean isCheckPermission();

    protected abstract String getAppId();

    protected abstract String getAppKey();

    protected abstract String getAdUnitId();

    protected abstract String getAdPlacementId();

    protected abstract boolean isProtectGDPR();

    protected abstract boolean isProtectCCPA();

    protected abstract @DrawableRes
    Integer getLaunchBackground();
}
