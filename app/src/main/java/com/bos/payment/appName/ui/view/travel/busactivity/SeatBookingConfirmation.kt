package com.bos.payment.appName.ui.view.travel.busactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bos.payment.appName.databinding.ActivitySeatBookingConfirmationBinding

class SeatBookingConfirmation : AppCompatActivity() {
    private lateinit var bin: ActivitySeatBookingConfirmationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bin = ActivitySeatBookingConfirmationBinding.inflate(layoutInflater)
        setContentView(bin.root)
        initView()
        btnListener()
    }

    private fun btnListener() {

    }

    private fun initView() {

    }
}