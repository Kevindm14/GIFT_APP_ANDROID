package com.example.gift_app_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gift_app_android.adapters.EventAdapter
import com.example.gift_app_android.api.ServiceBuilder
import com.example.gift_app_android.databinding.ActivityProfileBinding
import com.example.gift_app_android.models.Event
import com.example.gift_app_android.models.EventResponse
import com.example.gift_app_android.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private var eventList = listOf<Event>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ServiceBuilder.instance.listEvents("Bearer "+ SharedPrefManager.getInstance(this).token)
            .enqueue(object: Callback<EventResponse> {
                override fun onResponse(call: Call<EventResponse>, response: retrofit2.Response<EventResponse>) {
                    Toast.makeText(applicationContext, response.message(), Toast.LENGTH_LONG).show()
                    eventList = response.body()?.events!!
                    initRecyclerView()

                    binding.totalEvents.text = "${eventList.size} Eventos"

                    println(eventList[0].Gift)
                }

                override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

            })

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
        recyclerView.layoutManager = GridLayoutManager(this, 2)
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

    private fun listEvents() {


    }
}