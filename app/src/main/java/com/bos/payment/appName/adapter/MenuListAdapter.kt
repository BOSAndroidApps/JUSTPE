package com.bos.payment.appName.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bos.payment.appName.data.model.menuList.Data
import com.bos.payment.appName.databinding.DrawerSliderItemLayoutBinding
import com.bos.payment.appName.ui.view.CreditCardDetailsFragment
import com.bos.payment.appName.ui.view.Dashboard.activity.DashboardActivity
import com.bos.payment.appName.ui.view.Dashboard.activity.GenerateQRCodeActivity
import com.bos.payment.appName.ui.view.Dashboard.activity.ServiceWiseTransaction
import com.bos.payment.appName.ui.view.Dashboard.Wallet.Fragment.RechargeFragment
import com.bos.payment.appName.ui.view.Dashboard.dmt.PayoutDMT
import com.bos.payment.appName.ui.view.moneyTransfer.ScannerFragment

class MenuListAdapter(
    private val context: Context,
    private val menuList: MutableList<Data>,
    private val fragmentManager: FragmentManager,
    private val containerId: Int
) : RecyclerView.Adapter<MenuListAdapter.MenuListViewHolder>() {

    private var lastExpandedPosition: Int? = null // Track last expanded menu position

    inner class MenuListViewHolder(val binding: DrawerSliderItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuListViewHolder {
        val binding = DrawerSliderItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return MenuListViewHolder(binding)
    }

    override fun getItemCount(): Int = menuList.size

    override fun onBindViewHolder(holder: MenuListViewHolder, position: Int) {
        val menuItem = menuList[position]

        // Set menu text
        holder.binding.dashboard.text = menuItem.menuText ?: "N/A"

        // Adjust margin for child menus
        val layoutParams = holder.binding.root.layoutParams as ViewGroup.MarginLayoutParams
        if (menuItem.parentMenuCode.isNullOrEmpty()) {
            layoutParams.setMargins(0, 0, 0, 0) // Parent menu: no margin
        } else {
            val leftMarginInPx = (20 * context.resources.displayMetrics.density).toInt()
            layoutParams.setMargins(leftMarginInPx, 0, 0, 0) // Child menu: add left margin
        }
        holder.binding.root.layoutParams = layoutParams

        // Handle click for expanding/collapsing
        holder.itemView.setOnClickListener {
            if (menuItem.parentMenuCode.isNullOrEmpty()) {
                // Parent menu clicked
                if (menuItem.isExpanded) {
                    collapseChildren(position)
                } else {
                    collapsePreviousMenu() // Collapse only previously expanded menu
                    expandChildren(position, menuItem.childMenus)
                }
            } else {
                // Child menu clicked -> Navigate
                when (menuItem.childMenuCode) {
                    "M00011" -> context.startActivity(Intent(context, PayoutDMT::class.java))
                    "M00012" -> context.startActivity(Intent(context, ScannerFragment::class.java))
                    "M00013" -> navigateToFragment(CreditCardDetailsFragment(), "CreditCard")
                    "M00014" -> context.startActivity(Intent(context, GenerateQRCodeActivity::class.java))
                    "M00030" -> context.startActivity(Intent(context, ServiceWiseTransaction::class.java))
                    "M00031" -> context.startActivity(Intent(context, ServiceWiseTransaction::class.java))
                    "M00008" -> context.startActivity(Intent(context, DashboardActivity::class.java))
                    "M00042" -> context.startActivity(Intent(context, DashboardActivity::class.java))
                    "M00009" -> context.startActivity(Intent(context, DashboardActivity::class.java))
                    "M00010" -> navigateToFragment(RechargeFragment(), "FastTag")
                    else -> { /* Handle other cases */ }
                }
            }
        }
    }

    // Navigation logic
    private fun navigateToFragment(fragment: Fragment, rechargeType: String) {
        fragmentManager.beginTransaction()
            .replace(containerId, fragment)
            .addToBackStack(null)
            .commit()
    }

    // Expand children for the clicked parent
    private fun expandChildren(parentPosition: Int, children: List<Data>?) {
        if (!children.isNullOrEmpty() && !menuList[parentPosition].isExpanded) {
            menuList[parentPosition].isExpanded = true
            menuList.addAll(parentPosition + 1, children)
            notifyItemRangeInserted(parentPosition + 1, children.size)
            lastExpandedPosition = parentPosition // Update last expanded position
        }
    }

    private fun collapseChildren(parentPosition: Int) {
        val parentMenu = menuList[parentPosition]
        if (!parentMenu.isExpanded) return

        val startIndex = parentPosition + 1
        val childCount = parentMenu.childMenus?.size ?: 0

        if (startIndex < menuList.size) {
            menuList.subList(startIndex, minOf(menuList.size, startIndex + childCount)).clear()
            notifyItemRangeRemoved(startIndex, childCount)
        }
        parentMenu.isExpanded = false
    }

    // Collapse only the last expanded menu before expanding a new one
    private fun collapsePreviousMenu() {
        lastExpandedPosition?.let {
            if (menuList[it].isExpanded) {
                collapseChildren(it)
                lastExpandedPosition = null // Reset last expanded menu
            }
        }
    }
}
