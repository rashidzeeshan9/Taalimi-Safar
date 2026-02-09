import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

data class DashboardItem(
    val id: String,
    val icon: ImageVector,
    val nameEn: String,
    val nameHi: String,
    val nameUr: String
)

val dashboardMenu = listOf(
    DashboardItem("courses", Icons.Default.MenuBook, "Courses", "कोर्सेस", "کورسز"),
    DashboardItem("colleges", Icons.Default.School, "Colleges", "कॉलेज", "کالجز"),
    DashboardItem("exams", Icons.Default.Edit, "Exams", "परीक्षा", "امتحانات"),
    DashboardItem("scholarship", Icons.Default.AttachMoney, "Scholarships", "छात्रवृत्ति", "وظائف"),
    DashboardItem("counseling", Icons.Default.SupportAgent, "Counseling", "परामर्श", "مشاورت"),
    DashboardItem("settings", Icons.Default.Settings, "Settings", "सेटिंग्स", "ترتیبات")
)