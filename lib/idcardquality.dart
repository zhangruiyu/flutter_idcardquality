import 'dart:async';
import 'dart:typed_data';

import 'package:flutter/services.dart';

class Idcardquality {
  static const MethodChannel _channel = const MethodChannel('idcardquality');

  static Future<Map<String, dynamic>> openIdcardQuality(int side) async {
    final Uint8List image =
        await _channel.invokeMethod('openIdcardQuality', {'side': side});
    return {'side': side.toString(), 'image': image};
  }
}
