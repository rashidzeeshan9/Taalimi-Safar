package com.example.taalimisafar.data.remote

import com.example.taalimisafar.data.model.Quote
import com.example.taalimisafar.data.model.Course
import com.example.taalimisafar.data.model.Internship
import com.example.taalimisafar.data.model.InternshipCategory
import com.example.taalimisafar.data.model.Scholarship
import com.example.taalimisafar.data.model.ScholarshipCategory
import com.example.taalimisafar.data.model.ScholarshipType
import com.example.taalimisafar.data.model.Job
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/quotes/")
    suspend fun getQuotes(): List<Quote>

    @GET("api/courses/")
    suspend fun getCourses(): List<Course>

    @GET("api/scholarship-categories/")
    suspend fun getScholarshipCategories(): List<ScholarshipCategory>

    @GET("api/scholarship-types/")
    suspend fun getScholarshipTypes(): List<ScholarshipType>

    @GET("api/scholarships/")
    suspend fun getScholarships(
        @Query("category") categoryId: Int? = null,
        @Query("scholarship_type") typeId: Int? = null
    ): List<Scholarship>

    // For Internship Part

    @GET("api/internships/categories/")
    suspend fun getInternshipCategories(): List<InternshipCategory>

    @GET("api/internships/")
    suspend fun getInternships(
        @Query("category") categoryId: Int? = null,           // Now optional!
        @Query("search") searchQuery: String? = null,            // For text search
        @Query("mode") mode: String? = null,                     // "online" (Remote), "offline", "hybrid"
        @Query("internship_type") type: String? = null,          // "Paid" or "Unpaid"
        @Query("location__icontains") location: String? = null,  // e.g., "Delhi"
        @Query("domain__icontains") domain: String? = null,      // e.g., "IT"
        @Query("stipend_amount__gte") minStipend: Int? = null,   // Minimum stipend amount
        @Query("ppo_opportunity") ppo: Boolean? = null           // true if PPO is required
    ): List<Internship>
    @GET("api/jobs/")
    suspend fun getJobsByCategory(
        @Query("category") categoryId: Int? = null,
        @Query("search") searchQuery: String? = null,
        @Query("category__sector") sector: String? = null,
        @Query("location_state") stateId: Int? = null,
        @Query("is_remote") isRemote: Boolean? = null,
        @Query("experience_required") experience: String? = null
    ): List<Job>

}