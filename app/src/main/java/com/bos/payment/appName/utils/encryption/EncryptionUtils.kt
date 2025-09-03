package com.bos.payment.appName.utils.encryption

import android.annotation.SuppressLint
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import android.util.Base64
import java.security.MessageDigest

object EncryptionUtils {
    private const val ALGORITHM = "AES"
    private const val TRANSFORMATION = "AES"

    fun generateKey(): SecretKey {
        val keyGen = KeyGenerator.getInstance(ALGORITHM)
        keyGen.init(256)
        return keyGen.generateKey()
    }

    @SuppressLint("GetInstance")
    fun encryptData(data: String, secretKey: SecretKey): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encryptedBytes = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }
    fun hashStr(value: String): String {
        val md5 = MessageDigest.getInstance("MD5")
        val hashBytes = md5.digest(value.toByteArray(Charsets.US_ASCII))
        return hashBytes.joinToString("") { "%02x".format(it) }.lowercase()
    }

//    fun encryptTripleDES(value: String, key: ByteArray, iv: ByteArray): String {
//        try {
//            if (value.isNotEmpty()) {
//                val cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding")
//                val secretKey = SecretKeySpec(key, "DESede")
//                val ivSpec = IvParameterSpec(iv)
//                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)
//                val encryptedBytes = cipher.doFinal(value.toByteArray(Charsets.UTF_8))
//                return Base64.getEncoder().encodeToString(encryptedBytes)
//            }
//        } catch (e: Exception) {
//            throw RuntimeException("Error encrypting data", e)
//        }
//        return ""
//    }
//
//    fun decryptTripleDES(value: String, key: ByteArray, iv: ByteArray): String {
//        try {
//            if (value.isNotEmpty()) {
//                val cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding")
//                val secretKey = SecretKeySpec(key, "DESede")
//                val ivSpec = IvParameterSpec(iv)
//                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)
//                val decodedBytes = Base64.getDecoder().decode(value)
//                val decryptedBytes = cipher.doFinal(decodedBytes)
//                return String(decryptedBytes, Charsets.UTF_8)
//            }
//        } catch (e: Exception) {
//            throw RuntimeException("Error decrypting data", e)
//        }
//        return ""
//    }

    @SuppressLint("GetInstance")
    fun decryptData(encryptedData: String, secretKey: SecretKey): String {
        if (encryptedData.isEmpty()) {
            throw IllegalArgumentException("Input string is null or empty.")
        }

        return try {
            // Decode Base64 string
            val decodedBytes = Base64.decode(encryptedData, Base64.DEFAULT)

            // Decrypt the data
            val cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.DECRYPT_MODE, secretKey)
            val decryptedBytes = cipher.doFinal(decodedBytes)

            // Convert to string
            String(decryptedBytes, Charsets.UTF_8)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid Base64 input: ${e.message}", e)
        } catch (e: Exception) {
            throw Exception("Decryption failed: ${e.message}", e)
        }
    }

//    fun decryptData(encryptedData: String, secretKey: SecretKey): String {
//        val cipher = Cipher.getInstance(ALGORITHM)
//        cipher.init(Cipher.DECRYPT_MODE, secretKey)
//        val decryptedBytes = cipher.doFinal(Base64.decode(encryptedData, Base64.DEFAULT))
//        return String(decryptedBytes, Charsets.UTF_8)
//    }

    fun getKeyFromString(keyString: String): SecretKey {
        val decodedKey = Base64.decode(keyString, Base64.DEFAULT)
        return SecretKeySpec(decodedKey, 0, decodedKey.size, ALGORITHM)
    }

    fun keyToString(key: SecretKey): String {
        return Base64.encodeToString(key.encoded, Base64.DEFAULT)
    }
}
