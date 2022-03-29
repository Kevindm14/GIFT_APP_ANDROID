package com.example.gift_app_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.gift_app_android.api.Api
import com.example.gift_app_android.api.ServiceBuilder
import com.example.gift_app_android.databinding.ActivityLoginBinding
import com.example.gift_app_android.models.User
import com.example.gift_app_android.storage.SharedPrefManager
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignup.setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            if (binding.email.text.isEmpty()) {
                binding.email.error = "Email is required"
                binding.email.requestFocus()

                return@setOnClickListener
            }

            if (binding.password.text.isEmpty()) {
                binding.password.error = "Password is required"
                binding.password.requestFocus()

                return@setOnClickListener
            }

//            var user = User(binding.email.text.toString().trim(), binding.password.text.toString().trim())
//            SharedPrefManager.getInstance(applicationContext).saveUser(user)
//
//            val intent = Intent(applicationContext, MainActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//
//            startActivity(intent)

            login()
        }
    }

    private fun login() {
        val request = ServiceBuilder.buildService(Api::class.java)
        val jsonObject = JSONObject()
        jsonObject.put("email", binding.email.text.toString())
        jsonObject.put("password", binding.password.text.toString())

        val jsonObjectString = jsonObject.toString()

        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())


        CoroutineScope(Dispatchers.IO).launch {
            val response = request.login(requestBody)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser().parse(response.body()?.string())
                    )

                    Log.d("Pretty Printed JSON :", prettyJson)

                } else {

                    Log.e("RETROFIT_ERROR", response.code().toString())

                }
            }
        }
    }
}