package com.bos.payment.appName.localdb

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

object MySharedPreference {
    private const val DATABASE = "BOS App"

    fun initializeSharedPreference(context: Context): SharedPreferences? {
        return try {
            var masterKeyAlias: String? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            }
            EncryptedSharedPreferences.create(
                DATABASE,
                masterKeyAlias!!,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    fun setUserModelData(context: Context, userModelData: LoginModel?) =
//        initializeSharedPreference(context)!!.edit().putString("LoginModel", Gson().toJson(userModelData)).apply()


    @RequiresApi(api = Build.VERSION_CODES.M)
    fun setFingerPrint(context: Context, isChecked: Boolean?) =
        initializeSharedPreference(context)!!.edit().putBoolean("isChecked", isChecked!!).apply()


    @RequiresApi(api = Build.VERSION_CODES.M)
    fun getFingerPrint(context: Context): Boolean {
        val result = initializeSharedPreference(context)!!.getBoolean("isChecked", false)
        return result
    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    fun getUserModelData(context: Context): LoginModel {
//        val result = initializeSharedPreference(context)!!.getString("LoginModel", null)
//        return Gson().fromJson(result, LoginModel::class.java) ?: return LoginModel()
//    }

    fun clearData(context: Context){
        initializeSharedPreference(context)!!.edit().clear().apply()
    }

}