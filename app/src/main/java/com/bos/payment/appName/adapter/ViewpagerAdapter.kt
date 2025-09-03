package com.bos.payment.appName.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.bos.payment.appName.ui.view.Dashboard.Wallet.Fragment.AmountTransferToFragment
import com.bos.payment.appName.ui.view.Dashboard.Wallet.Fragment.TransferAmountFromFragment

class ViewpagerAdapter(fm: FragmentManager,context: Context) : FragmentPagerAdapter(fm) {
    var context: Context? = context

    override fun getItem(position: Int): Fragment {
        return if (position == 0) TransferAmountFromFragment() else AmountTransferToFragment()
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (position == 0) {
            "Transfer Amount From";
        } else {
            "Account Transfer To";
        }
    }
}