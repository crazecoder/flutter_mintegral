import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class NativeAdView extends StatelessWidget {
  final String placementId;
  final String adUnitId;
  final bool isBidding;
  final double width;
  final bool isLandscape;

  const NativeAdView(
      {super.key,
      required this.placementId,
      required this.adUnitId,
      this.width = 320,
      this.isLandscape = true,
      this.isBidding = false});

  @override
  Widget build(BuildContext context) {
    double height = isLandscape ? width/16*9 : width/9*16;
    return SizedBox(
      width: width,
      height: height,
      child: AndroidView(
        viewType: "m-native-ad",
        creationParamsCodec: const StandardMessageCodec(),
        creationParams: {
          "placementId": placementId,
          "adUnitId": adUnitId,
          "isBidding": isBidding,
          "width": width,
          "height": height,
        },
      ),
    );
  }
}
