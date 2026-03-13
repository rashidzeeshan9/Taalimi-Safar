package com.example.taalimisafar.ui.screens

import ads_mobile_sdk.id
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.taalimisafar.utils.AppLanguage
import com.example.taalimisafar.utils.LanguageManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    navController: NavController,
    categoryTitle: String,
    categoryId: String
) {
    Scaffold(
        containerColor = Color(0xFFF5F5F5),
        topBar = {
            TopAppBar(
                title = { Text(text = categoryTitle) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF5F5F5)
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (categoryId) {
                "academic" -> AcademicSection(navController)
                "diploma" -> DiplomaSection(navController)
                "internships" -> InternshipSection(navController)
                "important_dates" -> ImportantDatesSection(navController)
                "skills" -> SkillDevelopmentSection(navController)
                "women" -> WomenEmpowermentSection(navController)
                "govt_jobs" -> GovtJobsSection(navController)
                "private_jobs" -> PrivateJobsSection(navController)
                "govt_schemes" -> GovtSchemesSection(navController)
                "sports" -> CareerIndustrySection(navController)
                "hobbies" -> ReligiousStudiesSection(navController)
            }
        }
    }
}

data class GridItem(
    val name: String,
    val nameHi: String? = null,
    val nameUr: String? = null,
    val icon: ImageVector,
    val color: Color,
    val route: String? = null,
    val id: Int = 0
)

@Composable
fun GridSection(
    items: List<GridItem>,
    onItemClick: ((GridItem) -> Unit)? = null
) {
    val currentLanguage = LanguageManager.currentLanguage.value

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(items) { item ->

            // translation to show based on App settings
            val transText = when (currentLanguage) {
                AppLanguage.HINDI -> item.nameHi
                AppLanguage.URDU -> item.nameUr
                else -> null
            }

            Card(
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(155.dp)
                    .clickable {
                        onItemClick?.invoke(item)
                    }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(item.color.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.name,
                            tint = item.color,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // English Name
                    Text(
                        text = item.name,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF37474F),
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        lineHeight = 16.sp
                    )

                    // Translated Name
                    if (!transText.isNullOrBlank()) {
                        Text(
                            text = transText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF4F46E5), // Indigo for translation
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            lineHeight = 16.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun AcademicSection(navController: NavController) {
    val items = listOf(
        GridItem("Art", "कला", "فن", Icons.Default.Brush, Color(0xFFE91E63), id = 1),
        GridItem("Commerce", "वाणिज्य", "کامرس", Icons.Default.ShoppingCart, Color(0xFF4CAF50), id = 2),
        GridItem("IT", "आईटी (सूचना प्रौद्योगिकी)", "آئی ٹی", Icons.Default.Computer, Color(0xFF2196F3), id = 3),
        GridItem("Judicial - Court", "न्यायिक - न्यायालय", "عدالتی - عدالت", Icons.Default.Gavel, Color(0xFF9C27B0), id = 4),
        GridItem("Science", "विज्ञान", "سائنس", Icons.Default.Science, Color(0xFFFF9800), id = 5),
        GridItem("Vocational/\nSkill-based", "व्यावसायिक/कौशल", "پیشہ ورانہ/مہارت", Icons.Default.Work, Color(0xFF009688), id = 6)
    )
    GridSection(items) { item ->
        val safeName = item.name.replace("\n", " ")
        navController.navigate("academic_list/${item.id}/${Uri.encode(safeName)}")
    }
}
@Composable
fun DiplomaSection(navController: NavController) {
    val items = listOf(
        GridItem("Health Care", "स्वास्थ्य देखभाल", "صحت کی دیکھ بھال", Icons.Default.HealthAndSafety, Color(0xFFE53935),
            id = 1),
        GridItem("Trade Diploma", "ट्रेड डिप्लोमा", "ٹریڈ ڈپلومہ", Icons.Default.Business, Color(0xFFFB8C00), id=2),
        GridItem("Vocational Diploma", "व्यावसायिक डिप्लोमा", "پیشہ ورانہ ڈپلومہ", Icons.Default.Work, Color(0xFF43A047), id = 3),
        GridItem("Technical Diploma", "तकनीकी डिप्लोमा", "تکنیکی ڈپلومہ", Icons.Default.Computer, Color(0xFF1E88E5), id = 4),
        GridItem("Professional Diplomas", "पेशेवर डिप्लोमा", "پیشہ ورانہ ڈپلومہ", Icons.Default.AccountBalance, Color(0xFF8E24AA), id = 5),
        GridItem("International Diplomas", "अंतर्राष्ट्रीय डिप्लोमा", "بین الاقوامی ڈپلومہ", Icons.Default.Public, Color(0xFF009688), id = 6)
    )
    GridSection(items) { item ->
        val safeName = item.name.replace("\n", " ")
        navController.navigate("diploma_list/${item.id}/${Uri.encode(safeName)}")
    }
}

@Composable
fun InternshipSection(navController: NavController) {
    val items = listOf(
        GridItem("Paid Internship", "सशुल्क इंटर्नशिप", "بامعاوضہ انٹرنشپ", Icons.Default.MonetizationOn, Color(0xFF4CAF50), id = 1),
        GridItem("Virtual/Remote", "वर्चुअल/रिमोट", "ورچوئل/ریموٹ", Icons.Default.Laptop, Color(0xFF2196F3), id = 2),
        GridItem("Creative & Design", "रचनात्मक और डिजाइन", "تخلیقی اور ڈیزائن", Icons.Default.DesignServices, Color(0xFFE91E63), id = 3),
        GridItem("Academic Credit", "अकादमिक क्रेडिट", "اکیڈمک کریڈٹ", Icons.Default.School, Color(0xFFFFC107), id = 4),
        GridItem("Summer/Winter", "ग्रीष्मकालीन/शीतकालीन", "موسم گرما/موسم سرما", Icons.Default.AcUnit, Color(0xFF00BCD4), id = 5),
        GridItem("Corporate & Business", "कॉर्पोरेट और व्यापार", "کارپوریٹ اور بزنس", Icons.Default.BusinessCenter, Color(0xFF607D8B), id = 6),
        GridItem("Cottage Industry\n- Karkhana", "कुटीर उद्योग", "کاٹیج انڈسٹری", Icons.Default.Factory, Color(0xFF795548), id = 7)
    )
    GridSection(items) { item ->
        val safeName = item.name.replace("\n", " ")
        navController.navigate("internship_list/${item.id}/${Uri.encode(safeName)}")
    }
}
@Composable
fun SkillDevelopmentSection(navController: NavController) {
    val items = listOf(
        // Added the correct 'id' to each of these so the API knows what to fetch!
        GridItem("Soft Skill -\nProblem-Solving", "समस्या समाधान", "مسئلہ حل کرنا", Icons.Default.Lightbulb, Color(0xFFFFC107), id = 1),
        GridItem("Daily Life\nSkills", "दैनिक जीवन कौशल", "روزمرہ زندگی کی مہارتیں", Icons.Default.Chat, Color(0xFF2196F3), id = 2),
        GridItem("Interpersonal &\nSocial Skills", "सामाजिक कौशल", "سماجی مہارتیں", Icons.Default.Groups, Color(0xFFE91E63), id = 3),
        GridItem("Leadership &\nInfluence", "नेतृत्व और प्रभाव", "قیادت اور اثر و رسوخ", Icons.Default.Campaign, Color(0xFFFF5722), id = 4),
        GridItem("Computer\nSkills", "कंप्यूटर कौशल", "کمپیوٹر کی مہارتیں", Icons.Default.Computer, Color(0xFF00BCD4), id = 5),
        GridItem("Communication\nEnglish", "संचार अंग्रेजी", "مواصلاتی انگریزی", Icons.Default.Translate, Color(0xFF673AB7), id = 6),
        GridItem("Skilled Manual\nWork", "कुशल शारीरिक कार्य", "ہنر مند دستی کام", Icons.Default.Build, Color(0xFF795548), id = 7)
    )
    GridSection(items) { item ->
        val safeName = item.name.replace("\n", " ")
        navController.navigate("skill_list/${item.id}/${Uri.encode(safeName)}")
    }
}
@Composable
fun WomenEmpowermentSection(navController: NavController) {
    val items = listOf(
        GridItem("Business and\nE-Commerce", "व्यापार और ई-कॉमर्स", "کاروبار اور ای کامرس", Icons.Default.ShoppingBag, Color(0xFFE91E63)),
        GridItem("Skills", "कौशल", "مہارتیں", Icons.Default.Lightbulb, Color(0xFF9C27B0)),
        GridItem("Mahila Govt.\nScheme", "महिला सरकारी योजना", "خواتین سرکاری اسکیم", Icons.Default.AccountBalance, Color(0xFFFB8C00)),
        GridItem("Health - Fitness", "स्वास्थ्य - फिटनेस", "صحت - فٹنس", Icons.Default.HealthAndSafety, Color(0xFF43A047)),
        GridItem("Domestic\nViolence", "घरेलू हिंसा", "گھریلو تشدد", Icons.Default.Security, Color(0xFFE53935)),
        GridItem("Art & Craft", "कला और शिल्प", "آرٹ اور کرافٹ", Icons.Default.Brush, Color(0xFF3F51B5))
    )
    GridSection(items) { item -> navController.navigate("category_detail/${Uri.encode(item.name)}") }
}

@Composable
fun ImportantDatesSection(navController: NavController) {
    val items = listOf(
        GridItem("State Govt Exam", "राज्य सरकार परीक्षा", "ریاستی حکومت کا امتحان", Icons.Default.Event, Color(0xFF673AB7)),
        GridItem("Central Government\nExam", "केंद्र सरकार परीक्षा", "مرکزی حکومت کا امتحان", Icons.Default.AccountBalance, Color(0xFF1E88E5)),
        GridItem("Govt. Job", "सरकारी नौकरी", "سرکاری نوکری", Icons.Default.Work, Color(0xFF43A047)),
        GridItem("International\nAdmission", "अंतर्राष्ट्रीय प्रवेश", "بین الاقوامی داخلہ", Icons.Default.Public, Color(0xFFFF9800)),
        GridItem("Compliances", "अनुपालन", "تعمیل", Icons.Default.Receipt, Color(0xFFE53935)),
        GridItem("Types of Firm", "फर्म के प्रकार", "فرم کی اقسام", Icons.Default.Business, Color(0xFF009688)),
        GridItem("Banking &\nInsurance", "बैंकिंग और बीमा", "بینکنگ اور انشورنس", Icons.Default.AccountBalance, Color(0xFF5D4037))
    )
    GridSection(items) { item -> navController.navigate("category_detail/${Uri.encode(item.name)}") }
}

@Composable
fun GovtJobsSection(navController: NavController) {
    val items = listOf(
        GridItem("Civil Services\n(Administrative)", "सिविल सेवा (प्रशासनिक)", "سول سروسز", Icons.Default.Gavel, Color(0xFFE53935), id = 10),
        GridItem("Defence &\nSecurity", "रक्षा और सुरक्षा", "دفاع اور سلامتی", Icons.Default.VerifiedUser, Color(0xFF43A047), id = 11),
        GridItem("Public Sector\nUndertakings", "सार्वजनिक क्षेत्र उपक्रम", "پبلک سیکٹر", Icons.Default.Factory, Color(0xFFFB8C00), id = 12),
        GridItem("Teaching &\nEducation", "शिक्षण और शिक्षा", "تدریس اور تعلیم", Icons.Default.School, Color(0xFF1E88E5), id = 13),
        GridItem("Scientific &\nResearch", "वैज्ञानिक और अनुसंधान", "سائنسی اور تحقیق", Icons.Default.Science, Color(0xFFE91E63), id = 14),
        GridItem("Job Classifications\n(Hierarchy)", "नौकरी वर्गीकरण", "ملازمت کی درجہ بندی", Icons.Default.AccountTree, Color(0xFF673AB7), id = 15),
        GridItem("Health Care", "स्वास्थ्य देखभाल", "صحت کی دیکھ بھال", Icons.Default.HealthAndSafety, Color(0xFF009688), id = 16),
        GridItem("Embassy -\nDiplomates", "दूतावास - राजनयिक", "سفارت خانہ - سفارت کار", Icons.Default.Public, Color(0xFF6A1B9A), id = 17)
    )
    GridSection(items) { item ->
        val safeName = item.name.replace("\n", " ")
        navController.navigate("jobList/${item.id}/${Uri.encode(safeName)}")
    }
}

@Composable
fun PrivateJobsSection(navController: NavController) {
    val items = listOf(
        GridItem("Corporate", "कॉर्पोरेट", "کارپوریٹ", Icons.Default.BusinessCenter, Color(0xFF1E88E5), id = 1),
        GridItem("Factory", "कारखाना", "فیکٹری", Icons.Default.Factory, Color(0xFFE53935), id = 2),
        GridItem("FMCG\nWholesalers", "एफएमसीजी थोक", "ایف ایم سی جی ہول سیلرز", Icons.Default.ShoppingCart, Color(0xFF43A047), id = 3),
        GridItem("Daily Wages", "दिहाड़ी", "یومیہ اجرت", Icons.Default.MonetizationOn, Color(0xFFFF9800), id = 4),
        GridItem("Contract\n3rd Party", "अनुबंध तीसरा पक्ष", "معاہدہ تیسرا فریق", Icons.Default.Receipt, Color(0xFF9C27B0), id = 5),
        GridItem("Semi-Govt", "अर्ध-सरकारी", "نیم سرکاری", Icons.Default.AccountBalance, Color(0xFF009688), id = 6),
        GridItem("Mandi System", "मंडी प्रणाली", "منڈی کا نظام", Icons.Default.Agriculture, Color(0xFF795548), id = 7),
        GridItem("Quick\nCommerce", "त्वरित वाणिज्य", "کوئیک کامرس", Icons.Default.LocalShipping, Color(0xFF2196F3), id = 8)
    )
    GridSection(items) { item ->
        val safeName = item.name.replace("\n", " ")
        navController.navigate("jobList/${item.id}/${Uri.encode(safeName)}")
    }
}

@Composable
fun GovtSchemesSection(navController: NavController) {
    val items = listOf(
        GridItem("State Govt", "राज्य सरकार", "ریاستی حکومت", Icons.Default.AccountBalance, Color(0xFF1E88E5), id = 1),
        GridItem("Central Government\nschemes", "केंद्र सरकार योजनाएं", "مرکزی حکومت کی اسکیمیں", Icons.Default.AccountBalance, Color(0xFF43A047), id =2),
        GridItem("Tourism\nDevelopment", "पर्यटन विकास", "سیاحت کی ترقی", Icons.Default.TravelExplore, Color(0xFFFF9800), id = 3),
        GridItem("PSU Schemes", "पीएसयू योजनाएं", "پی ایس یو اسکیمیں", Icons.Default.Factory, Color(0xFFE53935), id = 4),
        GridItem("Private NGO\nSchemes", "निजी एनजीओ योजनाएं", "نجی این جی او اسکیمیں", Icons.Default.VolunteerActivism, Color(0xFF9C27B0), id =5),
        GridItem("International\nSchemes", "अंतर्राष्ट्रीय योजनाएं", "بین الاقوامی اسکیمیں", Icons.Default.Public, Color(0xFF009688), id =6),
        GridItem("United Nations\nProgrammes", "संयुक्त राष्ट्र कार्यक्रम", "اقوام متحدہ کے پروگرام", Icons.Default.Groups, Color(0xFF3F51B5), id = 7),
        GridItem("WHO\nProgrammes", "डब्ल्यूएचओ कार्यक्रम", "ڈبلیو ایچ او پروگرام", Icons.Default.HealthAndSafety, Color(0xFF795548), id =8)
    )
    GridSection(items) { item ->
        val safeName = item.name.replace("\n", " ")
            navController.navigate("scheme_list/${item.id}/${Uri.encode(safeName)}") }
}

@Composable
fun CareerIndustrySection(navController: NavController) {
    val items = listOf(
        GridItem("Aviation Industry", "विमानन उद्योग", "ایوی ایشن انڈسٹری", Icons.Default.Flight, Color(0xFF1E88E5), route = "career_industry_detail/${Uri.encode("Aviation Industry")}"),
        GridItem("Hospitality Industry", "आतिथ्य उद्योग", "مہمان نوازی کی صنعت", Icons.Default.Hotel, Color(0xFFE53935), route = "career_industry_detail/${Uri.encode("Hospitality Industry")}"),
        GridItem("Real Estate Industry", "रियल एस्टेट उद्योग", "رئیل اسٹیٹ انڈسٹری", Icons.Default.LocationCity, Color(0xFF6D4C41), route = "career_industry_detail/${Uri.encode("Real Estate Industry")}"),
        GridItem("Automobile Industry", "ऑटोमोबाइल उद्योग", "آٹوموبائل انڈسٹری", Icons.Default.DirectionsCar, Color(0xFF00897B), route = "career_industry_detail/${Uri.encode("Automobile Industry")}"),
        GridItem("Telecommunications\nIndustry", "दूरसंचार उद्योग", "ٹیلی کمیونیکیشن انڈسٹری", Icons.Default.PhoneAndroid, Color(0xFF3949AB), route = "career_industry_detail/${Uri.encode("Telecommunications Industry")}"),
        GridItem("Retail &\nE-commerce", "खुदरा और ई-कॉमर्स", "ریٹیل اور ای کامرس", Icons.Default.ShoppingCart, Color(0xFFF57C00), route = "career_industry_detail/${Uri.encode("Retail & E-commerce")}"),
        GridItem("Career in Sports", "खेलों में करियर", "کھیلوں میں کیریئر", Icons.Default.SportsSoccer, Color(0xFFFF5722), route = "career_industry_detail/${Uri.encode("Career in Sports")}"),
        GridItem("Agriculture &\nFood", "कृषि और भोजन", "زراعت اور خوراک", Icons.Default.Agriculture, Color(0xFF43A047), route = "career_industry_detail/${Uri.encode("Agriculture & Food")}")
    )
    GridSection(items) { item -> item.route?.let { navController.navigate(it) } }
}

@Composable
fun ReligiousStudiesSection(navController: NavController) {
    val items = listOf(
        GridItem("Muslim", "मुस्लिम", "مسلم", Icons.Default.Mosque, Color(0xFF1E88E5), id = 1),
        GridItem("Hindu", "हिंदू", "ہندو", Icons.Default.TempleHindu, Color(0xFFE91E63), id = 2),
        GridItem("Sikhism", "सिख धर्म", "سکھ مت", Icons.Default.TempleBuddhist, Color(0xFFFFC107), id = 3),
        GridItem("Christianity", "ईसाई धर्म", "عیسائیت", Icons.Default.Church, Color(0xFF3F51B5), id = 4),
        GridItem("Buddhists", "बौद्ध", "بدھ مت", Icons.Default.SelfImprovement, Color(0xFF4CAF50), id = 5),
        GridItem("Jain", "जैन", "جین", Icons.Default.Spa, Color(0xFF9C27B0), id = 6),
        GridItem("Parsi", "पारसी", "پارسی", Icons.Default.LocalFireDepartment, Color(0xFFFF7043), id = 7),
        GridItem("Adivasi Religions", "आदिवासी धर्म", "آدیواسی مذاہب", Icons.Default.Groups, Color(0xFF00695C), id = 8)
    )
    GridSection(items) { item ->
        val safeName = item.name.replace("\n", " ")
        navController.navigate("religious_list/${item.id}/${Uri.encode(safeName)}")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleDetailScreen(
    navController: NavController,
    title: String
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = title) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}