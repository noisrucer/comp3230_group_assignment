package com.example.jwt_tutorial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.jwt_tutorial.databinding.ActivityLoginBinding
import com.example.jwt_tutorial.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        if (sessionManager.fetchAccessToken() == null) { //if access token is null
            val intent = Intent(this, LoginActivity::class.java) //switch to LoginActivity
            startActivity(intent)
        }
        else {
            printMessage()  //this is the calling of wanted API
        }
    }

    /**
     * Function to print "Hello World" upon success login and passing of access token to the header
     */
    private fun printMessage() {
        apiClient.getApiService(this).checkService()
            .enqueue(object : Callback<CheckResponse> {
                override fun onFailure(call: Call<CheckResponse>, t: Throwable) {
                    Log.d("response","failed")
                }
                override fun onResponse(call: Call<CheckResponse>, response: Response<CheckResponse>) {
                    // Handle function to print Hello World
                        val responseBodyString = response.errorBody()?.string()
                        //if access token has expired then refresh
                        if (responseBodyString != null) {
                            var jsonObject = JSONObject(responseBodyString)
                            var failResponse = jsonObject.getString("detail")
                            if (failResponse == "Signature has expired") {
                                refresh()
                            }
                        }
                        //print Hello World if access token is valid
                        else binding.checkMsg.text = "Hello World" //means access token is still valid

                }
            })
    }
    private fun refresh() {
        val refreshToken = sessionManager.fetchRefreshToken()
        apiClient.getApiService(this).refresh("Bearer $refreshToken").enqueue(object: Callback<RefreshResponse> {
            override fun onResponse(call: Call<RefreshResponse>, response: Response<RefreshResponse>) {
                if (response.isSuccessful) {
                    val refreshResponse: RefreshResponse = response.body()!!
                    Log.d("REFRESH-RESPONSE", "SUCCESS")
                    sessionManager.saveAccessToken(refreshResponse.access_token)
                    printMessage() //call wanted API again once access token is reproduced
                }
                else {
                    sessionManager.logout()
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
            override fun onFailure(call: Call<RefreshResponse>, t: Throwable) {
                Log.d("REFRESH-RESPONSE", "FAIL")
            }

        })
    }
}