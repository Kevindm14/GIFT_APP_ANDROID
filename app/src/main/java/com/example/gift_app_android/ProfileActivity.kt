package com.example.gift_app_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gift_app_android.adapters.EventAdapter
import com.example.gift_app_android.api.ServiceBuilder
import com.example.gift_app_android.databinding.ActivityProfileBinding
import com.example.gift_app_android.models.Event
import com.example.gift_app_android.models.EventResponse
import com.example.gift_app_android.storage.SharedPrefManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private var eventList = listOf<Event>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CoroutineScope(Dispatchers.Main).launch {
            val call = ServiceBuilder.instance.listEvents("Bearer "+ SharedPrefManager.getInstance(applicationContext).token)
            val resEvent = call.body()

            if (call.isSuccessful) {
                eventList = resEvent?.events!!
                println(eventList)
                initRecyclerView()

                binding.totalEvents.text = "${eventList.size} Eventos"
            } else {
                println("==== ${call.message()}")
            }
        }

        binding.btnNewEvent.setOnClickListener {
            val intent = Intent(applicationContext, NewEventActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }

        binding.btnNewGift.setOnClickListener {
            val intent = Intent(applicationContext, NewGiftActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            SharedPrefManager.getInstance(this).clear()

            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }
    }

    private fun initRecyclerView() {
        val recyclerView = binding.eventslist
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = EventAdapter(eventList) { event ->
            onClick(event)
        }
    }

    private fun onClick(event: Event) {
        val intent = Intent(this, EventActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        intent.putExtra("event", event)

        startActivity(intent)
    }
}