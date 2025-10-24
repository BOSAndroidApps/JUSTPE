package com.bos.payment.appName.ui.view.Dashboard.activity

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.PopupMenu
import android.widget.Toast
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bos.payment.appName.ui.view.LoginActivity
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.MStash
import com.bos.payment.appName.R
import com.bos.payment.appName.adapter.MenuListAdapter
import com.bos.payment.appName.data.model.menuList.Data
import com.bos.payment.appName.data.model.menuList.GetAllMenuListReq
import com.bos.payment.appName.data.model.menuList.GetAllMenuListRes
import com.bos.payment.appName.data.model.walletBalance.walletBalanceCal.GetBalanceReq
import com.bos.payment.appName.data.model.walletBalance.walletBalanceCal.GetBalanceRes
import com.bos.payment.appName.data.repository.GetAllAPIServiceRepository
import com.bos.payment.appName.data.viewModelFactory.GetAllApiServiceViewModelFactory
import com.bos.payment.appName.databinding.ActivityDashboardBinding
import com.bos.payment.appName.network.RetrofitClient
import com.bos.payment.appName.ui.view.fragment.DashboardFragment
import com.bos.payment.appName.ui.view.moneyTransfer.ScannerFragment
import com.bos.payment.appName.ui.view.travel.flightBooking.activity.FlightDetailListActivity.Companion.context
import com.bos.payment.appName.ui.view.travel.flightBooking.activity.FlightFilterActivity.Companion.TAG
import com.bos.payment.appName.ui.viewmodel.GetAllApiServiceViewModel
import com.bos.payment.appName.utils.ApiStatus
import com.bos.payment.appName.utils.Constants.uploadDataOnFirebaseConsole
import com.bos.payment.appName.utils.Utils.PD
import com.bos.payment.appName.utils.Utils.logout
import com.bos.payment.appName.utils.Utils.runIfConnected
import com.bos.payment.appName.utils.Utils.toast
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private var doubleBackToExitPressedOnce: Boolean = false
    private var mStash: MStash? = null
    private var fingerPrint: Boolean = false
    private lateinit var pd: AlertDialog
    private lateinit var getAllApiServiceViewModel: GetAllApiServiceViewModel
    private lateinit var menuListAdapter: MenuListAdapter
    private lateinit var getMenuListData: ArrayList<Data>
    lateinit var toolbar: Toolbar
    lateinit var storageRef : StorageReference


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        getWalletBalance()
        getfirebasetoken()
        btnListener()

    }



    fun getfirebasetoken(){
        FirebaseFirestore.setLoggingEnabled(true)
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.d(TAG, "FCM Token: $token")

            // Send this token to your server if you need to send targeted notifications
            // sendRegistrationToServer(token)
        }
        val storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
    }


    @SuppressLint("RestrictedApi")
    private fun initView() {
        pd = PD(this)
        getMenuListData = ArrayList()
        mStash = MStash.getInstance(this@DashboardActivity)

        val fragment = DashboardFragment()
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()

        transaction.replace(R.id.fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()


        getAllApiServiceViewModel = ViewModelProvider(this, GetAllApiServiceViewModelFactory(GetAllAPIServiceRepository(RetrofitClient.apiAllInterface)))[GetAllApiServiceViewModel::class.java]

        binding.nav.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        menuListAdapter = MenuListAdapter(this, getMenuListData, fragmentManager = supportFragmentManager, containerId = R.id.fragment)
        binding.nav.recyclerView.adapter = menuListAdapter

        try {
            val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            val menuView = bottomNavView.getChildAt(0) as BottomNavigationMenuView

            for (i in 0 until menuView.childCount) {
                val itemView = menuView.getChildAt(i) as BottomNavigationItemView
                val iconView = itemView.findViewById<View>(R.id.icon)
                val params = iconView.layoutParams as FrameLayout.LayoutParams
                params.topMargin = 90 // Add 40dp gap
                iconView.layoutParams = params
            }

        } catch (e: NullPointerException) {
            e.printStackTrace()
        }


        try {
            Picasso.get().load(Constants.uploadImage).error(R.drawable.ic_d_profile).into(binding.nav.ownerPhoto)
        }
        catch (e: Exception) {
            e.printStackTrace()
        }


        try {
            val imageUrl = mStash!!.getStringValue(Constants.CompanyLogo, "")
            Picasso.get().load(imageUrl)
//            .placeholder(R.drawable.placeholder)  // Optional: placeholder while loading
                .error(R.drawable.no_image)        // Optional: error image if load fails
                .into(binding.toolbar.dashboardImage)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }


        fingerPrint = mStash!!.getBoolanValue(Constants.fingerPrintAction.toString(), false)
        binding.nav.switchButton.isChecked = fingerPrint

    }


    private fun getAllMenuList() {
        val getAllMenuListReq = GetAllMenuListReq(
            loginId = mStash!!.getStringValue(Constants.RegistrationId, ""),
            "Bos"
        )
        Log.d("getAllMenuListReq", Gson().toJson(getAllMenuListReq))
        getAllApiServiceViewModel.getAllMenuList(getAllMenuListReq).observe(this) { resource ->
            resource?.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        pd.dismiss()
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                getAllMenuListRes(response)
                            }
                        }
                    }

                    ApiStatus.ERROR -> {
                        pd.dismiss()
                    }

                    ApiStatus.LOADING -> {
                        pd.show()
                    }
                }
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun getAllMenuListRes(response: GetAllMenuListRes) {
        if (response.isSuccess == true) {
            // Clear the current menu list
            getMenuListData.clear()

            // Map to hold child menus by parentMenuCode
            val childMenusMap = mutableMapOf<String, MutableList<Data>>()

            //Separate parent menus and child menus
            val parentMenus = mutableListOf<Data>()
            val childMenus = mutableListOf<Data>()

            // Separate parent menus and child menus
            response.data.forEach { menuItem ->
                if (menuItem.parentMenuCode.isNullOrEmpty()) {
                    parentMenus.add(menuItem)
                } else {
                    childMenusMap.getOrPut(menuItem.parentMenuCode!!) { mutableListOf() }.add(menuItem)
                    childMenus.add(menuItem)
                }
            }

            //Filter parent menus: keep only those that have at least one child menu
            val validParentMenus = parentMenus.filter { parentMenu ->
                childMenus.any { it.parentMenuCode == parentMenu.childMenuCode}
            }

            //Attach child menus to parent menu
            validParentMenus.forEach { parentMenu ->
                parentMenu.childMenus = childMenusMap[parentMenu.childMenuCode]?: mutableListOf()
            }

            //Add filtered child menu to parent menus
            getMenuListData.addAll(validParentMenus)
            // Notify the adapter
            menuListAdapter.notifyDataSetChanged()
        } else {
            // Show an error message
            Toast.makeText(this, response.returnMessage ?: "Error occurred", Toast.LENGTH_SHORT).show()
        }
    }


    private fun getWalletBalance() {
        this.runIfConnected {
            val walletBalanceReq = GetBalanceReq(
                parmUser = mStash!!.getStringValue(Constants.RegistrationId, ""),
                flag = "CreditBalance"
            )
            getAllApiServiceViewModel.getWalletBalance(walletBalanceReq).observe(this) { resource ->
                resource?.let {
                    when (it.apiStatus) {
                        ApiStatus.SUCCESS -> {
                            pd.dismiss()
                            it.data?.let { users ->
                                users.body()?.let { response ->
                                    getAllWalletBalanceRes(response)
                                }
                            }
                        }

                        ApiStatus.ERROR -> {
                            pd.dismiss()
                        }

                        ApiStatus.LOADING -> {
                            pd.show()
                        }
                    }
                }
            }
        }
    }


    @SuppressLint("SetTextI18n", "DefaultLocale")
    private fun getAllWalletBalanceRes(response: GetBalanceRes) {
        val data = Gson().toJson(response)
        uploadDataOnFirebaseConsole(data,"DashboardWalletBalance",this@DashboardActivity)

        if (response.isSuccess == true) {
            binding.nav.walletBalance.text = "₹" + response.data[0].result.toString()
            Log.d("actualBalance", response.data[0].result.toString())
        } else {
            toast(response.returnMessage.toString())
        }


    }


    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun btnListener() {
        val virtualAmount = mStash!!.getStringValue(Constants.retailerMainVirtualAmount, "")
        Log.d("virtualAmount", virtualAmount.toString())

        binding.toolbar.menuLayout.setOnClickListener {
            getAllMenuList()
            binding.drawer.openDrawer(GravityCompat.START)
        }

        binding.nav.llLogout.setOnClickListener {
            binding.drawer.closeDrawer(GravityCompat.START)
            mStash!!.clear()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.toolbar.sideMoverBtn.setOnClickListener {
            createMenu()
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
//                    navController.popBackStack()
                    startActivity(Intent(this, DashboardActivity::class.java))
                }

                R.id.nav_scan_pay -> {
//                    navController.popBackStack()
                    startActivity(Intent(this, ScannerFragment::class.java))
                }

                R.id.nav_history -> {
//                    navController.popBackStack()
                    startActivity(Intent(this, RechargeHistory::class.java))
                }
            }
            return@setOnItemSelectedListener true
        }


        binding.nav.customerName.text = mStash!!.getStringValue(Constants.AgentName, "")

        binding.toolbar.dashboardImage.setOnClickListener {
            supportFragmentManager.commit {
                replace(R.id.fragment, DashboardFragment())
                addToBackStack(null)
            }
        }



        binding.nav.switchButton.setOnClickListener {
            fingerPrint = !fingerPrint // Toggle the value
            mStash!!.setBooleanValue(Constants.fingerPrintAction.toString(), fingerPrint)
        }



    }


    private fun createMenu() {
        val popUp = PopupMenu(this, binding.toolbar.sideMoverBtn)
        popUp.menuInflater.inflate(R.menu.option_menu, popUp.menu)
        popUp.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.profile_menu1 -> {
                    startActivity(Intent(this, DashboardActivity::class.java))

                }
                R.id.logout_menu -> {
                    logout(this)
                }
            }
            true
        }
        popUp.show()
    }


    fun restartApp(context: Context) {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent?.apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            Runtime.getRuntime().exit(0) // Optional: exit the process to avoid any lingering issues
        }
    }



    private fun changeAppIcon(newIcon: String) {
        val pm = applicationContext.packageManager

        when (newIcon) {
            "CMP1045" -> {
                pm.setComponentEnabledSetting(
                    ComponentName(this, "com.bos.bos.app.ui.view.SplashActivity"),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
                )

                pm.setComponentEnabledSetting(
                    ComponentName(this, "com.bos.bos.app.ui.view.UserType1Alias"),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                pm.setComponentEnabledSetting(
                    ComponentName(this, "com.bos.bos.app.ui.view.UserType2Alias"),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                pm.setComponentEnabledSetting(
                    ComponentName(this, "com.bos.bos.app.ui.view.UserType3Alias"),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
            }

            "CMP1085" -> {
                pm.setComponentEnabledSetting(
                    ComponentName(this, "com.bos.bos.app.ui.view.SplashActivity"),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )

                pm.setComponentEnabledSetting(
                    ComponentName(this, "com.bos.bos.app.ui.view.UserType1Alias"),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                pm.setComponentEnabledSetting(
                    ComponentName(this, "com.bos.bos.app.ui.view.UserType2Alias"),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                pm.setComponentEnabledSetting(
                    ComponentName(this, "com.bos.bos.app.ui.view.UserType3Alias"),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
                )
            }

            "CMP1347" -> {
                pm.setComponentEnabledSetting(
                    ComponentName(this, "com.bos.bos.app.ui.view.SplashActivity"),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                pm.setComponentEnabledSetting(
                    ComponentName(this, "com.bos.bos.app.ui.view.UserType1Alias"),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
                )
                pm.setComponentEnabledSetting(
                    ComponentName(this, "com.bos.bos.app.ui.view.UserType2Alias"),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                pm.setComponentEnabledSetting(
                    ComponentName(this, "com.bos.bos.app.ui.view.UserType3Alias"),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
            }

            "CMP1349" -> {
                pm.setComponentEnabledSetting(
                    ComponentName(this, "com.bos.bos.app.ui.view.SplashActivity"),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                pm.setComponentEnabledSetting(
                    ComponentName(this, "com.bos.bos.app.ui.view.UserType1Alias"),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )

                pm.setComponentEnabledSetting(
                    ComponentName(this, "com.bos.bos.app.ui.view.UserType2Alias"),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
                )
                pm.setComponentEnabledSetting(
                    ComponentName(this, "com.bos.bos.app.ui.view.UserType3Alias"),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
            }
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            finish()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, R.string.double_back_press_msg, Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }




}
