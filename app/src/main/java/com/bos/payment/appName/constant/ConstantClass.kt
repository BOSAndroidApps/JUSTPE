package com.bos.payment.appName.constant

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.util.Random

object ConstantClass {
    const val BASE_URL_LOGIN = "https://www.aepsmerchant.in/api/"
    const val BASE_URL = "https://api.bos.center/"
   //  const val BASE_URL = "https://api.boscenter.in/api/BOS/"
   //const val BASE_URL = "http://www.aepsmerchant.in/api/"
    const val api_response_default_Message = "Something Went Wrong!"
    val Success = "Success"

    var latdouble = 0.0
    var longdouble = 0.0

    fun generateRandomNumber(): String {
        val random = Random()
        val uniqueNumbers = HashSet<String>()

        while (uniqueNumbers.size < 8) {
            val randomNumber = random.nextInt(90000000) + 10000000
            uniqueNumbers.add(randomNumber.toString())
        }

        return uniqueNumbers.first()
    }


    fun saveImageToCache(context: Context, imageUri: Uri, filename: String): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(imageUri) ?: return null
            val bitmap = BitmapFactory.decodeStream(inputStream) ?: return null
            inputStream.close()

            val fileName = "$filename${System.currentTimeMillis()}.jpg"
            val tempFile = File(context.cacheDir, fileName)

            FileOutputStream(tempFile).use { output ->
                // Compress to ~70% quality
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, output)
            }

            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


}