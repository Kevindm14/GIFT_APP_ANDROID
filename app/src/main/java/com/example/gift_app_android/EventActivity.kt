package com.example.gift_app_android

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import com.bumptech.glide.Glide
import com.example.gift_app_android.databinding.ActivityEventBinding
import com.example.gift_app_android.models.Event

class EventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }

        val event = intent.extras?.get("event") as Event

        println(event.Gift)

        val imageBytes = Base64.decode(event.Gift.qr, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        binding.myEvent.text = event.title
        binding.eventDescription.text = event.description
        Glide
            .with(binding.giftImg.context)
            .load(decodedImage)
            .into(binding.giftImg)
    }
}