package com.bos.payment.appName.ui.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import java.util.concurrent.TimeUnit

open class FingerCapture(private val context: Context) {

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    fun capture(
        quality: Int,
        timeout: Int,
        imageView: ImageView,
        onCaptureResult: (String) -> Unit
    ) {
        try {
            val res = captureFinger(quality, timeout)
            if (res.httpStatus) {
                val errorCode = res.data.errorCode
                if (errorCode == "0") {
                    val bitmapData = "data:image/bmp;base64," + res.data.bitmapData
                    imageView.setImageBitmap(Base64Utils.decodeToBitmap(bitmapData))
                    val isoTemplate = res.data.isoTemplate
                    onCaptureResult("$isoTemplate~$bitmapData")
                    showToast("Error: ${res.data.bitmapData}")

                } else {
                    showToast("Error: ${res.data.errorCode}")
                    Log.e("FingerCapture", "Error Code: ${res.data.errorCode}")
                }
            } else {
                showToast("Error: ${res.error}")
                Log.e("FingerCapture", "HTTP Error: ${res.error}")
            }
        } catch (e: Exception) {
            showToast("Exception: ${e.message}")
            Log.e("FingerCapture", "Exception occurred: ${e.message}", e)
        }
    }


    private fun captureFinger(quality: Int, timeout: Int): CaptureResponse {
        // Simulating a capture response for demonstration purposes
        return CaptureResponse(true, CaptureData("0", "ISO_TEMPLATE", "BITMAP_DATA"))
    }

    fun discoverAvdm(onDiscoverSuccess: (String) -> Unit, onDiscoverFailure: () -> Unit) {
        val baseUrl = "http://192.168.0.112:8083/"
        var success = false

        for (port in 11100..11112) {
            try {
                val url = "$baseUrl$port"
                val request = Request.Builder().url(url).get().build()
                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        val data = response.body?.string() ?: ""
                        if (isDeviceReady(data)) {
                            onDiscoverSuccess(url)
                            success = true
//                            break
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("DiscoverAvdm", "Error discovering device on port $port: ${e.message}")
            }
        }

        if (!success) {
            onDiscoverFailure()
        }
    }

    private fun isDeviceReady(data: String): Boolean {
        val factory = XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()
        parser.setInput(StringReader(data))

        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && parser.name == "RDService") {
                val status = parser.getAttributeValue(null, "status")
                return status == "READY"
            }
            eventType = parser.next()
        }
        return false
    }

    fun getDeviceInfo(url: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        try {
            val request = Request.Builder().url(url).get().build()
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val data = response.body?.string() ?: ""
                    onSuccess(data)
                } else {
                    onError("Error: ${response.message}")
                }
            }
        } catch (e: Exception) {
            onError("Exception: ${e.message}")
        }
    }

    fun captureAvdm(
        url: String,
        pidOptions: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val requestBody =
                RequestBody.create("text/xml; charset=utf-8".toMediaType(), pidOptions)
            val request = Request.Builder().url(url).post(requestBody).build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val data = response.body?.string() ?: ""
                    onSuccess(data)
                } else {
                    onError("Error: ${response.message}")
                }
            }
        } catch (e: Exception) {
            onError("Exception: ${e.message}")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

data class CaptureResponse(val httpStatus: Boolean, val data: CaptureData, val error: String = "")
data class CaptureData(val errorCode: String, val isoTemplate: String, val bitmapData: String)

object Base64Utils {
    fun decodeToBitmap(base64String: String): Bitmap? {
        return try {
            val decodedString = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        } catch (e: IllegalArgumentException) {
            Log.e("Base64Utils", "Invalid Base64 string: ${e.message}", e)
            null
        }
    }
}


//object Base64Utils {
//    fun decodeToBitmap(base64String: String): Bitmap {
//        val decodedString = Base64.decode(base64String, Base64.DEFAULT)
//        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
//    }
//}
