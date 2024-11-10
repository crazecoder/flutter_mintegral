import 'package:flutter/material.dart';
import 'package:flutter_mintegral/flutter_mintegral.dart';

class BannerAdHome extends StatefulWidget {
  @override
  _BannerAdHomeState createState() => _BannerAdHomeState();
}

class _BannerAdHomeState extends State<BannerAdHome> {
  @override
  Widget build(BuildContext context) => Scaffold(
        body: Text(
          "streamController: _streamController",
        ),
      );
      
  void initState() {
    super.initState();
    FlutterMintegral.showBannerAD(adUnitId: "3833824", placementId: "1897304",isBidding: true,bannerSizeType: BannerSize.SMART_TYPE);
  }

  @override
  void dispose() {
    FlutterMintegral.disposeBannerAD(adUnitId: "3833824");
    super.dispose();
  }
}
