# flutter_mintegral

flutter_mintegral, Just support android, Welcome to the fork and develop ios version

## Getting Started
```yaml
dependencies:
  flutter_mintegral:
    git:
      url: git://github.com/crazecoder/flutter_mintegral.git
```

### Splash for native
Create SplashActivity.class
```java
public class SplashActivity extends SplashAdAbstractActivity {
    @Override
    protected String getAppId() {
        return "xxxxx";
    }

    @Override
    protected String getAppKey() {
        return "xxxxxx";
    }

    @Override
    protected String getAdUnitId() {
        return "xxxxx";
    }

    @Override
    protected String getAdPlacementId() {
        return "xxxx";
    }

    @Override
    protected boolean isProtectGDPR() {
        return false;
    }

    @Override
    protected boolean isProtectCCPA() {
        return false;
    }

    @Override
    protected Integer getLaunchBackground() {
        return null;
    }
}
```
startActivity in MainActivity.class
```java
public class MainActivity extends FlutterActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
    }
}
```
#### Splash for flutter
```dart
FlutterMintegral.initSdk({
                     String appId,
                     String appKey,
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
                   });

FlutterMintegral.startSplashAd({
    String adUnitId,
    String placementId,
    String launchBackgroundId,
  });
```
#### Others
```dart
FlutterMintegral.showBannerAD({String adUnitId, String placementId});

FlutterMintegral.disposeBannerAD({String adUnitId});

FlutterMintegral.showInteractiveAD({
                     String adUnitId,
                     String placementId,
                   });

FlutterMintegral.showInterstitialVideoAD({
                     String adUnitId,
                     String placementId,
                   });

FlutterMintegral.showRewardVideoAD({
                     String adUnitId,
                     String placementId,
                     String userId,
                     String rewardId,
                   });
```
