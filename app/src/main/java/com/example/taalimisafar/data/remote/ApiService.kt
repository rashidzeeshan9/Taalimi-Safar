package com.example.taalimisafar.data.remote

import com.example.taalimisafar.data.model.Quote
import com.example.taalimisafar.data.model.Course
import com.example.taalimisafar.data.model.Scholarship
import com.example.taalimisafar.data.model.ScholarshipCategory
import com.example.taalimisafar.data.model.ScholarshipType // ✅ ADDED THIS IMPORT
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/quotes/")
    suspend fun getQuotes(): List<Quote>

    @GET("api/courses/")
    suspend fun getCourses(): List<Course>

    @GET("api/scholarship-categories/")
    suspend fun getCategories(): List<ScholarshipCategory>

    @GET("api/scholarship-types/")
    suspend fun getScholarshipTypes(): List<ScholarshipType>

    @GET("api/scholarships/")
    suspend fun getScholarships(
        @Query("category") categoryId: Int? = null,
        @Query("scholarship_type") typeId: Int? = null
    ): List<Scholarship>
}