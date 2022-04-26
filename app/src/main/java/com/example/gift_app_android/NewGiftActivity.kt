package com.example.gift_app_android

import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.gift_app_android.api.ServiceBuilder
import com.example.gift_app_android.databinding.ActivityNewGiftBinding
import com.example.gift_app_android.models.ResponseQr
import com.example.gift_app_android.storage.SharedPrefManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class NewGiftActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewGiftBinding
    private lateinit var path: String
    private var uriVideo: Uri? = null
    private var responseQr: ResponseQr? = null
    private lateinit var progressDial: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewGiftBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDial = ProgressDialog(this)
        progressDial.setMessage("Subiendo Regalo...")
        progressDial.setCancelable(true)

        CoroutineScope(Dispatchers.IO).launch {
            val call = ServiceBuilder.instance.generateQR(
                "Bearer "+ SharedPrefManager.getInstance(applicationContext).token
            )
            val responses = call.body()

            if (call.isSuccessful) {
                println("----- $responses")
                responseQr = responses!!
                continueApp(responseQr)
            }
        }

        binding.uploadVideo.setOnClickListener {
            if (ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent()
                intent.type = "video/*"
                intent.action = Intent.ACTION_GET_CONTENT

                startActivityForResult(intent, 10)
            } else {
                var manifestPermission = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ActivityCompat.requestPermissions(this, manifestPermission, 1)
            }
        }

        binding.btnBack.setOnClickListener {
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }
    }

    private fun continueApp(res: ResponseQr?) {
        binding.saveGift.setOnClickListener {
            if (uriVideo == null || binding.titleGift.text.toString() == "") {
                Toast.makeText(this, "Por favor selecciona un video ", Toast.LENGTH_LONG).show()

                return@setOnClickListener
            }

            showDialog()

            val file = File(path)
            val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            var map = HashMap<String, RequestBody>()

            val title: RequestBody = createPartFromString(binding.titleGift.text.toString())
            val qrImage: RequestBody = createPartFromString(res?.qrImage.toString())
            val giftID: RequestBody = createPartFromString(res?.giftID.toString())
            val userID: RequestBody = createPartFromString(SharedPrefManager.getInstance(applicationContext).user.id)
            var multiPart = MultipartBody.Part.createFormData("videoFile", file.name, requestBody)

            map["title"] = title
            map["qrImage"] = qrImage
            map["giftID"] = giftID
            map["userID"] = userID

            CoroutineScope(Dispatchers.Main).launch {
                val call = ServiceBuilder.instance.createGift("Bearer "+ SharedPrefManager.getInstance(applicationContext).token, map, multiPart)

                if (call.isSuccessful) {
                    hideDialog()

                    val intent = Intent(applicationContext, ProfileActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                    startActivity(intent)
                } else {
                    hideDialog()
                    Toast.makeText(applicationContext, "Hubo un problema con la creacion", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun createPartFromString(param: String) : RequestBody {
        return param.toRequestBody("multipart/form-data".toMediaTypeOrNull())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 10 && resultCode == RESULT_OK) {
            uriVideo = data?.data
            path = uriVideo?.let { RealPathUtil.getRealPath(this, it).toString() }.toString()

            var bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MINI_KIND)
            binding.previewGift.setImageBitmap(bitmap)
        }
    }

    private fun showDialog() {
        if (!progressDial.isShowing) progressDial.show()
    }

    private fun hideDialog() {
        if (progressDial.isShowing) progressDial.dismiss()
    }
}