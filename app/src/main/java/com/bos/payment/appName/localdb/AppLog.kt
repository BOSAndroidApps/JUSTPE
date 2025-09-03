package com.bos.payment.appName.localdb

import android.app.Activity
import android.app.Application
import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

object AppLog {
    lateinit var logFile: File
    private lateinit var context: Context

    fun Application.initialize() {
        this@AppLog.context = this
    }

    fun File.openTxtFile(activity:Activity) {
        AppLog.d(TAG, "opening TXT file")
        try {
            val uri = FileProvider.getUriForFile(
                activity,
                context.packageName + ".fileprovider",
                this
            )

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/*")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            }

            activity.startActivity(Intent.createChooser(intent, "Open TXT"))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(activity, "No TXT viewer installed", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun AppLog.d(tag: String?, msg: String) {
        saveLogToFile("DEBUG: $tag", msg)
        Log.d(tag, msg)
    }

    fun AppLog.v(tag: String?, msg: String) {
        saveLogToFile("VERBOSE : $tag", msg)
        Log.v(tag, msg)
    }

    fun AppLog.i(tag: String?, msg: String) {
        saveLogToFile("INFO : $tag", msg)
        Log.i(tag, msg)
    }

    fun AppLog.e(tag: String?, msg: String) {
        saveLogToFile("ERROR : $tag", msg)
        Log.e(tag, msg)
    }

    private fun saveLogToFile(tag: String?, msg: String) {
        val pid = android.os.Process.myPid()
        val logFileName = "my_app_logcat.txt"

        // Use external files dir if available
        /*val pdfDir = File(context.getExternalFilesDir(null), "txts")*/

        val pdfDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        var logFile = File(pdfDir, "my_app_logcat.txt")

        if (!pdfDir.exists()) {
            pdfDir.mkdirs()
        }

        logFile = File(pdfDir, logFileName)

        try {
            try {
                val writer = BufferedWriter(FileWriter(logFile, true))

                var line = "($pid) : ${tag ?: ""} : $msg"
                // Filter lines for only current app process ID
                if (line.contains("($pid)")) {
                    writer.write(line)
                    writer.newLine()
                }

                writer.flush()
                writer.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}