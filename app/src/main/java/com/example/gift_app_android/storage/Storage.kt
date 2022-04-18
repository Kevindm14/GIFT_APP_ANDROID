package com.example.gift_app_android.storage

import android.content.Context
import com.example.gift_app_android.models.User

class SharedPrefManager private constructor(private val mCtx: Context) {

    val isLoggedIn: Boolean
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString("token", "") != ""
        }

    var token: String = ""
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString("token", null).toString()
        }

    val user: User
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return User(
                sharedPreferences.getString("id", null).toString(),
                sharedPreferences.getString("first_name", null).toString(),
                sharedPreferences.getString("last_name", null).toString(),
                sharedPreferences.getString("password", null).toString(),
                sharedPreferences.getString("email", null).toString(),
                sharedPreferences.getString("phone_number", null).toString(),
                sharedPreferences.getString("phone_extension", null).toString(),
//                sharedPreferences.getString("createdAt", null).toString(),
//                sharedPreferences.getString("updatedAt", null).toString(),
            )
        }

    fun saveToken(token: String) {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()


        editor.putString("token", token)

        editor.apply()
    }

    fun saveUser(user: User) {

        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("id", user.id)
        editor.putString("first_name", user.firstName)
        editor.putString("last_name", user.lastName)
        editor.putString("email", user.email)
        editor.putString("phone_number", user.workPhone)
        editor.putString("phone_extension", user.phoneExtension)

        editor.apply()

    }

    fun clear() {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    companion object {
        private val SHARED_PREF_NAME by lazy { "my_shared_pref" }
        private var mInstance: SharedPrefManager? = null
        @Synchronized
        fun getInstance(mCtx: Context): SharedPrefManager {
            if (mInstance == null) {
                mInstance = SharedPrefManager(mCtx)
            }
            return mInstance as SharedPrefManager
        }
    }

}