package com.bos.payment.appName.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import com.bos.payment.appName.data.model.loginSignUp.LoginRes
import com.bos.payment.appName.data.model.travel.flight.TripDetailsItem
import com.bos.payment.appName.localdb.MySharedPreference
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

open class MStash(mContext: Context) {

    val prefFile = "com.ayuvya.ayuvyascanner.Prefs"
    private val preferences: SharedPreferences = mContext.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor? = preferences.edit()

    /**
     * to get key is stored in DB
     */
    fun getContainsKey(key: String?): Boolean {
        return preferences.contains(key)
    }

    /**
     * Stores String value in preference
     *
     * @param key   key of preference
     * @param value value for that key
     */
    fun setValue(key: String?, value: String?) {
        editor!!.putString(key, value)
        editor.commit()
    }

    /**
     * Stores String value in preference
     *
     * @param key   key of preference
     * @param value value for that key
     */
    fun setStringValue(key: String?, value: String?) {
        editor!!.putString(key, value)
        editor.commit()
    }

    /**
     * Stores int value in preference
     *
     * @param key   key of preference
     * @param value value for that key
     */
    fun setValue(key: String?, value: Int) {
        editor!!.putInt(key, value)
        editor.commit()
    }

    /**
     * Stores Double value in String format in preference
     *
     * @param key   key of preference
     * @param value value for that key
     */
    fun setValue(key: String?, value: Double) {
        setValue(key, java.lang.Double.toString(value))
    }

    /**
     * Stores long value in preference
     *
     * @param key   key of preference
     * @param value value for that key
     */
    fun setValue(key: String?, value: Long) {
        editor!!.putLong(key, value)
        editor.commit()
    }

    /**
     * Stores boolean value in preference
     *
     * @param key   key of preference
     * @param value value for that key
     */
    fun setValue(key: String?, value: Boolean) {
        editor!!.putBoolean(key, value)
        editor.commit()
    }

    /**
     * Retrieves String value from preference
     *
     * @param key          key of preference
     * @param defaultValue default value if no key found
     */
    fun getStringValue(key: String?, defaultValue: String?): String? {
        return preferences.getString(key, defaultValue)
    }

    /**
     * Retrieves int value from preference
     *
     * @param key          key of preference
     * @param defaultValue default value if no key found
     */
    fun getIntValue(key: String?, defaultValue: Int): Int {
        return preferences.getInt(key, defaultValue)
    }

    /**
     * Retrieves long value from preference
     *
     * @param key          key of preference
     * @param defaultValue default value if no key found
     */
    fun getLongValue(key: String?, defaultValue: Long): Long {
        return preferences.getLong(key, defaultValue)
    }

    /**
     * Retrieves boolean value from preference
     *
     * @param keyFlag      key of preference
     * @param defaultValue default value if no key found
     */
    fun getBoolanValue(keyFlag: String?, defaultValue: Boolean): Boolean {
        return preferences.getBoolean(keyFlag, defaultValue)
    }


    fun setBooleanValue(keyFlag: String?, value: Boolean) {
        preferences.edit().putBoolean(keyFlag, value).apply()
    }

    /**
     * Removes key from preference
     *
     * @param key key of preference that is to be deleted
     */
    fun removeKey(key: String?) {
        if (editor != null) {
            editor.remove(key)
            editor.commit()
        }
    }

    /**
     * Clears all the preferences stored
     */
    fun clear() {
        editor!!.clear().commit()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var mMStash: MStash? = null

        /**
         * Creates single instance of MStash
         *
         * @param context context of Activity or Service
         * @return Returns instance of MStash
         */
        @Synchronized
        fun getInstance(context: Context): MStash? {
            if (mMStash == null) {
                mMStash = MStash(context.applicationContext)
            }
            return mMStash
        }
    }
    fun setVariableInPreferences(key: String?, value: String?, context: Context) {
        val sharedPreferences = context.getSharedPreferences(prefFile, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getVariablesInPreferences(key: String, context: Context?): String {
        return if (context != null) {
            val sharedPreferences = context.getSharedPreferences(prefFile, Context.MODE_PRIVATE)
            val value = sharedPreferences.getString(key, null) ?: return ""
            value
        } else {
            ""
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    fun getUserModelData(context: Context): LoginRes{
        val result = MySharedPreference.initializeSharedPreference(context)!!.getString("LoginModel", null)
        return Gson().fromJson(result, LoginRes::class.java) ?: return LoginRes()
    }


    fun setFlightSearchData(key:String, list: List<TripDetailsItem>?, context:Context){
        val sharedPreferences = context.getSharedPreferences(prefFile, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(list)
        editor.putString(key, json)
        editor.apply()
    }


    fun getFlightSearchTripList(key: String, context: Context): List<TripDetailsItem> {
        val sharedPreferences = context.getSharedPreferences(prefFile, Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(key, null)
        return if (json != null) {
            val type = object : TypeToken<List<TripDetailsItem>>() {}.type
            Gson().fromJson(json, type)
        } else {
            emptyList()
        }
    }


}