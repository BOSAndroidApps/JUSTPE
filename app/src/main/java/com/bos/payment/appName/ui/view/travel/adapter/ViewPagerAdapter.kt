package com.bos.payment.appName.ui.view.travel.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bos.payment.appName.ui.view.travel.busfragment.CancelledRefundBus
import com.bos.payment.appName.ui.view.travel.busfragment.UpcomingBus

/*private const val NUM_TABS = 4*/

private const val NUM_TABS = 2

class ViewPagerAdapter(fm: FragmentManager,lifecycle: Lifecycle) : FragmentStateAdapter(fm,lifecycle) {


    override fun getItemCount(): Int {
        return NUM_TABS
    }


    override fun createFragment(position: Int): Fragment {
      /*  return when (position) {
            0 -> UpcomingBus()
            1 -> PendingBus()
            2 -> CancelledRefundBus()
            3 -> CompletedBus()
            else -> UpcomingBus()
        }*/

          return when (position) {
            0 -> UpcomingBus()
            else -> CancelledRefundBus()

        }

    }


}
