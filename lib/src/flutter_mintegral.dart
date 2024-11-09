import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_mintegral/src/types/layout_type.dart';

import '../flutter_mintegral.dart';

class FlutterMintegral {
  static const MethodChannel _channel =
      const MethodChannel('flutter_mintegral');

  static Future<Null> initSdk({
    required String appId,
    required String appKey,
    bool isCheckPermission = true,
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
    assert(appId.isNotEmpty && appKey.isNotEmpty);
    Map map = {
      "appId": appId,
      "appKey": appKey,
      "isProtectGDPR": isProtectGDPR,
      "isProtectCCPA": isProtectCCPA,
      "isCheckPermission": isCheckPermission
    };
    await _channel.invokeMethod('initAdSDK', map);
  }

  static Future<Null> startSplashAd({
    required String adUnitId,
    required String placementId,
    String? launchBackgroundId,
    bool isBidding = false,
  }) async {
    Map map = {
      "adUnitId": adUnitId,
      "placementId": placementId,
      "isBidding": isBidding,
      //if R.drawable.icon ,set icon
      "launchBackgroundId": launchBackgroundId,
    };
    await _channel.invokeMethod('startSplashAd', map);
  }

  static Future<Null> preload({
    required String adUnitId,
    required String placementId,
    int? layoutType,
  }) async {
    Map map = {
      "adUnitId": adUnitId,
      "placementId": placementId,
      "layoutType": layoutType,
    };
    await _channel.invokeMethod('preload', map);
  }

  // static Future<Null> showNativeAdvancedAD({
  //   required String adUnitId,
  //   required String placementId,
  //   bool isBidding = false,
  // }) async {
  //   Map map = {
  //     "adUnitId": adUnitId,
  //     "placementId": placementId,
  //     "isBidding": isBidding,
  //   };
  //   await _channel.invokeMethod('showNativeAdvancedAD', map);
  // }

  static Future<Null> showNewInterstitialAD({
    required String adUnitId,
    required String placementId,
    bool isBidding = false,
  }) async {
    Map map = {
      "adUnitId": adUnitId,
      "placementId": placementId,
      "isBidding": isBidding,
    };
    await _channel.invokeMethod('showNewInterstitialAD', map);
  }

  static Future<Null> showRewardVideoAD(
      {required String adUnitId,
      required String placementId,
      String? userId,
      String? extraData,
      bool isBidding = false,
      bool isJustPlus = false}) async {
    Map map = {
      "adUnitId": adUnitId,
      "placementId": placementId,
      "userId": userId,
      "extraData": extraData,
      "isBidding": isBidding,
      "isJustPlus": isJustPlus,
    };
    await _channel.invokeMethod('showRewardVideoAD', map);
  }

  static Future<Null> showBannerAD(
      {required String adUnitId,
      required String placementId,
      bool isBidding = false,
      int bannerSizeType = BannerSize.SMART_TYPE,
      int refreshTime = 30}) async {
    Map map = {
      "adUnitId": adUnitId,
      "placementId": placementId,
      "isBidding": isBidding,
      "bannerSizeType": bannerSizeType,
      "refreshTime": refreshTime,
    };
    await _channel.invokeMethod('showBannerAD', map);
  }

  static Future<Null> disposeBannerAD({required String adUnitId}) async {
    Map map = {
      "adUnitId": adUnitId,
    };
    await _channel.invokeMethod('disposeBannerAD', map);
  }
}
