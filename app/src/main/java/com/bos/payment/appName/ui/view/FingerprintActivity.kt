package com.bos.payment.appName.ui.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.bos.payment.appName.R
import com.bos.payment.appName.ui.view.Dashboard.activity.DashboardActivity
import com.bos.payment.appName.ui.view.Dashboard.activity.JustPeDashboard
import com.bos.payment.appName.utils.Utils.toast
import java.util.concurrent.Executor

class FingerprintActivity : FragmentActivity() {

    companion object {
        private const val PREFS_NAME = "MyPrefs"
        private const val FIRST_RUN_KEY = "isFirstRun"
        private const val BIOMETRIC_ENROLL_REQUEST_CODE = 1001
    }

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fingerprint)

        executor = ContextCompat.getMainExecutor(this)

        // Check if it's the first time the app is launched
        if (isFirstRun()) {
            if (!isBiometricEnrolled()) {
                promptEnableBiometric()  // Ask the user to enable biometrics
            }
            // Set first run as completed so the dialog won't show again
            setFirstRunComplete()
        }

        // Proceed with biometric authentication setup if biometrics are enrolled
        if (isBiometricEnrolled()) {
            setupBiometricPrompt()
            showBiometricPrompt()
        } else {
            Toast.makeText(this, "Biometric not enrolled. Please enable it first.", Toast.LENGTH_LONG).show()
        }
    }

    // SharedPreferences to check if the app is opened for the first time
    private fun isFirstRun(): Boolean {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        return prefs.getBoolean(FIRST_RUN_KEY, true)
    }

    private fun setFirstRunComplete() {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit()
        prefs.putBoolean(FIRST_RUN_KEY, false)
        prefs.apply()
    }

    // Check if biometric hardware is available and biometrics are enrolled
    private fun isBiometricEnrolled(): Boolean {
        val biometricManager = BiometricManager.from(this)
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Toast.makeText(this, "No biometric hardware available", Toast.LENGTH_LONG).show()
                false
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Toast.makeText(this, "Biometric hardware is unavailable", Toast.LENGTH_LONG).show()
                false
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> false
            else -> false
        }
    }

    // Prompt the user to go to settings to enable biometrics
    private fun promptEnableBiometric() {
        AlertDialog.Builder(this)
            .setTitle("Enable Biometric Authentication")
            .setMessage("Biometric authentication is not enabled. Would you like to enable it now?")
            .setPositiveButton("Yes") { _, _ ->
                val intent = Intent(Settings.ACTION_BIOMETRIC_ENROLL)
                startActivityForResult(intent, BIOMETRIC_ENROLL_REQUEST_CODE)
            }
            .setNegativeButton("No") { _, _ ->
                restartSplashScreen()
            }
            .show()
    }

    // Handle the result of the biometric enrollment activity
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == BIOMETRIC_ENROLL_REQUEST_CODE) {
            // Check again if biometric is now enrolled
            if (isBiometricEnrolled()) {
                setupBiometricPrompt()
                showBiometricPrompt()
            } else {
                Toast.makeText(this, "Biometric enrollment failed or was not completed", Toast.LENGTH_LONG).show()
                restartSplashScreen()
            }
        }
    }

    // Setup the biometric prompt using BiometricPrompt
    private fun setupBiometricPrompt() {
        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(this@FingerprintActivity, "Authentication error: $errString", Toast.LENGTH_LONG).show()
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON || errorCode == BiometricPrompt.ERROR_USER_CANCELED) {
                    restartSplashScreen()
                }
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                toast("Authentication Successful")
                //startActivity(Intent(this@FingerprintActivity, DashboardActivity::class.java))
                startActivity(Intent(this@FingerprintActivity, JustPeDashboard::class.java))
                finish()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(this@FingerprintActivity, "Authentication failed. Please try again.", Toast.LENGTH_LONG).show()
            }
        })

        // Set the prompt information
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authentication required")
            .setSubtitle("Verify Identity")
            .setDescription("Confirm your phone screen lock, pattern, PIN or password")
            .setNegativeButtonText("Cancel")
            .build()
    }

    // Show the biometric prompt to the user
    private fun showBiometricPrompt() {
        biometricPrompt.authenticate(promptInfo)
    }

    private fun restartSplashScreen() {
        val intent = Intent(this, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

}
