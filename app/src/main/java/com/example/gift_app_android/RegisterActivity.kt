package com.example.gift_app_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.gift_app_android.api.ServiceBuilder
import com.example.gift_app_android.databinding.ActivityRegisterBinding
import com.example.gift_app_android.models.User
import com.example.gift_app_android.storage.SharedPrefManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }

        binding.btnSignup.setOnClickListener {
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

            val jsonObject = JSONObject()
            jsonObject.put("email", binding.email.text.toString())
            jsonObject.put("password", binding.password.text.toString())

            val jsonObjectString = jsonObject.toString()
            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

            CoroutineScope(Dispatchers.IO).launch {
                val call = ServiceBuilder.instance.register(requestBody)
                val resRegister = call.body()

                if (call.isSuccessful) {
                    println(resRegister?.message)
                    var instancePrefManager = SharedPrefManager.getInstance(applicationContext)
                    instancePrefManager.saveUser(resRegister?.user!!)
                    instancePrefManager.saveToken(resRegister?.token!!)

                    val intent = Intent(applicationContext, ProfileActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                    startActivity(intent)
                }
            }
        }
    }
}