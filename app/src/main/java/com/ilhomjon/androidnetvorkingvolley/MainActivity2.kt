package com.ilhomjon.androidnetvorkingvolley

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.*
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ilhomjon.Adapter.UserAdapter
import com.ilhomjon.Models.User
import com.ilhomjon.androidnetvorkingvolley.databinding.ActivityMain2Binding
import org.json.JSONArray

class MainActivity2 : AppCompatActivity() {
    lateinit var binding: ActivityMain2Binding
    lateinit var requestQueue: RequestQueue
    lateinit var userAdapter: UserAdapter
    var url = "https://api.github.com/users"
    private val TAG = "MainActivity2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        requestQueue = Volley.newRequestQueue(this)
        VolleyLog.DEBUG = true //qanday ma'lumot kelayotganini Logda ko'rsatib turadi

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
                object : Response.Listener<JSONArray> {
                    override fun onResponse(response: JSONArray?) {

                        val type = object : TypeToken<List<User>>(){}.type
                        val list = Gson().fromJson<List<User>>(response.toString(), type)

                        userAdapter = UserAdapter(list)
                        binding.rv.adapter = userAdapter

                        Log.d(TAG, "onResponse : ${response.toString()}")
                    }
                }, object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError?) {

            }
        })

        jsonArrayRequest.tag = "tag1" //tag berilyapti
        requestQueue.add(jsonArrayRequest)

        requestQueue.cancelAll("tag1") // tag1 dagi so'rovlarni ortga qaytarish uchun
    }
}