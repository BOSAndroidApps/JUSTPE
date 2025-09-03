package com.bos.payment.appName.constant

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object CryptoUtils {

    private val KEY_192 = byteArrayOf( // Replace with your actual key
        0x12, 0x34, 0x56, 0x78, 0x90.toByte(), 0xAB.toByte(), 0xCD.toByte(), 0xEF.toByte(),
        0xFE.toByte(), 0xDC.toByte(), 0xBA.toByte(), 0x09, 0x87.toByte(), 0x65, 0x43, 0x21
    )

    private val IV_192 = byteArrayOf( // Replace with your actual IV
        0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
        0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10
    )

    fun hashStr(value: String): String {
        val md5 = MessageDigest.getInstance("MD5")
        val hashBytes = md5.digest(value.toByteArray(StandardCharsets.US_ASCII))
        val stringBuilder = StringBuilder()
        for (byte in hashBytes) {
            stringBuilder.append(String.format("%02x", byte))
        }
        return stringBuilder.toString()
    }

    fun encryptTripleDES(value: String): String {
        if (value.isEmpty()) return ""

        val secretKey = SecretKeySpec(KEY_192, "DESede")
        val ivSpec = IvParameterSpec(IV_192)
        val cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)

        val outputStream = ByteArrayOutputStream()
        val cipherOutputStream = CipherOutputStream(outputStream, cipher)
        val writer = OutputStreamWriter(cipherOutputStream, StandardCharsets.UTF_8)

        writer.write(value)
        writer.flush()
        cipherOutputStream.flush()
        cipherOutputStream.close()
        return android.util.Base64.encodeToString(outputStream.toByteArray(), android.util.Base64.DEFAULT).trim()
    }

    fun decryptTripleDES(value: String): String {
        if (value.isEmpty()) return ""

        val secretKey = SecretKeySpec(KEY_192, "DESede")
        val ivSpec = IvParameterSpec(IV_192)
        val cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)

        val decodedBytes = android.util.Base64.decode(value, android.util.Base64.DEFAULT)
        val inputStream = ByteArrayInputStream(decodedBytes)
        val cipherInputStream = CipherInputStream(inputStream, cipher)
        val reader = InputStreamReader(cipherInputStream, StandardCharsets.UTF_8)

        return reader.readText()
    }
}
