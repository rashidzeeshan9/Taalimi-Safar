package com.example.taalimisafar.data.remote

import com.example.taalimisafar.data.model.Quote
import retrofit2.http.GET

interface ApiService {
    @GET("api/quotes/")
    suspend fun getQuotes(): List<Quote>
}