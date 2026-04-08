import com.google.gson.annotations.SerializedName

// 1. The Model for receiving data FROM the server (About Us)
data class AboutUsResponse(
    @SerializedName("description") val description: String,

    // We use String? (nullable) because you might leave these blank in the Django Admin
    @SerializedName("website_link") val websiteLink: String?,
    @SerializedName("contact_email") val contactEmail: String?
)

// 2. The Model for sending data TO the server (Feedback)
data class FeedbackRequest(
    @SerializedName("message") val message: String
)