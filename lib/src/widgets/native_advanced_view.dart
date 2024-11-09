import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class NativeAdvancedView extends StatelessWidget {
  final String placementId;
  final String adUnitId;
  final bool isBidding;
  final double? width;
  final double? height;
  final String? styleJson;

  const NativeAdvancedView(
      {super.key,
      required this.placementId,
      required this.adUnitId,
      this.width,
      this.height,
      this.isBidding = false,
      this.styleJson});

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      width: width??320,
      height: height??250,
      child: AndroidView(
        viewType: "m-native-advanced-ad",
        creationParamsCodec: const StandardMessageCodec(),
        creationParams: {
          "placementId": placementId,
          "adUnitId": adUnitId,
          "isBidding": isBidding,
          "width": width,
          "height": height,
          "styleJson": styleJson,
        },
      ),
    );
  }
}
