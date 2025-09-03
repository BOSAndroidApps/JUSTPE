package com.bos.payment.appName.ui.view.Dashboard.Wallet.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bos.payment.appName.databinding.FragmentAmountTransferToBinding

class AmountTransferToFragment : Fragment() {
    private var binding: FragmentAmountTransferToBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAmountTransferToBinding.inflate(inflater, container, false)
        return binding!!.root
    }
}