package com.example.gift_app_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gift_app_android.databinding.ActivityLoginBinding
import com.example.gift_app_android.databinding.ActivityRegisterBinding
import com.example.gift_app_android.models.User
import com.example.gift_app_android.storage.SharedPrefManager

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

            var user = User(binding.email.text.toString().trim(), binding.password.text.toString().trim())
            SharedPrefManager.getInstance(applicationContext).saveUser(user)

            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }
    }
}