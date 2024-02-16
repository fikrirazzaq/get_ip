import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:get_ip/get_ip.dart';

void main() => runApp(const MyApp());

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _ip = 'Unknown';

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String? ipAddress;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      ipAddress = await GetIp.ipAddress;
    } on PlatformException {
      ipAddress = 'Failed to get ipAddress.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _ip = ipAddress ?? 'Unknown';
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('IP Example app'),
        ),
        body: Center(
          child: Text('Running on: $_ip\n'),
        ),
      ),
    );
  }
}
