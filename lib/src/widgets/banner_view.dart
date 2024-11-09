import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import '../../flutter_mintegral.dart';

class BannerView extends StatelessWidget {
  final String placementId;
  final String adUnitId;
  final bool isBidding;
  final int bannerSizeType;
  final int refreshTime;
  final double? width;
  final double? height;

  const BannerView(
      {super.key,
      required this.placementId,
      required this.adUnitId,
      this.bannerSizeType = BannerSize.SMART_TYPE,
      this.refreshTime = 30,
      this.width,
      this.height,
      this.isBidding = false});

  @override
  Widget build(BuildContext context) {
    var size = BannerSize.getSize(context, bannerSizeType);
    if (bannerSizeType == BannerSize.DEV_SET_TYPE) {
      if (width != null && height != null) {
        size = Size(width!, height!);
      }
    }
    return SizedBox(
      width: size.width,
      height: size.height,
      child: AndroidView(
        viewType: "m-banner",
        creationParamsCodec: const StandardMessageCodec(),
        creationParams: {
          "placementId": placementId,
          "adUnitId": adUnitId,
          "isBidding": isBidding,
          "bannerSizeType": bannerSizeType,
          "refreshTime": refreshTime,
          "width": size.width,
          "height": size.height,
        },
      ),
    );
  }
}
