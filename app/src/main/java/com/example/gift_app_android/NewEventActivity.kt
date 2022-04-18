package com.example.gift_app_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.gift_app_android.api.ServiceBuilder
import com.example.gift_app_android.databinding.ActivityNewEventBinding
import com.example.gift_app_android.fragments.DatePickerFragment
import com.example.gift_app_android.models.CreateEventResponse
import com.example.gift_app_android.storage.SharedPrefManager
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

        binding.btnBack.setOnClickListener {
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }

        binding.etDate.setOnClickListener { showDatePickerDialog() }


        binding.saveEvent.setOnClickListener {
            val jsonObject = JSONObject()
            jsonObject.put("title", binding.titleEvent.text.toString())
            jsonObject.put("description", binding.descriptionEvent.text.toString())
            jsonObject.put("date", binding.etDate.text.toString())
            jsonObject.put("gift_id", binding.giftID.text.toString())

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