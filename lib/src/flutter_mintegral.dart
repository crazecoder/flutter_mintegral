import 'dart:async';

import 'package:flutter/services.dart';

class FlutterMintegral {
  static const MethodChannel _channel =
      const MethodChannel('flutter_mintegral');

  static Future<Null> initSdk({String appId, String appKey}) async {
    assert(appId != null &&
        appId.isNotEmpty &&
        appKey != null &&
        appKey.isNotEmpty);
    Map map = {
      "appId": appId,
      "appKey": appKey,
    };
    await _channel.invokeMethod('initAdSDK', map);
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
