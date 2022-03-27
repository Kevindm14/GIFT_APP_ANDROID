package com.example.gift_app_android.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.gift_app_android.databinding.ItemEventlistBinding
import com.example.gift_app_android.models.Event

class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemEventlistBinding.bind(view)

    fun render(event: Event, onClick: (Event) -> Unit) {
        binding.eventTitle.text = event.title
        binding.eventDescription.text = event.description

        itemView.setOnClickListener { onClick(event) }
    }
}