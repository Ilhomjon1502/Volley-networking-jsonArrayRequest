package com.ilhomjon.androidnetvorkingvolley

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ilhomjon.Utils.NetworkHelper
import com.ilhomjon.androidnetvorkingvolley.databinding.ActivityMainBinding
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var networkHelper: NetworkHelper
    lateinit var requestQueue:RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        networkHelper = NetworkHelper(this)

        if (networkHelper.isNetworkConnected()){
            binding.tv.text = "Connected"

            requestQueue = Volley.newRequestQueue(this)

            fetchImageLoad()

            featchObjectLoad()

        }else{
            binding.tv.text = "Disconnected"
        }
    }

    private fun featchObjectLoad() {
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, "http://ip.jsontest.com", null,
        object: Response.Listener<JSONObject>{//xatolik
            override fun onResponse(response: JSONObject?) {
                val strstring = response?.getString("ip")
                binding.tv.text = strstring
            }
        }, object : Response.ErrorListener{
            override fun onErrorResponse(error: VolleyError?) {
                binding.tv.text = error?.message
            }
        })
        requestQueue.add(jsonObjectRequest)
    }

    private fun fetchImageLoad() {
        val imageRequest = ImageRequest("https://i.imgur.com/Nwk25LA.jpg",
         object : Response.Listener<Bitmap>{
             override fun onResponse(response: Bitmap?) {
                binding.imageView.setImageBitmap(response)
             }
         }, 0, 0, ImageView.ScaleType.CENTER_CROP,
        Bitmap.Config.ARGB_8888,
        object : Response.ErrorListener{
            override fun onErrorResponse(error: VolleyError?) {
                binding.tv.text = error?.message
            }
        })

        requestQueue.add(imageRequest)
    }

    private fun isNetworkConnected() : Boolean{
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            //SDK 23 dan yuqori bo'lsa
            val activeNetwork = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)

            return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }else{
            val activeNetworkInfo = connectivityManager.activeNetworkInfo

            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }
}