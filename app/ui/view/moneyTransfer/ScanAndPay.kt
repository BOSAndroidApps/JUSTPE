package com.bos.payment.appName.ui.view.moneyTransfer

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bos.payment.appName.databinding.ActivityScanAndPayBinding
import com.bos.payment.appName.ui.view.Dashboard.activity.GenerateQRCodeActivity
import com.bos.payment.appName.ui.viewmodel.MoneyTransferViewModel
import com.bos.payment.appName.utils.MStash
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import org.json.JSONException
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScannerFragment : AppCompatActivity() {
    private val vibrator: Vibrator by lazy {
        this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
    private lateinit var binding: ActivityScanAndPayBinding
    private lateinit var cameraExecutor: ExecutorService
    private var isScanned = false
    private var flag: Int = 0
    private val UPDATE_REQ_CODE = 267
    private var isFlashOn: Boolean = false
    private lateinit var camera: Camera
    private var builder: AlertDialog.Builder? = null
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var previewView: PreviewView
    private var mStash: MStash? = null
    private lateinit var viewModel: MoneyTransferViewModel


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanAndPayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        btnListener()

        cameraExecutor = Executors.newSingleThreadExecutor()

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions.launch(REQUIRED_PERMISSIONS)
        }
        // Check if specific package is installed
//        checkIfPackageInstalled(requireContext())

//        return binding.root
    }

    private fun initView() {
        builder = AlertDialog.Builder(this)
        previewView = findViewById(com.bos.payment.appName.R.id.previewView)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun btnListener() {
        binding.crossBtn.setOnClickListener {
            onBackPressed()
        }
        binding.torchBtn.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
            }else {
                flashLight()
            }
        }
        binding.barCodeBtn.setOnClickListener {
            startActivity(Intent(this, GenerateQRCodeActivity::class.java))
            finish()
        }
        binding.uploadImage.setOnClickListener {
            openGallery()
//            flag = Constants.UPLOAD_PHOTO
//            show_image_upload_option()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun flashLight() {
        isFlashOn = if (isFlashOn){
            setFlashLight(false)
            false
        } else {
            setFlashLight(true)
            true
        }
    }
    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setFlashLight(state: Boolean) {
        val cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        try {
//            val cameraId = cameraManager.cameraIdList[0]
//            cameraManager.setTorchMode(cameraId, state)
            camera.cameraControl.enableTorch(state)
            // Update the torch button icon based on the state
            if (state) {
                binding.torchBtn.setImageResource(com.bos.payment.appName.R.drawable.flashlight_off) // replace with your "off" icon
            } else {
                binding.torchBtn.setImageResource(com.bos.payment.appName.R.drawable.flashlight_on) // replace with your "on" icon
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }

            val imageAnalyzer = ImageAnalysis.Builder().build().also {
                it.setAnalyzer(cameraExecutor, BarcodeAnalyzer { qrData ->
                    runOnUiThread {
                        if (!isScanned) {
                            isScanned = true
                            vibrateOnScan()
//                            binding.scannedData.text = qrData
                            processPaymentData(qrData)
                        }
                    }
                })
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)

                camera.cameraControl.enableTorch(isFlashOn)
            } catch (exc: Exception) {
                Toast.makeText(
                    this,
                    "Error starting camera: ${exc.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    private val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.all { it.value }) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
//                finish()
            }
        }

    private fun processPaymentData(qrData: String) {
        try {
            val uri = Uri.parse(qrData)

            // Extract UPI parameters from the QR code
            val upiId = uri.getQueryParameter("pa") ?: "N/A"
            val payeeName = uri.getQueryParameter("pn") ?: "N/A"
            val merchantCode = uri.getQueryParameter("mc") ?: "N/A"
            val bankingName = uri.getQueryParameter("pn") ?: ""
            val transactionId = uri.getQueryParameter("tid") ?: "N/A"
            val referenceId = uri.getQueryParameter("tr") ?: "N/A"
            val transactionNote = uri.getQueryParameter("tn") ?: "N/A"
            val amount = uri.getQueryParameter("am") ?: ""
            val currencyCode = uri.getQueryParameter("cu") ?: "N/A"

            // Prepare Intent to start DisplayDetailsActivity with extracted details
            val intent = Intent(this, DisplayDetailsActivity::class.java).apply {
                putExtra("upiId", upiId)
                putExtra("payeeName", payeeName)
                putExtra("bankingName", bankingName)
                putExtra("merchantCode", merchantCode)
                putExtra("transactionId", transactionId)
                putExtra("referenceId", referenceId)
                putExtra("transactionNote", transactionNote)
                putExtra("amount", amount)
                putExtra("currencyCode", currencyCode)
            }
            startActivity(intent)
            finish()

            // Show a Toast with the extracted details for testing
//            Toast.makeText(
//                this,
//                "UPI ID: $upiId\nPayee Name: $payeeName\nMerchant Code: $merchantCode\nTransaction ID:" +
//                        " $transactionId\nReference ID: $referenceId\nTransaction Note: $transactionNote\nAmount: " +
//                        "$amount\nCurrency Code: $currencyCode",
//                Toast.LENGTH_LONG
//            ).show()

        } catch (e: JSONException) {
            Toast.makeText(this, "Invalid QR code data", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private class BarcodeAnalyzer(private val onQrCodeDetected: (qrData: String) -> Unit) :
        ImageAnalysis.Analyzer {
        @OptIn(ExperimentalGetImage::class)
        override fun analyze(imageProxy: ImageProxy) {
            val mediaImage = imageProxy.image ?: return
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            val scanner = BarcodeScanning.getClient()
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        barcode.rawValue?.let { onQrCodeDetected(it) }
                    }
                }
                .addOnFailureListener {
                    // Handle failure
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }

    /**
     *  Vibration mobile on Scan successful.
     */
    private fun vibrateOnScan() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        VIBRATE_DURATION,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                vibrator.vibrate(VIBRATE_DURATION)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val TAG = "QrCodeReaderFragment"
        private const val VIBRATE_DURATION = 200L
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            flashLight()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val selectedImageUri: Uri? = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
                // Display selected image (optional)
                // previewView.setImageBitmap(bitmap)
                // Extract bank details from the image
                fetchBarcodeFromImage(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchBarcodeFromImage(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val scanner = BarcodeScanning.getClient()
        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    barcode.rawValue?.let {
                        processPaymentData(it)
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to process image", Toast.LENGTH_SHORT).show()
            }
    }
}
