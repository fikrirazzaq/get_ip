package com.example.get_ip

import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.net.NetworkInterface
import java.util.*
/** GetIpPlugin */
class GetIpPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "get_ip")
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method.equals("getIpAdress")) {
      result.success(getIPAddress(true))
    } else if (call.method.equals("getIpV6Adress")) {
      result.success(getIPAddress(false))
    } else {
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  private fun getIPAddress(useIPv4: Boolean): String {
    try {
      val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
      for (intf in interfaces) {
        val addrs = Collections.list(intf.inetAddresses)
        for (addr in addrs) {
          if (!addr.isLoopbackAddress) {
            val sAddr = addr.hostAddress
            //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
            val isIPv4 = sAddr.indexOf(':') < 0

            if (useIPv4) {
              if (isIPv4)
                return sAddr
            } else {
              if (!isIPv4) {
                val delim = sAddr.indexOf('%') // drop ip6 zone suffix
                return if (delim < 0) sAddr.uppercase() else sAddr.substring(0, delim).uppercase()
              }
            }
          }
        }
      }
    } catch (e: Exception) {
      print(e)
    }
    return ""
  }
}
