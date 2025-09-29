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

        // Load and parse menu data
//        parseMenuData(getMenuListData)
//        displayParentMenus()

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

//        val companyCode = mStash!!.getStringValue(Constants.CompanyCode, "")
//        Log.d("companyCode", companyCode.toString())
//        changeAppIcon(companyCode!!)

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
//        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
//        bottomNavigationView.menu.clear() // Clear existing menu
//
//        val menu = bottomNavigationView.menu
//        val menuItem = menu.add(Menu.NONE, R.id.nav_scan_pay, 0, "Scan and Pay")
//        menuItem.setIcon(R.drawable.qr_code_scanner)
//
//        val menuView = bottomNavigationView.getChildAt(0) as BottomNavigationMenuView
//        val itemView = menuView.getChildAt(1) as BottomNavigationItemView // Second item
//        itemView.removeAllViews() // Clear the default layout
//        LayoutInflater.from(this).inflate(R.layout.custom_menu_item, itemView, true)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

//        navController =
//        (supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment).navController
//        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

        try {
            Picasso.get().load(Constants.uploadImage).error(R.drawable.ic_d_profile).into(binding.nav.ownerPhoto)
        }
        catch (e: Exception) {
            e.printStackTrace()
        }

//        requestOption?.let {
//            Utils.setImgGlideLib(
//                Constants.uploadImage, binding.nav.ownerPhoto, this,
//                it
//            )
////            Constants.uploadImage = response.UploadPhoto.toString()
//        }

        try {
            val imageUrl = mStash!!.getStringValue(Constants.CompanyLogo, "")
            Picasso.get().load(imageUrl)
//            .placeholder(R.drawable.placeholder)  // Optional: placeholder while loading
                .error(R.drawable.no_image)        // Optional: error image if load fails
                .into(binding.toolbar.dashboardImage)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
//        mStash!!.getStringValue(Constants.CompanyCode, "")?.let {
//            changeAppIcon(it)
//        }

        fingerPrint = mStash!!.getBoolanValue(Constants.fingerPrintAction.toString(), false)
        binding.nav.switchButton.isChecked = fingerPrint
//        binding.nav.reportsLayout.visibility = View.GONE
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


//    @SuppressLint("NotifyDataSetChanged", "SuspiciousIndentation")
//    private fun getAllMenuListRes(response: GetAllMenuListRes) {
//        if (response.isSuccess == true){
//            response.data.let { menuList ->
//                getMenuListData.clear()
//                getMenuListData.addAll(menuList)
//                menuListAdapter.notifyDataSetChanged()
//            }
//        }else {
//            Toast.makeText(this, response.returnMessage.toString(), Toast.LENGTH_SHORT).show()
//        }
//    }


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
//                    if(!menuItem.childMenuCode!!.contains(menuItem.parentMenuCode.toString())) {
//                        getMenuListData.remove(menuItem)
//                    }else {
                        // Parent menu
                        parentMenus.add(menuItem)
//                    }
                } else {
                    // Child menu
                    childMenusMap
                        .getOrPut(menuItem.parentMenuCode!!) { mutableListOf() }
                        .add(menuItem)
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


        /*
        val cacheDir = this.cacheDir
        val subdirectory = File(cacheDir, "my_cache_files")
        if (!subdirectory.exists()) {
            subdirectory.mkdirs()
        }

        val fileName = "aopaytravel.txt"
        val file = File(subdirectory, fileName)


        try {
            file.bufferedWriter().use { writer ->
                writer.write(data)
                writer.newLine() // Add a new line
                //writer.append("More content can be appended here.")
            }
        } catch (e: IOException) {
            e.printStackTrace() // Handle potential IO errors
        }*/
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


//        binding.nav.dashboard.setOnClickListener {
//            startActivity(Intent(this, com.bos.payment.app.ui.view.Dashboard.GenerateQRCodeActivity::class.java))
//        }
        binding.toolbar.menuLayout.setOnClickListener {
            getAllMenuList()
            binding.drawer.openDrawer(GravityCompat.START)
        }
////        binding.nav.walletBalance.setOnClickListener { binding.drawer.closeDrawer(GravityCompat.START) }
        binding.nav.llLogout.setOnClickListener {
            binding.drawer.closeDrawer(GravityCompat.START)
//            MySharedPreference.clearData(context)
//            mStash!!.setStringValue(Constants.IS_LOGIN.toString(), "false")
            mStash!!.clear()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
//            finishAffinity()
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
//        binding.fab.setOnClickListener {
//            startActivity(Intent(this, ScannerFragment::class.java))
//
//        }
//        binding.nav.switchButton.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                MySharedPreference.setFingerPrint(context, true)
//            } else {
//                MySharedPreference.setFingerPrint(context, false)
//            }
//        }
//        if (MySharedPreference.getFingerPrint(context)) {
//            binding.nav.switchButton.isChecked = true
//        }
        binding.nav.customerName.text = mStash!!.getStringValue(Constants.AgentName, "")
        binding.toolbar.dashboardImage.setOnClickListener {
            supportFragmentManager.commit {
                replace(R.id.fragment, DashboardFragment())
                addToBackStack(null)
            }
        }

//        binding.nav.changePinBtn.setOnClickListener {
//            startActivity(Intent(this@DashboardActivity, ChangePin::class.java))
//        }
//        binding.nav.updateKYCBtn.setOnClickListener {
//            startActivity(Intent(this@DashboardActivity, UpdateKYC::class.java))
//        }
//        binding.nav.changePasswordBtn.setOnClickListener {
//            startActivity(Intent(this@DashboardActivity, ChangePassword::class.java))
//        }
//        binding.nav.serviceWiseReports.setOnClickListener {
//            startActivity(Intent(this@DashboardActivity, ServiceWiseTransaction::class.java))
//        }

        binding.nav.switchButton.setOnClickListener {
            fingerPrint = !fingerPrint // Toggle the value
            mStash!!.setBooleanValue(Constants.fingerPrintAction.toString(), fingerPrint)
        }


//        binding.nav.reportsImage.setOnClickListener {
//            reports = !reports
//            if (reports) {
//                binding.nav.reportsLayout.visibility = View.VISIBLE
//                binding.nav.reportsImage.setImageResource(R.drawable.down_arrow)
//            } else {
//                binding.nav.reportsLayout.visibility = View.GONE
//                binding.nav.reportsImage.setImageResource(R.drawable.left_arrow)
//            }
//        }
//        binding.nav.rechargeAndBill.setOnClickListener {
//            supportFragmentManager.commit {
//                binding.drawer.closeDrawer(GravityCompat.START)
//                replace(R.id.fragment, RechargeAndBillPaymentReportFragment()).addToBackStack(null)
//            }
//        }
//        binding.nav.makePaymentBtn.setOnClickListener {
//            startActivity(Intent(this, MakePayment::class.java))
//        }
    }


    private fun createMenu() {
        val popUp = PopupMenu(this, binding.toolbar.sideMoverBtn)
        popUp.menuInflater.inflate(R.menu.option_menu, popUp.menu)
        popUp.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.profile_menu1 -> {
                    startActivity(Intent(this, DashboardActivity::class.java))
//                    navController.navigate(R.id.profileFragment)
//                    binding.bottomNavigationView.selectedItemId = R.id.profile_menu
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
