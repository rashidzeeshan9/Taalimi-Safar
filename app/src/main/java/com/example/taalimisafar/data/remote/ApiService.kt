package com.example.taalimisafar.data.remote

import com.example.taalimisafar.data.model.Quote
import com.example.taalimisafar.data.model.Course
import com.example.taalimisafar.data.model.Scholarship
import retrofit2.http.GET

interface ApiService {
    @GET("api/quotes/")
    suspend fun getQuotes(): List<Quote>

    @GET("api/courses/")
    suspend fun getCourses(): List<Course>

    @GET("api/scholarships/")
    suspend fun getScholarships(): List<Scholarship>
}