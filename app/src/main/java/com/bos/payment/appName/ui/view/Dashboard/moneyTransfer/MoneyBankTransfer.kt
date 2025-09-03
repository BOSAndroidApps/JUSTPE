package com.bos.payment.appName.ui.view.Dashboard.moneyTransfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.bos.payment.appName.databinding.FragmentMoneyBankTransferBinding

class MoneyBankTransfer : Fragment() {
    private lateinit var binding: FragmentMoneyBankTransferBinding
    private lateinit var webView: WebView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMoneyBankTransferBinding.inflate(inflater, container, false)
        val rootView = binding.root

        webView = binding.webView

        webView.loadUrl("https://bos.center/")
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.settings.setSupportZoom(true)

        // Handle back press in fragment
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (webView.canGoBack()) {
                        webView.goBack()
                    } else {
                        isEnabled = false
                        requireActivity().onBackPressed()
                    }
                }
            }
        )

        return rootView
    }
}
