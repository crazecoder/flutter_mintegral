import 'package:flutter/cupertino.dart';

class BannerSize {
  BannerSize._();

  static const int LARGE_TYPE = 1;
  static const int MEDIUM_TYPE = 2;
  static const int SMART_TYPE = 3;
  static const int STANDARD_TYPE = 4;
  static const int DEV_SET_TYPE = 5;

  static Size getSize(BuildContext context, int type) {
    switch (type) {
      case LARGE_TYPE:
        return largeSize();
      case MEDIUM_TYPE:
        return mediumSize();
      case SMART_TYPE:
        return smartSize(MediaQuery.of(context).size.width *
                MediaQuery.of(context).devicePixelRatio >
            720);
      case STANDARD_TYPE:
        return standardSize();
      default:
        return devSetSize();
    }
  }

  static Size largeSize() {
    return Size(320, 90);
  }

  static Size mediumSize() {
    return Size(300, 250);
  }

  static Size smartSize(bool isBig) {
    if (isBig) {
      return Size(728, 90);
    }
    return Size(320, 50);
  }

  static Size standardSize() {
    return Size(320, 50);
  }

  static Size devSetSize() {
    return Size(1294, 720);
  }
}
