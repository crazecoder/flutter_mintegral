import 'package:flutter/material.dart';
import 'package:flutter_mintegral/flutter_mintegral.dart';

import 'banner.dart';

// import 'splash.dart';
void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  MyApp() {
    //You do not need to call this method if you have already have an splashAD
    // FlutterMintegral.initSdk(appId: "118690",appKey: "7c22942b749fe6a6e361b675e96b3ee9");
  }

  @override
  Widget build(BuildContext context) =>MaterialApp(home: MainHome());
}

class MainHome extends StatelessWidget {
  @override
  Widget build(BuildContext context) => Scaffold(
      appBar: AppBar(
        title: const Text('Plugin example app'),
      ),
      body: Center(
        child: Column(
          children: [
            TextButton(
              child: Text("banner"),
              onPressed: () {
                Navigator.push(
                    context, MaterialPageRoute(builder: (_) => BannerAdHome()));
              },
            ),
            TextButton(
              child: Text("splash"),
              onPressed: () {
                FlutterMintegral.startSplashAd(
                  adUnitId: "209547",
                  placementId: "173349",
                );
              },
            ),
            // TextButton(
            //   child: Text("Interactive"),
            //   onPressed: () {
            //     FlutterMintegral.showInteractiveAD(
            //       adUnitId: "146878",
            //       placementId: "138790",
            //     );
            //   },
            // ),
            TextButton(
              child: Text("InterstitialVideo"),
              onPressed: () {
                FlutterMintegral.showInterstitialVideoAD(
                  adUnitId: "146869",
                  placementId: "138781",
                );
              },
            ),
            TextButton(
              child: Text("RewardVideo"),
              onPressed: () {
                FlutterMintegral.showRewardVideoAD(
                  adUnitId: "146874",
                  placementId: "138786",
                );
              },
            ),
          ],
        ),
      ),
    );
}
