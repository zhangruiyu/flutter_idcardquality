import 'dart:async';

import 'package:flutter/services.dart';

class Idcardquality {
  static const MethodChannel _channel = const MethodChannel('idcardquality');

  static Future<String> platformVersion(int side) async {
    final String version =
        await _channel.invokeMethod('getPlatformVersion', {'side': side});
    return version;
  }
}
