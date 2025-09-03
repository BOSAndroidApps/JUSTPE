package com.bos.payment.appName.ui.view.Dashboard.Wallet.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bos.payment.appName.databinding.FragmentTransferAmountFromBinding


class TransferAmountFromFragment : Fragment() {
    private var binding: FragmentTransferAmountFromBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTransferAmountFromBinding.inflate(inflater,container,false)
        return binding!!.root
    }
}