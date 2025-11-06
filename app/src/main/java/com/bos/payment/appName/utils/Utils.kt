package com.bos.payment.appName.utils

import android.Manifest
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentUris
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.Settings
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bos.payment.appName.R
import com.bos.payment.appName.databinding.LogoutLayoutBinding
import com.bos.payment.appName.databinding.ProgressDialogBinding
import com.bos.payment.appName.ui.view.LoginActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.io.ByteArrayOutputStream
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.Random


object Utils {

    var currentNumber = 0

    fun getNextNumber(): Int {
        return currentNumber++
    }

    fun showLoadingDialog(context: Activity?): ProgressDialog {
        val progressDialog = ProgressDialog(context)
        progressDialog.show()
        if (progressDialog.window != null) {
            progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        progressDialog.setContentView(R.layout.progress_dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        return progressDialog
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateAgeFromDOB(dobString: String): Int {
        val formatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy") // change based on your input format
        val dob = LocalDate.parse(dobString, formatter)
        val today = LocalDate.now()

        return Period.between(dob, today).years
    }

    fun generateRandomNumber(): String {
        val random = Random()
        val uniqueNumbers = HashSet<String>()

        while (uniqueNumbers.size < 8) {
            val randomNumber = random.nextInt(90000000) + 10000000
            uniqueNumbers.add(randomNumber.toString())
        }

        return uniqueNumbers.first()
    }

    fun PD(context: Context?): AlertDialog {
        val builder = AlertDialog.Builder(context!!, R.style.dialog_bg)
        val binding = ProgressDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        val pd: AlertDialog = builder.create()
        if (pd.window != null) {
            pd.window?.setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        context,
                        R.color.transparent
                    )
                )
            )
            pd.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        pd.setCancelable(false)
        pd.setCanceledOnTouchOutside(false)
        pd.setOnDismissListener {
            Handler(Looper.getMainLooper()).postDelayed({ pd.dismiss() }, 500)
        }
        return pd
    }

    fun logout(activity: Activity) {
        val mStash = MStash.getInstance(activity)
        val logoutBinding = LogoutLayoutBinding.inflate(LayoutInflater.from(activity))
        BottomSheetDialog(activity, R.style.BottomSheetDialogTheme).apply {
            setTitle("Log Out")
            setContentView(logoutBinding.root)
            logoutBinding.confirmButton.setOnClickListener {
                mStash!!.setValue(Constants.IS_LOGIN, false)
                this.dismiss()
                mStash.clear()
                activity.startActivity(
                    Intent(
                        context,
                        LoginActivity::class.java
                    ).putExtra("UpdateProfile", false)
                )
                activity.finish()
                activity.finishAffinity()
            }
            logoutBinding.cancelButton.setOnClickListener { this.dismiss() }
            show()
        }
    }


    fun getCurrentDateTime(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun getChangeDateFormat(dateString: String): String {
        val knownFormats = listOf(
            "dd/MM/yyyy",
            "yyyy-MM-dd",
            "yyyy/dd/MM",
            "MM/dd/yyyy",
            "dd-MM-yyyy",
            "yyyy/MM/dd",
            "dd MMM yyyy",
            "MMM dd, yyyy",
            "dd.MM.yyyy"
        )

        for (format in knownFormats) {
            try {
                val parser = SimpleDateFormat(format, Locale.getDefault())
                parser.isLenient = false
                val date = parser.parse(dateString)
                val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                return outputFormat.format(date!!)
            } catch (_: Exception) {
                // Try next format
            }
        }

        return "" // return empty if no format matches
    }


    fun checkPermissions(context: Activity) {
        try {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_DENIED || ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED || ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED
            ) {
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    1
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            showSettingsDialog(context)
        }
    }

    fun showSettingsDialog(activity: Activity) {
        val builder = MaterialAlertDialogBuilder(activity)
        builder.setTitle("Need Permissions")
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton("GOTO SETTINGS") { dialog, which ->
            dialog.cancel()
            openSettings(activity)
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        builder.show()
    }

    fun Activity.toast(msg: String?) {
        val layout = this.layoutInflater.inflate(R.layout.custom_toast_layout, this.findViewById(R.id.toast_container))

        val textView = layout.findViewById<TextView>(R.id.toast_text)
        textView.text = msg
        Toast(this).apply { view = layout
            duration = Toast.LENGTH_SHORT
            show()
        }
        // Toast.makeText(this,msg+"",Toast.LENGTH_SHORT).show()
    }

    fun Fragment.toast(msg: String?) {
        val inflater = layoutInflater
        val layout = inflater.inflate(
            R.layout.custom_toast_layout,
            requireView().findViewById(R.id.toast_container)
        )

        val textView = layout.findViewById<TextView>(R.id.toast_text)
        textView.text = msg ?: ""
        Toast(requireContext()).apply {
            view = layout
            duration = Toast.LENGTH_SHORT
            show()
        }
        // Toast.makeText(this,msg+"",Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun IsImgaeUpload(img: ImageView?, c: Context): Boolean? {
        return img?.let {
            getBitmapFromDrawable(it, c)
                .sameAs(c.getDrawable(android.R.drawable.ic_menu_camera)
                    ?.let { it1 -> getBitmap(it1) })
        }
    }

    fun getBitmap(drawable: Drawable): Bitmap {
        val result: Bitmap
        if (drawable is BitmapDrawable) {
            result = drawable.bitmap
        } else {
            var width = drawable.intrinsicWidth
            var height = drawable.intrinsicHeight
            // Some drawables have no intrinsic width - e.g. solid colours.
            if (width <= 0) {
                width = 1
            }
            if (height <= 0) {
                height = 1
            }
            result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(result)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
        }
        return result
    }

    fun InvalidField(im: InputMethodManager, view: View, toast_msg: String?, ac: Activity) {
        Toast.makeText(ac.applicationContext, toast_msg, Toast.LENGTH_SHORT).show()
        view.requestFocus()
        im.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }


    //    fun updateLabel(view: TextView, myCalendar: Calendar, type: String?) {
//        val myFormat = "dd/MM/yyyy" //In which you need put here
//        val sdf = SimpleDateFormat(myFormat, Locale.US)
//        check_error_msg(view)
//        view.text = sdf.format(myCalendar.time)
//    }


    fun updateLabel(view: EditText, myCalendar: Calendar, type: String?) {
        val myFormat = "dd/MM/yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        check_error_msg(view)
        val formatedDate = sdf.format(myCalendar.time)
        view.setText(formatedDate)
        view.setSelection(formatedDate.length)
    }

    fun check_error_msg(v: TextView) {
        if (v.error != null) {
            v.error = null
        }
    }

    fun showChangePinDialog(activity: Activity) {
        val builder = MaterialAlertDialogBuilder(activity)
        builder.setTitle("Need Permissions")
        builder.setMessage("Are you sure want to change pin")
        builder.setPositiveButton("Yes") { dialog, which ->
//            dialog.cancel()
//            openSettings(activity)
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        builder.show()
    }

    fun openSettings(context: Activity) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivityForResult(intent, 101)
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun getRealPathFromURI(contentUri: Uri?, context: Activity): String? {
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context.managedQuery(contentUri, proj, null, null, null)!!
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getPath(context: Context, uri: Uri): String? {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    id.toLong()
                )
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(
                    split[1]
                )
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            return getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    fun getDataColumn(
        context: Context, uri: Uri?, selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor =
                context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }



    fun getBytesFromBitmap(bitmap: Bitmap?): ByteArray? {
        if (bitmap != null) {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)
            return stream.toByteArray()
        }
        return null
    }


    fun getBitmapFromDrawable(img: ImageView, c: Context?): Bitmap {
        return (img.drawable as BitmapDrawable).bitmap
    }


    @JvmStatic
    fun setImgGlideLib(uri: String, view: ImageView, context: Context, reqOptions: RequestOptions) {
        // Load the image using Glide
        Glide.with(context)
            .load(uri)
            .apply(reqOptions)
            .into(view)

        // Set click listener to show the popup
        view.setOnClickListener {
            validateImagePopup(view)
        }
    }


    fun calculateTimeDifference(startTimeStr: String, endTimeStr: String): String {
        val format = SimpleDateFormat("hh:mm a", Locale.getDefault()) // Format like "9:00 AM"
        val calendar = Calendar.getInstance()

        val startTime = format.parse(startTimeStr)
        val endTime = format.parse(endTimeStr)

        if (startTime != null && endTime != null) {

            val startCal = Calendar.getInstance().apply { time = startTime }
            val endCal = Calendar.getInstance().apply { time = endTime }

            if (endCal.before(startCal)) {
                endCal.add(Calendar.DAY_OF_MONTH, 1)
            }

            val diffInMillis = endCal.timeInMillis - startCal.timeInMillis
            val diffInHours = diffInMillis / (1000 * 60 * 60)
            val diffInMinutes = (diffInMillis / (1000 * 60)) % 60

            return "$diffInHours h $diffInMinutes m"
        }
        return "Invalid time"
    }

    fun validateImagePopup(view: ImageView) = try {
        // Configure the ImagePopup
//        imagePopup.setWindowHeight(850) // Optional
//        imagePopup.setWindowWidth(750) // Optional
//        imagePopup.setBackgroundColor(Color.BLACK)
//        imagePopup.setHideCloseIcon(true)  // Optional
//        imagePopup.setImageOnClickClose(false)  // Optional
//        imagePopup.initiatePopup(view.drawable)
//        imagePopup.viewPopup()
    } catch (e: Exception) {
        e.printStackTrace()
    }

//    fun setImg_glideLib(
//        uri: String?,
//        view: ImageView,
//        igPopup: ImagePopup?,
//        c: Context?,
//        reqOptns: RequestOptions?
//    ) {
//        view.setOnClickListener {
//            com.isourse.gsp.Constants.Uitls.validate_image_popup(
//                igPopup,
//                view
//            )
//        }
//        Glide.with(c!!).setDefaultRequestOptions(reqOptns!!).asBitmap().load(uri).error
//        ContextCompat.getDrawable(c!!, R.drawable.ic_error)
//            .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
//            .into(object : CustomTarget<Bitmap?>() {
//                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
//                    Log.d("size", "" + resource.width + "__" + resource.height)
//                    view.setImageBitmap(resource)
//                }
//
//                override fun onLoadCleared(placeholder: Drawable?) {
//                } /*@Override
//            public void onLoadFailed(@Nullable Drawable errorDrawable) {
//                super.onLoadFailed(errorDrawable);
//                view.setImageDrawable(errorDrawable);
//            }*/
//            })
//    }


    inline fun Context.runIfConnected(action: () -> Unit) {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val isConnected = connectivityManager.activeNetworkInfo?.isConnectedOrConnecting == true

        if (isConnected) {
            action()
        } else {
            Toast.makeText(
                this,
                "No internet connection. Please check your network.",
                Toast.LENGTH_LONG
            ).show()
        }
    }


    fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }



    fun getStateCode(stateName: String): String {
        return when (stateName.trim().lowercase()) {
            "jammu and kashmir" -> "01"
            "himachal pradesh" -> "02"
            "punjab" -> "03"
            "chandigarh" -> "04"
            "uttarakhand" -> "05"
            "haryana" -> "06"
            "delhi" -> "08"
            "rajasthan" -> "09"
            "uttar pradesh" -> "10"
            "bihar" -> "11"
            "sikkim" -> "12"
            "arunachal pradesh" -> "13"
            "nagaland" -> "14"
            "manipur" -> "15"
            "mizoram" -> "16"
            "tripura" -> "17"
            "meghalaya" -> "18"
            "assam" -> "19"
            "west bengal" -> "20"
            "jharkhand" -> "21"
            "odisha" -> "22"
            "chhattisgarh" -> "23"
            "madhya pradesh" -> "24"
            "gujarat" -> "25"
            "daman and diu", "dadra and nagar haveli" -> "26"
            "maharashtra" -> "27"
            "andhra pradesh" -> "28"
            "karnataka" -> "29"
            "goa" -> "30"
            "lakshadweep" -> "31"
            "kerala" -> "32"
            "tamil nadu" -> "33"
            "puducherry" -> "34"
            "andaman and nicobar islands" -> "35"
            "telangana" -> "36"
            "ladakh" -> "37"
            else -> "00"
        }
    }

    fun generateQrBitmap(content: String, sizePx: Int = 1024): Bitmap {
        val hints = hashMapOf<EncodeHintType, Any>().apply {
            put(EncodeHintType.CHARACTER_SET, "UTF-8")
            put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H) // high error correction
            put(EncodeHintType.MARGIN, 1) // minimal white border
        }
        val barcodeEncoder = BarcodeEncoder()
        return barcodeEncoder.encodeBitmap(content, BarcodeFormat.QR_CODE, sizePx, sizePx, hints)
    }




    fun buildUpiUri(
        pa: String,   // payee address (vpa)
        pn: String,   // payee name
        mc: String? = null,
        tn: String? = null,
        am: String? = null,
        cu: String = "INR"
    ): String {
        fun enc(value: String) = URLEncoder.encode(value, StandardCharsets.UTF_8.toString())

        val base = StringBuilder("upi://pay?")

        if (!mc.isNullOrEmpty()) base.append("mc=").append(enc(mc)).append("&")
        base.append("pa=").append(enc(pa)).append("&")
        base.append("pn=").append(enc(pn)).append("&")
        if (!tn.isNullOrEmpty()) base.append("tn=").append(enc(tn)).append("&")
        if (!am.isNullOrEmpty()) base.append("am=").append(enc(am)).append("&")
        base.append("cu=").append(enc(cu))

        // remove trailing '&' if any (not needed but safe)
        var uri = base.toString()
        if (uri.endsWith("&")) uri = uri.dropLast(1)
        return uri
    }



    fun getScreenshotFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }


     fun animateTextSize(textView: TextView, newSizeSp: Float) {
        val currentSize = textView.textSize / textView.resources.displayMetrics.scaledDensity
        if (currentSize == newSizeSp) return  // no need to animate if size is same

        ValueAnimator.ofFloat(currentSize, newSizeSp).apply {
            duration = 180 // smooth & quick
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                val animatedValue = it.animatedValue as Float
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, animatedValue)
            }
            start()
        }

        // subtle zoom pop
        textView.animate()
            .scaleX(1.05f)
            .scaleY(1.05f)
            .setDuration(90)
            .withEndAction {
                textView.animate().scaleX(1f).scaleY(1f).setDuration(90).start()
            }.start()
    }





}