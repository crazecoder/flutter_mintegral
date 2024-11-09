import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class SplashAdView extends StatelessWidget {
  final String placementId;
  final String adUnitId;
  final bool isBidding;
  final String closeText;
  final Widget? placeholder;
  final Function? onDispose;

  const SplashAdView({
    super.key,
    required this.placementId,
    required this.adUnitId,
    this.isBidding = false,
    this.closeText = 'Skip',
    this.placeholder,
    this.onDispose,
  });

  @override
  Widget build(BuildContext context) {
    final ValueNotifier<bool> valueNotifier = ValueNotifier(false);
    return Stack(
      children: [
        AndroidView(
          viewType: "m-splash-ad",
          creationParamsCodec: const StandardMessageCodec(),
          onPlatformViewCreated: (_) {
            MethodChannel('splash_$adUnitId')
              ..setMethodCallHandler((call) async {
                if (call.method == 'onDismissAndFailed') {
                  Navigator.pop(context);
                  onDispose?.call();
                } else if (call.method == 'onLoadSuccessed') {
                  valueNotifier.value = true;
                }
              });
          },
          creationParams: {
            "placementId": placementId,
            "adUnitId": adUnitId,
            "isBidding": isBidding,
            "closeText": closeText,
          },
        ),
        placeholder == null
            ? Container()
            : ValueListenableBuilder<bool>(
                valueListenable: valueNotifier,
                builder: (_, data, __) {
                  return Offstage(
                    offstage: data,
                    child: placeholder,
                  );
                }),
      ],
    );
  }
}
