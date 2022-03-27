package com.example.gift_app_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gift_app_android.adapters.EventAdapter
import com.example.gift_app_android.databinding.ActivityProfileBinding
import com.example.gift_app_android.models.Event
import com.example.gift_app_android.storage.SharedPrefManager

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    private var eventList = listOf(
        Event("Event 1", "Description 1"),
        Event("Event 2", "Description 2"),
        Event("Event 3", "Description 3"),
        Event("Event 4", "Description 4"),
        Event("Event 5", "Description 5"),
        Event("Event 6", "Description 6"),
        Event("Event 7", "Description 7"),
        Event("Event 8", "Description 8"),
        Event("Event 9", "Description 9"),
        Event("Event 10", "Description 10"),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()

        binding.totalEvents.text = "${eventList.size} Eventos"

        binding.btnNewEvent.setOnClickListener {
            val intent = Intent(applicationContext, NewEventActivity::class.java)
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
}