package com.crazecoder.flutter.mintegral;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.crazecoder.flutter.mintegral.activity.SplashAdActivity;
import com.crazecoder.flutter.mintegral.utils.BannerUtil;
import com.crazecoder.flutter.mintegral.utils.InteractiveAdUtil;
import com.crazecoder.flutter.mintegral.utils.InterstitialVideoUtil;
import com.crazecoder.flutter.mintegral.utils.RewardVideoUtil;
import com.mintegral.msdk.MIntegralConstans;
import com.mintegral.msdk.MIntegralSDK;
import com.mintegral.msdk.out.MIntegralSDKFactory;
import com.mintegral.msdk.out.SDKInitStatusListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.plugin.common.StandardMessageCodec;

/**
 * FlutterMintegralPlugin
 */
public class FlutterMintegralPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel channel;
    private static Context context;
    private static Activity activity;
    private FlutterPluginBinding flutterPluginBinding;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        this.flutterPluginBinding = flutterPluginBinding;
    }

    // This static function is optional and equivalent to onAttachedToEngine. It supports the old
    // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
    // plugin registration via this function while apps migrate to use the new Android APIs
    // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
    //
    // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
    // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
    // depending on the user's project. onAttachedToEngine or registerWith must both be defined
    // in the same class.
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_mintegral");
        channel.setMethodCallHandler(new FlutterMintegralPlugin());
        activity = registrar.activity();
        context = registrar.context();
    }

    private void checkAndRequestPermission() {
        List<String> lackedPermission = new ArrayList<String>();
        if (!(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
        }
        // 权限都已经有了，那么直接调用SDK
        if (lackedPermission.size() == 0) {
//      fetchSplashAD(this, container, skipView, appId, adId, splashADListener, 0);
        } else {
            // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            ActivityCompat.requestPermissions(activity, requestPermissions, 1024);
        }
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("initAdSDK")) {
            checkAndRequestPermission();
            if (call.hasArgument("appId") && call.hasArgument("appKey")) {
                String appId = call.argument("appId");
                String appKey = call.argument("appKey");

                MIntegralSDK sdk = MIntegralSDKFactory.getMIntegralSDK();
                Map<String, String> map = sdk.getMTGConfigurationMap(appId, appKey);
                boolean isProtectGDPR = call.argument("isProtectGDPR");
                sdk.setConsentStatus(activity, isProtectGDPR ? MIntegralConstans.IS_SWITCH_OFF : MIntegralConstans.IS_SWITCH_ON);
                sdk.init(map, context);
                boolean isProtectCCPA = call.argument("isProtectCCPA");
                sdk.setDoNotTrackStatus(isProtectCCPA);
            }
            result.success(null);
        } else if (call.method.equals("startSplashAd")) {
            String adUnitId = call.argument("adUnitId").toString();
            String placementId = call.argument("placementId").toString();
            String launchBackgroundIdStr = call.argument("launchBackgroundId");
            int launchBackgroundId;
            if (TextUtils.isEmpty(launchBackgroundIdStr)) {
                launchBackgroundId = -1;
            } else {
                Resources res = activity.getResources();
                launchBackgroundId = res.getIdentifier(launchBackgroundIdStr, "drawable", activity.getPackageName());
            }

            Intent intent = new Intent(activity, SplashAdActivity.class);
            intent.putExtra("adUnitId", adUnitId);
            intent.putExtra("placementId", placementId);
            intent.putExtra("launchBackgroundId", launchBackgroundId);
            activity.startActivity(intent);
            result.success(null);
        } else if (call.method.equals("showBannerAD")) {
            String adUnitId = call.argument("adUnitId").toString();
            String placementId = call.argument("placementId").toString();

            BannerUtil.getInstance().setActivity(activity).createBanner(placementId, adUnitId);
            BannerUtil.getInstance().show(adUnitId);
            result.success(null);
        } else if (call.method.equals("disposeBannerAD")) {
            String adUnitId = call.argument("adUnitId").toString();
            BannerUtil.getInstance().dispose(adUnitId);
            result.success(null);
        } else if (call.method.equals("showInteractiveAD")) {
            String adUnitId = call.argument("adUnitId").toString();
            String placementId = call.argument("placementId").toString();

            InteractiveAdUtil.getInstance().setActivity(activity).show(adUnitId, placementId);
            result.success(null);
        } else if (call.method.equals("showInterstitialVideoAD")) {
            String adUnitId = call.argument("adUnitId").toString();
            String placementId = call.argument("placementId").toString();

            InterstitialVideoUtil.getInstance().setActivity(activity).show(adUnitId, placementId);
            result.success(null);
        } else if (call.method.equals("showRewardVideoAD")) {
            String adUnitId = call.argument("adUnitId").toString();
            String placementId = call.argument("placementId").toString();
            String userId = call.argument("userId");
            String rewardId = call.argument("rewardId");

            RewardVideoUtil.getInstance().setActivity(activity).show(adUnitId, placementId, userId,rewardId);
            result.success(null);
        } else {
            result.notImplemented();
        }

    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        activity = binding.getActivity();
        channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "flutter_mintegral");
        channel.setMethodCallHandler(this);
        context = flutterPluginBinding.getApplicationContext();
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {

    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {

    }

    @Override
    public void onDetachedFromActivity() {
        flutterPluginBinding = null;
    }

}
