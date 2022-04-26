package com.example.gift_app_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.gift_app_android.api.ServiceBuilder
import com.example.gift_app_android.databinding.ActivityNewEventBinding
import com.example.gift_app_android.fragments.DatePickerFragment
import com.example.gift_app_android.models.CreateEventResponse
import com.example.gift_app_android.models.Gift
import com.example.gift_app_android.storage.SharedPrefManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class NewEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var listGift = mutableListOf<Gift>()
        var listSpinner = mutableListOf<String>()

        binding.btnBack.setOnClickListener {
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }

        CoroutineScope(Dispatchers.Main).launch {
            val call = ServiceBuilder.instance.listGifts(
                SharedPrefManager.getInstance(applicationContext).user.id,
                "Bearer "+SharedPrefManager.getInstance(applicationContext).token
            )
            val resGifts = call.body()

            if (call.isSuccessful) {
                for (item in resGifts?.gifts!!) {
                    listSpinner.add(item.title)
                    listGift.add(item)
                }
                initSpinner(listSpinner)
            } else {
                println(call.message())
            }
        }

        binding.etDate.setOnClickListener { showDatePickerDialog() }
        binding.saveEvent.setOnClickListener {

            var itemSelected = ""
            listGift.forEach { gift ->
                if (gift.title == binding.giftID.selectedItem) {
                    itemSelected = gift.id
                }
            }

            println(itemSelected)

            val jsonObject = JSONObject()
            jsonObject.put("title", binding.titleEvent.text.toString())
            jsonObject.put("description", binding.descriptionEvent.text.toString())
            jsonObject.put("date", binding.etDate.text.toString())
            jsonObject.put("gift_id", itemSelected)
            jsonObject.put("user_id", SharedPrefManager.getInstance(this).user.id)

            val jsonObjectString = jsonObject.toString()

            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

            ServiceBuilder.instance.createEvent(requestBody, "Bearer "+ SharedPrefManager.getInstance(this).token)
                .enqueue(object: Callback<CreateEventResponse> {
                    override fun onResponse(call: Call<CreateEventResponse>, response: retrofit2.Response<CreateEventResponse>) {
                        Toast.makeText(applicationContext, response.isSuccessful.toString(), Toast.LENGTH_LONG).show()
                        println(response.body()?.message)

                        if (response.isSuccessful) {
                            val intent = Intent(applicationContext, ProfileActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                            startActivity(intent)
                        }
                    }

                    override fun onFailure(call: Call<CreateEventResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                        println(t.message)
                    }

                })
        }
    }

    private fun initSpinner(listGift: List<String>) {
        val adapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, listGift)
        binding.giftID.adapter = adapter
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(supportFragmentManager, "datePicker")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        var monthStr = "$month"
        var dayStr = "$day"

        if(month < 10){
            monthStr = "0$month"
        }

        if(day < 10){
            dayStr = "0$day"
        }
        binding.etDate.setText("$year-$monthStr-$dayStr")
    }
}