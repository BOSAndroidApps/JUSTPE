package com.bos.payment.appName.ui.view.Dashboard.dmt

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bos.payment.appName.R
import com.bos.payment.appName.databinding.ActivityDmtmantraBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import java.util.concurrent.TimeUnit

class FingerCaptureActivity : AppCompatActivity() {
    private lateinit var bin: ActivityDmtmantraBinding
    private lateinit var context: Context
    private lateinit var imageView: ImageView

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bin = ActivityDmtmantraBinding.inflate(layoutInflater)
        setContentView(bin.root)
        context = this
        imageView = findViewById(R.id.ivFingerprint)

        bin.btnCaptureFingerprint.setOnClickListener {
            startDeviceWorkflow()
        }
    }

    private fun startDeviceWorkflow() {
        discoverDevice(
            onDiscoverSuccess = { deviceUrl ->
                Log.d("DeviceWorkflow", "Device discovered at: $deviceUrl")
                captureFingerData(
                    url = "$deviceUrl/rd/capture",
                    pidOptions = """
                        <PidOptions ver="1.0">
                            <Opts fCount="1" fType="0" iCount="0" pCount="0" format="0" pidVer="2.0" timeout="10000" />
                        </PidOptions>
                    """.trimIndent()
                )
            },
            onDiscoverFailure = {
                showToast("No device found. Please check the connection.")
                Log.e("DeviceWorkflow", "Device discovery failed.")
            }
        )
    }

    private fun discoverDevice(onDiscoverSuccess: (String) -> Unit, onDiscoverFailure: () -> Unit) {
        val baseUrl = "https://api.boscenter.in/rd/info"
        val request = Request.Builder().url(baseUrl).get().build()

        try {
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val data = response.body?.string() ?: ""
                    Log.d("DiscoverDevice", "Response: $data")

                    if (isDeviceReady(data)) {
                        onDiscoverSuccess("https://api.boscenter.in")
                    } else {
                        Log.d("DiscoverDevice", "Device is not ready.")
                        onDiscoverFailure()
                    }
                } else {
                    Log.e("DiscoverDevice", "Failed: ${response.message}")
                    onDiscoverFailure()
                }
            }
        } catch (e: Exception) {
            Log.e("DiscoverDevice", "Error: ${e.message}")
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
                return status.equals("READY", ignoreCase = true)
            }
            eventType = parser.next()
        }
        return false
    }

    private fun captureFingerData(url: String, pidOptions: String) {
        try {
            val requestBody =
                RequestBody.create("text/xml; charset=utf-8".toMediaType(), pidOptions)
            val request = Request.Builder().url(url).post(requestBody).build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val data = response.body?.string() ?: ""
                    handleCaptureResponse(data)
                } else {
                    showToast("Error capturing fingerprint: ${response.message}")
                    Log.e("CaptureError", "Error: ${response.message}")
                }
            }
        } catch (e: Exception) {
            showToast("Exception capturing fingerprint: ${e.message}")
            Log.e("CaptureError", "Exception: ${e.message}")
        }
    }

    private fun handleCaptureResponse(response: String) {
        Log.d("CaptureResponse", "Captured Data: $response")

        val pidData = extractPidData(response)
        if (pidData != null) {
            showToast("Fingerprint captured successfully.")
            displayFingerprintImage(pidData)
        } else {
            showToast("Failed to capture fingerprint.")
        }
    }

    private fun extractPidData(response: String): String? {
        val factory = XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()
        parser.setInput(StringReader(response))

        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && parser.name == "Data") {
                return parser.getAttributeValue(null, "content") // Assuming Base64 content is in "content" attribute
            }
            eventType = parser.next()
        }
        return null
    }

    private fun displayFingerprintImage(base64Data: String) {
        try {
            val decodedBytes = Base64.decode(base64Data, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            imageView.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Log.e("DisplayImage", "Failed to decode image: ${e.message}")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
