package com.example.taalimisafar.data.model

data class ImportantCategory(
    val id: Int,
    val name: String,
    val name_hi: String?,
    val name_ur: String?,
    val link: String?,
    val icon: String?,
    val description: String,
    val description_hi: String?,
    val description_ur: String?,
    val is_active: Boolean?,
    )

data class ImportantProgram(
    val id: Int,
    val category: Int,
    val category_hi: String?,
    val category_ur: String?,
    val title: String,
    val title_hi: String?,
    val title_ur: String?,
    val link: String?,
    val image: String?,
    val is_featured: Boolean,
    val is_active: Boolean,
    val overview: String,
    val overview_hi: String?,
    val overview_ur: String?,
    val description: String,
    val description_hi: String?,
    val description_ur: String?,
    val eligibility: String,
    val eligibility_hi: String?,
    val eligibility_ur: String?,
    val age_limit: String?,
    val application_fee: String?,
    val selection_process: String?,
    val selection_process_hi: String?,
    val selection_process_ur: String?,
    val official_notification: String?,

    val official_websites: List<OfficialWebsite>? = null,
    val important_dates: List<ImportantDate>? = null,
    val application_steps: List<ApplicationStep>? = null,
    val required_documents: List<RequiredDocument>? = null,
    val exam_stages: List<ExamStage>? = null,
    val exam_patterns: List<ExamPattern>? = null,
    val program_syllabus: List<ProgramSyllabus>? = null,
    val preparation_strategies: List<PreparationStrategy>? = null,
    val previous_papers: List<PreviousYearPaper>? = null,
    val cutoffs: List<Cutoff>? = null,
    val attempt_limits: List<AttemptLimit>? = null,
    val reservation_benefits: List<ReservationBenefit>? = null,
    val faqs: List<ImportantDateFAQ>? = null
)
data class OfficialWebsite(
    val id: Int,
    val title: String,
    val website_link: String?,

)

data class ImportantDate(
    val id: Int,
    val title: String,
    val date: String,
    val description: String?,
    val description_hi: String?,
    val description_ur: String?
)
data class ApplicationStep(
    val id: Int,
    val step_number: Int,
    val title: String,
    val title_hi: String?,
    val title_ur: String?,
    val description: String?,
    val description_hi: String?,
    val description_ur: String?
)
data class RequiredDocument(
    val id: Int,
    val name: String,
    val name_hi: String?,
    val name_ur: String?,
    val description: String?,
    val description_hi: String?,
    val description_ur: String?
)
data class ExamStage(
    val id: Int,
    val name: String,
    val name_hi: String?,
    val name_ur: String?,
    val description: String?,
    val description_hi: String?,
    val description_ur: String?
)
data class ExamPattern(
    val id: Int,
    val title: String,
    val title_hi: String?,
    val title_ur: String?,
    val description: String?,
    val description_hi: String?,
    val description_ur: String?
)

data class ProgramSyllabus(
    val id: Int,
    val subject: String,
    val subject_hi: String?,
    val subject_ur: String?,
    val topics: String?,
    val topics_hi: String?,
    val topics_ur: String?
)
data class PreparationStrategy(
    val id: Int,
    val title: String,
    val title_hi: String?,
    val title_ur: String?,
    val description: String?,
    val description_hi: String?,
    val description_ur: String?,
    val video_link: String?,
    val website_link: String?
)

data class PreviousYearPaper(
    val id: Int,
    val year: Int,
    val paper_file: String?,
    val description: String?,
    val description_hi: String?,
    val description_ur: String?
)
data class Cutoff(
    val id: Int,
    val year: Int,
    val category: String,
    val cutoff_marks: String
)

data class AttemptLimit(
    val id: Int,
    val category: String,
    val max_attempts: Int
)
data class ReservationBenefit(
    val id: Int,
    val category: String,
    val age_relaxation: String,
    val description: String?,
    val description_hi: String?,
    val description_ur: String?
)

data class ImportantDateFAQ(
    val id: Int,
    val question: String,
    val question_hi: String?,
    val question_ur: String?,
    val answer: String?,
    val answer_hi: String?,
    val answer_ur: String?
)