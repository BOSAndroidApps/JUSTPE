package com.bos.payment.appName.ui.view.Dashboard.activity

import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.makePayment.GetMakePaymentReq
import com.bos.payment.appName.data.model.makePayment.GetMakePaymentRes
import com.bos.payment.appName.data.repository.MoneyTransferRepository
import com.bos.payment.appName.data.viewModelFactory.MoneyTransferViewModelFactory
import com.bos.payment.appName.databinding.ActivityMakePaymentBinding
import com.bos.payment.appName.network.RetrofitClient
import com.bos.payment.appName.ui.viewmodel.MoneyTransferViewModel
import com.bos.payment.appName.utils.ApiStatus
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.MStash
import com.bos.payment.appName.utils.Utils
import com.bos.payment.appName.utils.Utils.PD
import com.bos.payment.appName.utils.Utils.hideKeyboard
import com.bos.payment.appName.utils.Utils.runIfConnected
import com.bos.payment.appName.utils.Utils.toast
import com.bos.payment.appName.utils.imageCompressor.Compressor
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

class MakePayment : AppCompatActivity() {
    private lateinit var bin: ActivityMakePaymentBinding
    private var paymentMode_txt: String? = ""
    private var mStash: MStash? = null
    var flag = 0
    private val UPDATE_REQ_CODE = 267
    private var im: InputMethodManager? = null
    private var builder: AlertDialog.Builder? = null
    private var mCapturedImageURI: Uri? = null
    private lateinit var pd: AlertDialog
    private lateinit var viewModel: MoneyTransferViewModel

    var compressedBitmap: Bitmap? = null

