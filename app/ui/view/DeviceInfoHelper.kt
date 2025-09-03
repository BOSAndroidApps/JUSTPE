package com.bos.bos.app.ui.view

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.util.Log
import java.net.Inet4Address
import java.net.NetworkInterface
import java.util.*

class DeviceInfoHelper(private val context: Context) {

    // Function to get the device IP Address
    fun getIpAddress(): String {
        return if (isConnectedToWifi()) {
            getWifiIpAddress()
        } else {
            getMobileNetworkIpAddress()
        }
    }

    // Check if connected to Wi-Fi
    private fun isConnectedToWifi(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.type == ConnectivityManager.TYPE_WIFI
    }

    // Get the Wi-Fi IP Address
    private fun getWifiIpAddress(): String {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        val ipAddressInt = wifiInfo.ipAddress

        return if (ipAddressInt != 0) {
            String.format(
                Locale.getDefault(),
                "%d.%d.%d.%d",
                (ipAddressInt and 0xff),
                (ipAddressInt shr 8 and 0xff),
                (ipAddressInt shr 16 and 0xff),
                (ipAddressInt shr 24 and 0xff)
            )
        } else {
            "Unable to retrieve Wi-Fi IP address"
        }
    }

    // Get the Mobile Network IP Address (IPv4)
    private fun getMobileNetworkIpAddress(): String {
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            for (networkInterface in interfaces) {
                val inetAddresses = networkInterface.inetAddresses
                while (inetAddresses.hasMoreElements()) {
                    val inetAddress = inetAddresses.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        return inetAddress.hostAddress ?: "No valid IP address found"
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("DeviceInfoHelper", "Error getting IP address", e)
        }
        return "No valid IP address found"
    }
}
