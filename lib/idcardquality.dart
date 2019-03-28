import 'dart:async';

import 'package:flutter/services.dart';

class Idcardquality {
  static const MethodChannel _channel =
      const MethodChannel('idcardquality');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
