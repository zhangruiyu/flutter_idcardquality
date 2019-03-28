import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:idcardquality/idcardquality.dart';

void main() {
  const MethodChannel channel = MethodChannel('idcardquality');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await Idcardquality.platformVersion, '42');
  });
}
