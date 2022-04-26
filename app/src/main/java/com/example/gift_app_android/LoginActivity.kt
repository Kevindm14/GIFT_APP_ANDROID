package com.example.gift_app_android

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.gift_app_android.api.ServiceBuilder
import com.example.gift_app_android.databinding.ActivityLoginBinding
import com.example.gift_app_android.models.Response
import com.example.gift_app_android.storage.SharedPrefManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback


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

            login()
        }
    }

    private fun login() {
        val jsonObject = JSONObject()
        jsonObject.put("email", binding.email.text.toString())
        jsonObject.put("password", binding.password.text.toString())

        val jsonObjectString = jsonObject.toString()

        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.Main).launch {
            val call = ServiceBuilder.instance.login(requestBody)
            val resLogin = call.body()

            if (call.isSuccessful) {
                var instancePrefManager = SharedPrefManager.getInstance(applicationContext)
                instancePrefManager.saveUser(resLogin?.user!!)
                instancePrefManager.saveToken(resLogin?.token!!)


                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                startActivity(intent)
            } else {
                binding.error.text = "Usuario no encontrado"
                binding.error.isVisible = true
            }
        }

    }
}