import 'dart:async';

import 'package:flutter/services.dart';

class FlutterMintegral {
  static const MethodChannel _channel =
      const MethodChannel('flutter_mintegral');

  static Future<Null> initSdk({
    String appId,
    String appKey,
    /**
      * for EU-GDPR
      * false: MIntegralConstans.IS_SWITCH_ON
      */
    bool isProtectGDPR = true,
    /**
      * If set to TRUE, the server will not display personalized ads based on the user's personal information.
      * When receiving the user's request, and will not synchronize the user's information to other third-party partners.
      */
    bool isProtectCCPA = false,
  }) async {
    assert(appId != null &&
        appId.isNotEmpty &&
        appKey != null &&
        appKey.isNotEmpty);
    Map map = {
      "appId": appId,
      "appKey": appKey,
      "isProtectGDPR": isProtectGDPR,
      "isProtectCCPA": isProtectCCPA,
    };
    await _channel.invokeMethod('initAdSDK', map);
  }

  static Future<Null> startSplashAd({
    String adUnitId,
    String placementId,
    String launchBackgroundId,
  }) async {
    assert(adUnitId != null && placementId != null);
    Map map = {
      "adUnitId": adUnitId,
      "placementId": placementId,
      //if R.drawable.icon ,set icon
      "launchBackgroundId": launchBackgroundId,
    };
    await _channel.invokeMethod('startSplashAd', map);
  }

  static Future<Null> showInteractiveAD({
    String adUnitId,
    String placementId,
  }) async {
    assert(adUnitId != null && placementId != null);
    Map map = {
      "adUnitId": adUnitId,
      "placementId": placementId,
    };
    await _channel.invokeMethod('showInteractiveAD', map);
  }

  static Future<Null> showInterstitialVideoAD({
    String adUnitId,
    String placementId,
  }) async {
    assert(adUnitId != null && placementId != null);
    Map map = {
      "adUnitId": adUnitId,
      "placementId": placementId,
    };
    await _channel.invokeMethod('showInterstitialVideoAD', map);
  }
  

  static Future<Null> showRewardVideoAD({
    String adUnitId,
    String placementId,
    String userId,
    String rewardId,
  }) async {
    assert(adUnitId != null && placementId != null);
    Map map = {
      "adUnitId": adUnitId,
      "placementId": placementId,
      "userId": userId,
      "rewardId":rewardId,
    };
    await _channel.invokeMethod('showRewardVideoAD', map);
  }

  static Future<Null> showBannerAD(
      {String adUnitId, String placementId}) async {
    assert(adUnitId != null && placementId != null);
    Map map = {
      "adUnitId": adUnitId,
      "placementId": placementId,
    };
    await _channel.invokeMethod('showBannerAD', map);
  }

  static Future<Null> disposeBannerAD({String adUnitId}) async {
    assert(adUnitId != null);
    Map map = {
      "adUnitId": adUnitId,
    };
    await _channel.invokeMethod('disposeBannerAD', map);
  }
}
