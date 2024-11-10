import 'package:flutter/material.dart';
import 'package:flutter_mintegral/flutter_mintegral.dart';
import 'package:flutter_mintegral_example/main.dart';

class SplashAdHome extends StatefulWidget {
  @override
  _SplashAdHomeState createState() => _SplashAdHomeState();
}

class _SplashAdHomeState extends State<SplashAdHome> {
//  static final StreamController<String> _streamController =
//  StreamController<String>();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        child: SplashAdView(
          adUnitId: "3833819",
          placementId: "1897299",
          isBidding: true,
          closeText: "跳过",
          placeholder: Center(child: CircularProgressIndicator(),),
          onDispose: () {
            // Navigator.push(
            //     context, MaterialPageRoute(builder: (_) => MainHome()));
          },
        ),
      ),
    );
  }

  void initState() {
    super.initState();
//    _streamController.stream.listen((_data) {
//      print(_data);
//    });
  }

  @override
  void dispose() {
    // TODO: implement dispose
//    _streamController.sink.close();
//    _streamController.close();
    super.dispose();
  }
}
