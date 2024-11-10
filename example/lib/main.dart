import 'package:flutter/material.dart';
import 'package:flutter_mintegral/flutter_mintegral.dart';
import 'package:flutter_mintegral_example/splash.dart';

import 'banner.dart';

// import 'splash.dart';
void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  MyApp() {
      //You do not need to call this method if you have already have an splashAD
      FlutterMintegral.initSdk(
          appId: "appId",
          appKey: "appKey",
          isProtectGDPR: true);
      FlutterMintegral.startSplashAd(
          adUnitId: "3833819", placementId: "1897299", isBidding: true);

  }

  @override
  Widget build(BuildContext context) => MaterialApp(home: MainHome());
}

class MainHome extends StatelessWidget {
  @override
  Widget build(BuildContext context) => Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: SafeArea(child: Center(
          child: SingleChildScrollView(
            child: Column(
              children: [
                TextButton(
                  child: Text("splash"),
                  onPressed: () {
                    Navigator.of(context).push(MaterialPageRoute(builder: (_) => SplashAdHome()));
                  },
                ),
                TextButton(
                  child: Text("NewInterstitial"),
                  onPressed: () {
                    FlutterMintegral.showNewInterstitialAD(
                        adUnitId: "3833822",
                        placementId: "1897302",
                        isBidding: true);
                  },
                ),
                TextButton(
                  child: Text("RewardVideo"),
                  onPressed: () {
                    FlutterMintegral.showRewardVideoAD(
                        adUnitId: "3833820",
                        placementId: "1897300",
                        isBidding: true);
                  },
                ),
                TextButton(
                  child: Text("banner"),
                  onPressed: () {
                    Navigator.push(context,
                        MaterialPageRoute(builder: (_) => BannerAdHome()));
                  },
                ),
                BannerView(
                  adUnitId: "3836858",
                  placementId: "1897304",
                  bannerSizeType: BannerSize.STANDARD_TYPE,
                ),
                BannerView(
                  adUnitId: "3837362",
                  placementId: "1897304",
                  bannerSizeType: BannerSize.STANDARD_TYPE,
                ),

                TextButton(
                  child: Text("NativeAdvanced"),
                  onPressed: () {},
                ),
                NativeAdView(
                  placementId: "1897307",
                  adUnitId: "3837027",
                ),
                NativeAdView(
                  placementId: "1897307",
                  adUnitId: "3833827",
                  isBidding: true,
                ),

                // Expanded(child: Container()),
              ],
            ),
          ),
        )),
      );
}
