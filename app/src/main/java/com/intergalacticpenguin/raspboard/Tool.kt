package com.intergalacticpenguin.raspboard

import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.net.wifi.WifiManager
import android.util.Log
import java.math.BigInteger
import java.net.InetAddress
import java.net.UnknownHostException
import java.nio.ByteOrder


fun getDeviceIp(context: Context): String {
    val wifiManager = context.getSystemService(WIFI_SERVICE) as WifiManager
    var ipAddress = wifiManager.connectionInfo.ipAddress

    // Convert little-endian to big-endianif needed
    if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
        ipAddress = Integer.reverseBytes(ipAddress)
    }

    val ipByteArray = BigInteger.valueOf(ipAddress.toLong()).toByteArray()

    var ipAddressString: String = "n/a"
    try {
        ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress()
    } catch (ex: UnknownHostException) {
        Log.e("getDeviceIp", "Unable to get IP address.")
    }

    return ipAddressString
}