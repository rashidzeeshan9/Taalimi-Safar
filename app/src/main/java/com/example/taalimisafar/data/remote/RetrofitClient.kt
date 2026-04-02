package com.example.taalimisafar.data.remote

import android.content.Context
import com.example.taalimisafar.utils.TokenManager // Update if your TokenManager is in a different folder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.45.179.201:8000/"

    // 1. YOUR ORIGINAL API (Keep this so AcademicViewModel doesn't break!)
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    // 2. THE NEW SECURE API (For AuthViewModel and private user data)
    private var authRetrofit: Retrofit? = null

    fun getClient(context: Context): ApiService {
        if (authRetrofit == null) {
            val tokenManager = TokenManager(context)
            val authInterceptor = AuthInterceptor(tokenManager)

            // Attach the JWT token to the request headers
            val client = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build()

            authRetrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return authRetrofit!!.create(ApiService::class.java)
    }
}