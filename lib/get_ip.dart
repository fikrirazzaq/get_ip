
import 'dart:async';

import 'package:flutter/services.dart';

class GetIp {
  static const MethodChannel _channel = MethodChannel('get_ip');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
