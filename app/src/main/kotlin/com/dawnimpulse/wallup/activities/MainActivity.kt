package com.dawnimpulse.wallup.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.network.RetroApiClient
import com.dawnimpulse.wallup.pojo.ImagePojo
import com.dawnimpulse.wallup.source.RetroUnsplashSource
import com.dawnimpulse.wallup.utils.Config
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    /**
     * On create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                message.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
                val call = apiClient.getLatestPhotos(
                        Config.UNSPLASH_API_KEY,
                        1,
                        10)

                call.enqueue(object : Callback<List<ImagePojo>> {

                    override fun onResponse(call: Call<List<ImagePojo>>?, response: Response<List<ImagePojo>>) {
                        if (response.isSuccessful)
                            Log.d("Test", Gson().toJson(response.body()))
                        else
                            Log.d("Test", "Nops")
                    }

                    override fun onFailure(call: Call<List<ImagePojo>>?, t: Throwable?) {
                        Log.e("Test", t.toString())
                    }
                })
                message.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}
