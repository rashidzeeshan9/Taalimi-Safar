package com.example.taalimisafar.data.remote

import com.example.taalimisafar.data.model.Quote
import com.example.taalimisafar.data.model.Course
import com.example.taalimisafar.data.model.DiplomaProgram
import com.example.taalimisafar.data.model.IndustryCategory
import com.example.taalimisafar.data.model.IndustryProgram
import com.example.taalimisafar.data.model.Internship
import com.example.taalimisafar.data.model.InternshipCategory
import com.example.taalimisafar.data.model.Scholarship
import com.example.taalimisafar.data.model.ScholarshipCategory
import com.example.taalimisafar.data.model.ScholarshipType
import com.example.taalimisafar.data.model.Job
import com.example.taalimisafar.data.model.ReligiousProgram
import com.example.taalimisafar.data.model.Schemes
import com.example.taalimisafar.data.model.SchemesCategory
import com.example.taalimisafar.data.model.SkillProgram
import com.example.taalimisafar.data.model.Stream
import com.example.taalimisafar.data.model.WomenCategory
import com.example.taalimisafar.data.model.WomenProgram
import retrofit2.http.GET
import retrofit2.http.Path
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

    @GET("api/internships/categories/")
    suspend fun getInternshipCategories(): List<InternshipCategory>

    @GET("api/internships/")
    suspend fun getInternships(
        @Query("category") categoryId: Int? = null,
        @Query("search") searchQuery: String? = null,
        @Query("mode") mode: String? = null,
        @Query("internship_type") type: String? = null,
        @Query("location__icontains") location: String? = null,
        @Query("domain__icontains") domain: String? = null,
        @Query("stipend_amount__gte") minStipend: Int? = null,
        @Query("ppo_opportunity") ppo: Boolean? = null
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


    @GET("api/skill-programs/")
    suspend fun getSkillPrograms(
        @Query("category") categoryId: Int? = null
    ): List<SkillProgram>

    @GET("api/skill-programs/{id}/")
    suspend fun getSkillProgramDetail(
        @Path("id") programId: Int
    ): SkillProgram

    @GET("api/religious-programs/")
    suspend fun getReligiousPrograms(
        @Query("religion") religionId: Int? = null
    ): List<ReligiousProgram>

    @GET("api/religious-programs/{id}/")
    suspend fun getReligiousProgramDetail(
        @Path("id") programId: Int
    ): ReligiousProgram


    @GET("api/diploma-programs/")
    suspend fun getDiplomaPrograms(
        @Query("category") categoryId: Int
    ): List<DiplomaProgram>

    @GET("api/diploma-programs/{id}/")
    suspend fun getDiplomaProgramDetail(
        @Path("id") programId: Int
    ): DiplomaProgram


    @GET("api/scheme-categories")
    suspend fun getSchemeCategories(): List<SchemesCategory>

    @GET("api/schemes/")
    suspend fun getSchemes(
        @Query("category") categoryId: Int? = null,
        @Query("status") status: String? = "Active"
    ): List<Schemes>

    @GET("api/schemes/{id}/")
    suspend fun getSchemeDetail(
        @Path("id") id: Int
    ): Schemes
    // ----------------------------
// STREAMS
// ----------------------------

    @GET("api/streams/")
    suspend fun getStreams(): List<Stream>


// ----------------------------
// COURSES BY STREAM
// ----------------------------

    @GET("api/courses/")
    suspend fun getCourses(
        @Query("stream") streamId: Int
    ): List<Course>


// ----------------------------
// COURSE DETAIL
// ----------------------------

    @GET("api/courses/{id}/")
    suspend fun getCourseDetail(
        @Path("id") courseId: Int
    ): Course


    // === INDUSTRY & CAREER ENDPOINTS ===
    @GET("api/industry-categories/")
    suspend fun getIndustryCategories(): List<IndustryCategory>

    @GET("api/industry-programs/")
    suspend fun getIndustryPrograms(
        @Query("category") categoryId: Int? = null,
        @Query("demand_level") demandLevel: String? = null,
        @Query("is_featured") isFeatured: Boolean? = null
    ): List<IndustryProgram>

    @GET("api/industry-programs/{id}/")
    suspend fun getIndustryProgramDetail(
        @Path("id") id: Int
    ): IndustryProgram

    @GET("api/women-categories/")
    suspend fun getWomenCategories(): List<WomenCategory>

    @GET("api/women-programs")
    suspend fun getWomenPrograms(
        @Query("category") categoryId: Int? = null
    ): List<WomenProgram>

    @GET("api/women-programs/{id}/")
    suspend fun getWomenProgramDetail(
        @Path("id") id: Int
    ): WomenProgram

}

