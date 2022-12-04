package com.example.jwt_tutorial

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.jwt_tutorial.databinding.ActivityLoginBinding
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        binding.nextBtn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.password.text.toString()
            apiClient.getApiService(this).login(email,password)
                .enqueue(object : Callback<LoginResponse> {
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        // Error logging in
                    }

                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        val loginResponse: LoginResponse = response.body()!!

                        if (response.isSuccessful) {
                            sessionManager.saveAccessToken(loginResponse.access_token)
                            sessionManager.saveRefreshToken(loginResponse.refresh_token)
                            Log.d("LOGIN-RESPONSE","successful")
                            finish()
                        } else {
                            Log.d("LOGIN-RESPONSE","unsuccessful")
                        }
                    }
                })
        }

    }

}