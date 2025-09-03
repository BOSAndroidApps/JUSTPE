/*
package com.beastblocks.demoproject

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.beastblocks.demoproject.QRCodeGenerator.generateQRPdf
import com.beastblocks.demoproject.QRCodeGenerator.openPdfFile

class MainActivity : AppCompatActivity() {

    var headers: Array<String> = arrayOf("Name", "Age", "City")
    var data: Array<Array<String>> = arrayOf(
        arrayOf("John", "25", "New York"),
        arrayOf("Alice", "30", "London"),
        arrayOf("Bob", "28", "Tokyo"),
        arrayOf("Annu", "28", "Ballia")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        generateQR()
    }

    fun generateQR() {
        generateQRPdf(
            this,
            "This is my UPI ID 9971087811@ptaxis",
            data,
            headers,
            "9971087811@ptaxis"
        ) { QR ->
            QR.second?.let {
                Toast.makeText(this, "QR Generated", Toast.LENGTH_SHORT).show()
                it.openPdfFile(this)
            }
        }
    }
}*/