    // Constants
    private val CAMERA_REQUEST_CODE = 1
    private val GALLERY_REQUEST_CODE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bin = ActivityMakePaymentBinding.inflate(layoutInflater)
        setContentView(bin.root)
        initView()
        setDropDown()
        btnListener()

    }

    private fun setDropDown() {
        val paymentModeSp = resources.getStringArray(R.array.makePayment_array)
        val paymentModeAdapter = ArrayAdapter(this, R.layout.spinner_right_aligned, paymentModeSp)
        paymentModeAdapter.setDropDownViewResource(R.layout.spinner_right_aligned)
        bin.paymentModeSp.adapter = paymentModeAdapter
        bin.paymentModeSp.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position > 0) {
                        bin.branchCodeTxt.visibility = View.VISIBLE
                        paymentMode_txt = parent!!.getItemAtPosition(position).toString()
                    }
                    if (position == 2) {
                        bin.branchCodeTxt.visibility = View.GONE
                    } else {
                        bin.branchCodeTxt.visibility = View.VISIBLE
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        bin.paymentModeSp.setSelection(0)

    }

    private fun btnListener() {
        bin.makePaymentToolBar.tvToolbarName.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }
        bin.makePaymentToolBar.ivBack.setOnClickListener {
            onBackPressed()
        }
        bin.uploadSlipBtn.setOnClickListener {
            flag = Constants.UPLOAD_SLIP
            show_image_upload_option()
        }
        bin.proceedBtn.setOnClickListener {
            makePaymentValidation()
        }

    }

    private fun initView() {
        pd = PD(this)
        mStash = MStash.getInstance(this)
        builder = AlertDialog.Builder(this)
        im = application.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        viewModel = ViewModelProvider(
            this, MoneyTransferViewModelFactory(
                MoneyTransferRepository(RetrofitClient.apiAllInterface)
            )
        )[MoneyTransferViewModel::class.java]

        try {
            val imageUrl = mStash!!.getStringValue(Constants.CompanyLogo, "")

            Picasso.get()
                .load(imageUrl)
//            .placeholder(R.drawable.placeholder)  // Optional: placeholder while loading
                .error(R.drawable.ic_error)        // Optional: error image if load fails
                .into(bin.makePaymentToolBar.tvToolbarName)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
        bin.branchCodeTxt.visibility = View.VISIBLE
    }

    private fun makePaymentValidation() {
        if (paymentMode_txt.isNullOrEmpty()) {
            bin.paymentModeSp.requestFocus()
            toast("Please select the payment mode")
        } else if (bin.amount.text.toString().isEmpty()) {
            bin.amount.requestFocus()
            toast("Enter your Amount")
        } else if (bin.depositingBank.text.toString().isEmpty()) {
            bin.depositingBank.requestFocus()
            toast("Enter your bank name")
//        } else if (bin.branchCode.text.toString().isEmpty()) {
//            bin.branchCode.requestFocus()
//            toast("Enter your branch code")
        } else if (bin.transitionId.text.toString().isEmpty()) {
            bin.transitionId.requestFocus()
            toast("Enter your transaction Id")
//        } else if (bin.remarks.text.toString().isEmpty()) {
//            toast("Enter remarks")
//        } else if (Utils.IsImgaeUpload(bin.slipImage, this) == true) {
//            Utils.InvalidField(im!!, bin.uploadSlipBtn, "Upload statement slip", this)
        } else {
            getMakePaymentApiCalling()
        }
    }

    private fun getMakePaymentApiCalling() {
        this.runIfConnected {
            val getMakePaymentReq = GetMakePaymentReq(
                RegistrationId = mStash!!.getStringValue(Constants.RegistrationId, ""),
                Amount = bin.amount.text.toString().trim(),
                DepositBankName = bin.depositingBank.text.toString().trim(),
                PaymentMode = paymentMode_txt,
                TransactionID = bin.transitionId.text.toString().trim(),
                DcoumentPath = Utils.getBytesFromBitmap(
                    Utils.getBitmapFromDrawable(
                        bin.slipImage,
                        this
                    )
                ).toString(),
                Remarks = bin.remarks.text.toString().trim(),
                CompanyCode = mStash!!.getStringValue(Constants.CompanyCode, "")
            )
            Log.d("getMakePaymentApi", Gson().toJson(getMakePaymentReq))
            viewModel.getAllMakePayment(getMakePaymentReq).observe(this) { resource ->
                resource?.let {
                    when (it.apiStatus) {
                        ApiStatus.SUCCESS -> {
                            pd.dismiss()
                            it.data?.let { users ->
                                users.body()?.let { response ->
                                    getMakePaymentRes(response)
                                }
                            }
                        }

                        ApiStatus.ERROR -> {
                            pd.dismiss()

                        }

                        ApiStatus.LOADING -> {
                            pd.show()
                        }
                    }
                }
            }
        }
    }

    private fun getMakePaymentRes(response: GetMakePaymentRes) {
        if (response.Status == true) {
            toast(response.message.toString())
            startActivity(Intent(this, DashboardActivity::class.java))
        } else {
            toast(response.message.toString())
        }
    }

//    private fun show_image_upload_option() {
//        hideKeyboard(this)
//
//        // Ensure builder is initialized
//        val builder = builder ?: AlertDialog.Builder(this)
//
//        builder.setView(null)
//        builder.setTitle("Upload Pictures Option")
//        builder.setMessage("How do you want to set your picture?")
//
//        // Positive button for gallery
//        builder.setPositiveButton("Gallery") { _, _ ->
//            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            startActivityForResult(galleryIntent, 2)
//            Log.d(TAG, "Gallery option selected")
//        }
//
//        // Negative button for camera
//        builder.setNegativeButton("Camera") { _, _ ->
//            val values = ContentValues().apply {
//                put(MediaStore.Images.Media.TITLE, "Image File Name")
//            }
//            mCapturedImageURI = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
//            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
//                putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI)
//            }
//            startActivityForResult(cameraIntent, 1)
//        }
//
//        // Show the dialog
//        builder.show()
//    }
//
//    @Deprecated("Deprecated in Java")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK) {
//            try {
//                when (requestCode) {
//                    1 -> handleCameraResult() // Camera
//                    2 -> handleGalleryResult(data) // Gallery
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
//
//    private fun handleCameraResult() {
//        try {
//            val selectedImagePath = Utils.getRealPathFromURI(mCapturedImageURI, this)
//            Log.i("SelectedImagePath", selectedImagePath ?: "No path found")
//            val file = File(selectedImagePath)
//            Constants.compressedBitmap = compressImage(file)
//
//            // Set the image in your UI
//            setImage()
//
//            // Set the compressed image path to send it via API
//            sendImagePathForApi(selectedImagePath)
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }
//
//    private fun handleGalleryResult(data: Intent?) {
//        if (data != null) {
//            try {
//                val selectedImageUri = data.data
//                Log.i("SelectedImage", selectedImageUri.toString())
//                val file = File(Utils.getPath(this, selectedImageUri!!)!!)
//                Log.i("SelectedImage", file.toString())
//
//                Constants.compressedBitmap = compressImage(file)
//
//                // Set the image in your UI
//                setImage()
//
//                // Set the compressed image path to send it via API
//                sendImagePathForApi(file.absolutePath)
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
//    }
//
//    private fun sendImagePathForApi(imagePath: String?) {
//        if (imagePath != null) {
//            val imageFile = File(imagePath)
//            Log.i("SelectedImage", imageFile.toString())
//
//            val imageBytes = Utils.getBytesFromBitmap(Utils.getBitmapFromDrawable(bin.slipImage, this))
//
//            val getMakePaymentReq = GetMakePaymentReq(
//                RegistrationId = mStash!!.getStringValue(Constants.MerchantId, ""),
//                Amount = bin.amount.text.toString().trim(),
//                DepositBankName = bin.depositingBank.text.toString().trim(),
//                PaymentMode = paymentMode_txt,
//                TransactionID = bin.transitionId.text.toString().trim(),
//                DcoumentPath = imageBytes.toString(),  // Send the image bytes as a string
//                Remarks = bin.remarks.text.toString().trim(),
//                CompanyCode = mStash!!.getStringValue(Constants.CompanyCode, "")
//            )
//
//            Log.d("getMakePaymentApi", Gson().toJson(getMakePaymentReq))
//            viewModel.getAllMakePayment(getMakePaymentReq).observe(this) { resource ->
//                resource?.let {
//                    when (it.apiStatus) {
//                        ApiStatus.SUCCESS -> {
//                            pd.dismiss()
//                            it.data?.let { users -> users.body()?.let { response -> getMakePaymentRes(response) } }
//                        }
//                        ApiStatus.ERROR -> {
//                            pd.dismiss()
//                        }
//                        ApiStatus.LOADING -> {
//                            pd.show()
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//
//    private fun compressImage(file: File): Bitmap? {
//        return try {
//            Compressor(this)
//                .setQuality(60)
//                .setMaxWidth(640)
//                .setMaxHeight(480)
//                .setCompressFormat(Bitmap.CompressFormat.JPEG)
//                .setDestinationDirectoryPath(
//                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath
//                )
//                .compressToBitmap(file)
//        } catch (e: IOException) {
//            e.printStackTrace()
//            null
//        }
//    }
//
//    private fun setImage() {
//        if (flag == Constants.UPLOAD_SLIP) {
//            Utils.check_error_msg(bin.uploadSlipBtn)
//            bin.slipImage.setImageBitmap(Constants.compressedBitmap)
//        }
//    }

    private fun show_image_upload_option() {
        hideKeyboard(this)
        builder!!.setView(null)
        builder!!.setTitle("Upload Pictures Option")
        builder!!.setMessage("How do you want to set your picture?")
        builder!!.setPositiveButton("Gallery",
            DialogInterface.OnClickListener { arg0, arg1 ->
                val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(i, 2)
                Log.d(TAG, "btnListener: " + "Gallery")

            })
        builder!!.setNegativeButton("Camera",
            DialogInterface.OnClickListener { arg0, arg1 ->
                val values = ContentValues()
                values.put(MediaStore.Images.Media.TITLE, "Image File name")
                mCapturedImageURI =
                    contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                val intentPicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intentPicture.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    mCapturedImageURI
                )
                startActivityForResult(intentPicture, 1)
            })
        builder!!.show()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            when (requestCode) {
                1 ->                     //camera
                    if (resultCode == Activity.RESULT_OK) {
                        try {
                            val selectedImagePath = Utils.getRealPathFromURI(
                                mCapturedImageURI,
                                this
                            )
                            Log.i("selectedImagePath", selectedImagePath!!)
                            val file = File(selectedImagePath)
                            Constants.compressedBitmap = null
                            Constants.compressedBitmap =
                                Compressor(
                                    this
                                )
                                    .setQuality(60)
                                    .setMaxWidth(640)
                                    .setMaxHeight(480)
                                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                                    .setDestinationDirectoryPath(
                                        Environment.getExternalStoragePublicDirectory(
                                            Environment.DIRECTORY_PICTURES
                                        ).absolutePath
                                    )
                                    .compressToBitmap(file)
                            val stream = ByteArrayOutputStream()
                            Constants.compressedBitmap?.compress(
                                Bitmap.CompressFormat.JPEG,
                                100,
                                stream
                            )
                            Log.d("compressToBitmap", stream.toString())

                            setImage()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }

                2 ->                     //gallery
                    // Enable if permission granted
                    if (resultCode == Activity.RESULT_OK) {
                        if (data != null) {
                            try {
                                val selectedImage = data.data
                                Log.i("Selected Image", "" + selectedImage)
                                val file = File(Utils.getPath(this, selectedImage!!)!!)
                                Constants.compressedBitmap = null
                                Constants.compressedBitmap =
                                    Compressor(
                                        this
                                    )
                                        .setQuality(60)
                                        .setMaxWidth(640)
                                        .setMaxHeight(480)
                                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                                        .setDestinationDirectoryPath(
                                            Environment.getExternalStoragePublicDirectory(
                                                Environment.DIRECTORY_PICTURES
                                            ).absolutePath
                                        )
                                        .compressToBitmap(file)
                                val stream = ByteArrayOutputStream()
                                Constants.compressedBitmap?.compress(
                                    Bitmap.CompressFormat.JPEG,
                                    100,
                                    stream
                                )
                                Log.d("compressToBitmap", stream.toString())
                                setImage()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                    }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun setImage() {

        if (flag == Constants.UPLOAD_SLIP) {
            Utils.check_error_msg(bin.uploadSlipBtn)
            bin.slipImage.setImageBitmap(Constants.compressedBitmap)
        }
    }
}