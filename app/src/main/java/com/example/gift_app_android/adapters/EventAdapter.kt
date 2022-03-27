package com.example.gift_app_android.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gift_app_android.R
import com.example.gift_app_android.models.Event
import com.example.gift_app_android.viewholders.EventViewHolder

class EventAdapter(private val eventList: List<Event>, private val onClick: (Event) -> Unit) : RecyclerView.Adapter<EventViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        var v = layoutInflater.inflate(R.layout.item_eventlist, parent, false)

        return EventViewHolder(v)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val iterator = eventList[position]
        holder.render(iterator, onClick)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

}