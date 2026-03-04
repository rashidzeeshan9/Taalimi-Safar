package com.example.taalimisafar.ui.screens

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

data class GridItem(val name: String, val icon: ImageVector, val color: Color, val route: String? = null, val id: Int = 0)

@Composable
fun GridSection(
    items: List<GridItem>,
    onItemClick: ((GridItem) -> Unit)? = null
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(items) { item ->
            Card(
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(145.dp)
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

                    Text(
                        text = item.name,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF37474F),
                        textAlign = TextAlign.Center,
                        maxLines = 3,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

/* ---------------- SECTIONS ---------------- */

@Composable
fun AcademicSection(navController: NavController) {
    val items = listOf(
        GridItem("Art", Icons.Default.Brush, Color(0xFFE91E63)),
        GridItem("Commerce", Icons.Default.ShoppingCart, Color(0xFF4CAF50)),
        GridItem("IT", Icons.Default.Computer, Color(0xFF2196F3)),
        GridItem("Judicial - Court", Icons.Default.Gavel, Color(0xFF9C27B0)),
        GridItem("Science", Icons.Default.Science, Color(0xFFFF9800)),
        GridItem("Vocational/\nSkill-based", Icons.Default.Work, Color(0xFF009688))
    )

    GridSection(items) { item ->
        navController.navigate("category_detail/${Uri.encode(item.name)}")
    }
}

@Composable
fun DiplomaSection(navController: NavController) {
    val items = listOf(
        GridItem("Health Care", Icons.Default.HealthAndSafety, Color(0xFFE53935)),
        GridItem("Trade Diploma", Icons.Default.Business, Color(0xFFFB8C00)),
        GridItem("Vocational Diploma", Icons.Default.Work, Color(0xFF43A047)),
        GridItem("Technical Diploma", Icons.Default.Computer, Color(0xFF1E88E5)),
        GridItem("Professional Diplomas", Icons.Default.AccountBalance, Color(0xFF8E24AA)),
        GridItem("International Diplomas", Icons.Default.Public, Color(0xFF009688))
    )

    GridSection(items) { item ->
        navController.navigate("category_detail/${Uri.encode(item.name)}")
    }
}

@Composable
fun InternshipSection(navController: NavController) {
    val items = listOf(
        GridItem("Paid Internship", Icons.Default.MonetizationOn, Color(0xFF4CAF50), id = 1),
        GridItem("Virtual/Remote", Icons.Default.Laptop, Color(0xFF2196F3), id = 2),
        GridItem("Creative & Design", Icons.Default.DesignServices, Color(0xFFE91E63), id = 3),
        GridItem("Academic Credit", Icons.Default.School, Color(0xFFFFC107), id = 4),
        GridItem("Summer/Winter", Icons.Default.AcUnit, Color(0xFF00BCD4), id = 5),
        GridItem("Corporate & Business", Icons.Default.BusinessCenter, Color(0xFF607D8B), id = 6),
        GridItem("Cottage Industry\n- Karkhana", Icons.Default.Factory, Color(0xFF795548), id = 7)
    )

    GridSection(items) { item ->
        navController.navigate("internship_list/${item.id}/${Uri.encode(item.name)}")
    }
}

@Composable
fun SkillDevelopmentSection(navController: NavController) {
    val items = listOf(
        GridItem("Soft Skill -\nProblem-Solving", Icons.Default.Lightbulb, Color(0xFFFFC107)),
        GridItem("Daily Life\nSkills", Icons.Default.Chat, Color(0xFF2196F3)),
        GridItem("Interpersonal &\nSocial Skills", Icons.Default.Groups, Color(0xFFE91E63)),
        GridItem("Leadership &\nInfluence", Icons.Default.Campaign, Color(0xFFFF5722)),
        GridItem("Computer\nSkills", Icons.Default.Computer, Color(0xFF00BCD4)),
        GridItem("Communication\nEnglish", Icons.Default.Translate, Color(0xFF673AB7)),
        GridItem("Skilled Manual\nWork", Icons.Default.Build, Color(0xFF795548))
    )

    GridSection(items) { item ->
        navController.navigate("category_detail/${Uri.encode(item.name)}")
    }
}

@Composable
fun WomenEmpowermentSection(navController: NavController) {
    val items = listOf(
        GridItem("Business and\nE-Commerce", Icons.Default.ShoppingBag, Color(0xFFE91E63)),
        GridItem("Skills", Icons.Default.Lightbulb, Color(0xFF9C27B0)),
        GridItem("Mahila Govt.\nScheme", Icons.Default.AccountBalance, Color(0xFFFB8C00)),
        GridItem("Health - Fitness", Icons.Default.HealthAndSafety, Color(0xFF43A047)),
        GridItem("Domestic\nViolence", Icons.Default.Security, Color(0xFFE53935)),
        GridItem("Art & Craft", Icons.Default.Brush, Color(0xFF3F51B5))
    )

    GridSection(items) { item ->
        navController.navigate("category_detail/${Uri.encode(item.name)}")
    }
}

@Composable
fun ImportantDatesSection(navController: NavController) {
    val items = listOf(
        GridItem("State Govt Exam", Icons.Default.Event, Color(0xFF673AB7)),
        GridItem("Central Government\nExam", Icons.Default.AccountBalance, Color(0xFF1E88E5)),
        GridItem("Govt. Job", Icons.Default.Work, Color(0xFF43A047)),
        GridItem("International\nAdmission", Icons.Default.Public, Color(0xFFFF9800)),
        GridItem("Compliances", Icons.Default.Receipt, Color(0xFFE53935)),
        GridItem("Types of Firm", Icons.Default.Business, Color(0xFF009688)),
        GridItem("Banking &\nInsurance", Icons.Default.AccountBalance, Color(0xFF5D4037))
    )

    GridSection(items) { item ->
        navController.navigate("category_detail/${Uri.encode(item.name)}")
    }
}

// 👇 FIXED: Added placeholder IDs and routed to jobList
@Composable
fun GovtJobsSection(navController: NavController) {
    val items = listOf(
        // ⚠️ REPLACE these IDs (10, 11, etc.) with the real category IDs from your Django Admin panel!
        GridItem("Civil Services\n(Administrative)", Icons.Default.Gavel, Color(0xFFE53935), id = 10),
        GridItem("Defence &\nSecurity", Icons.Default.VerifiedUser, Color(0xFF43A047), id = 11),
        GridItem("Public Sector\nUndertakings", Icons.Default.Factory, Color(0xFFFB8C00), id = 12),
        GridItem("Teaching &\nEducation", Icons.Default.School, Color(0xFF1E88E5), id = 13),
        GridItem("Scientific &\nResearch", Icons.Default.Science, Color(0xFFE91E63), id = 14),
        GridItem("Job Classifications\n(Hierarchy)", Icons.Default.AccountTree, Color(0xFF673AB7), id = 15),
        GridItem("Health Care", Icons.Default.HealthAndSafety, Color(0xFF009688), id = 16),
        GridItem("Embassy -\nDiplomates", Icons.Default.Public, Color(0xFF6A1B9A), id = 17)
    )

    GridSection(items) { item ->
        val safeName = item.name.replace("\n", " ") // Removes the line break for a clean title
        navController.navigate("jobList/${item.id}/${Uri.encode(safeName)}")
    }
}

// 👇 FIXED: Added placeholder IDs and routed to jobList
@Composable
fun PrivateJobsSection(navController: NavController) {
    val items = listOf(
        // ⚠️ REPLACE these IDs (1, 2, etc.) with the real category IDs from your Django Admin panel!
        GridItem("Corporate", Icons.Default.BusinessCenter, Color(0xFF1E88E5), id = 1),
        GridItem("Factory", Icons.Default.Factory, Color(0xFFE53935), id = 2),
        GridItem("FMCG\nWholesalers", Icons.Default.ShoppingCart, Color(0xFF43A047), id = 3),
        GridItem("Daily Wages", Icons.Default.MonetizationOn, Color(0xFFFF9800), id = 4),
        GridItem("Contract\n3rd Party", Icons.Default.Receipt, Color(0xFF9C27B0), id = 5),
        GridItem("Semi-Govt", Icons.Default.AccountBalance, Color(0xFF009688), id = 6),
        GridItem("Mandi System", Icons.Default.Agriculture, Color(0xFF795548), id = 7),
        GridItem("Quick\nCommerce", Icons.Default.LocalShipping, Color(0xFF2196F3), id = 8)
    )

    GridSection(items) { item ->
        val safeName = item.name.replace("\n", " ") // Removes the line break for a clean title
        navController.navigate("jobList/${item.id}/${Uri.encode(safeName)}")
    }
}

@Composable
fun GovtSchemesSection(navController: NavController) {
    val items = listOf(
        GridItem("State Govt", Icons.Default.AccountBalance, Color(0xFF1E88E5)),
        GridItem("Central Government\nschemes", Icons.Default.AccountBalance, Color(0xFF43A047)),
        GridItem("Tourism\nDevelopment", Icons.Default.TravelExplore, Color(0xFFFF9800)),
        GridItem("PSU Schemes", Icons.Default.Factory, Color(0xFFE53935)),
        GridItem("Private NGO\nSchemes", Icons.Default.VolunteerActivism, Color(0xFF9C27B0)),
        GridItem("International\nSchemes", Icons.Default.Public, Color(0xFF009688)),
        GridItem("United Nations\nProgrammes", Icons.Default.Groups, Color(0xFF3F51B5)),
        GridItem("WHO\nProgrammes", Icons.Default.HealthAndSafety, Color(0xFF795548))
    )

    GridSection(items) { item ->
        navController.navigate("category_detail/${Uri.encode(item.name)}")
    }
}

@Composable
fun CareerIndustrySection(navController: NavController) {
    val items = listOf(
        GridItem(
            "Aviation Industry",
            Icons.Default.Flight,
            Color(0xFF1E88E5),
            route = "career_industry_detail/${Uri.encode("Aviation Industry")}"
        ),
        GridItem(
            "Hospitality Industry",
            Icons.Default.Hotel,
            Color(0xFFE53935),
            route = "career_industry_detail/${Uri.encode("Hospitality Industry")}"
        ),
        GridItem(
            "Real Estate Industry",
            Icons.Default.LocationCity,
            Color(0xFF6D4C41),
            route = "career_industry_detail/${Uri.encode("Real Estate Industry")}"
        ),
        GridItem(
            "Automobile Industry",
            Icons.Default.DirectionsCar,
            Color(0xFF00897B),
            route = "career_industry_detail/${Uri.encode("Automobile Industry")}"
        ),
        GridItem(
            "Telecommunications\nIndustry",
            Icons.Default.PhoneAndroid,
            Color(0xFF3949AB),
            route = "career_industry_detail/${Uri.encode("Telecommunications Industry")}"
        ),
        GridItem(
            "Retail &\nE-commerce",
            Icons.Default.ShoppingCart,
            Color(0xFFF57C00),
            route = "career_industry_detail/${Uri.encode("Retail & E-commerce")}"
        ),
        GridItem(
            "Career in Sports",
            Icons.Default.SportsSoccer,
            Color(0xFFFF5722),
            route = "career_industry_detail/${Uri.encode("Career in Sports")}"
        ),
        GridItem(
            "Agriculture &\nFood",
            Icons.Default.Agriculture,
            Color(0xFF43A047),
            route = "career_industry_detail/${Uri.encode("Agriculture & Food")}"
        )
    )

    GridSection(items) { item ->
        item.route?.let { navController.navigate(it) }
    }
}

@Composable
fun ReligiousStudiesSection(navController: NavController) {
    val items = listOf(
        GridItem(
            "Hindu",
            Icons.Default.TempleHindu,
            Color(0xFFE91E63),
            route = "religious_studies_detail/${Uri.encode("Hindu")}"
        ),
        GridItem(
            "Muslim",
            Icons.Default.Mosque,
            Color(0xFF1E88E5),
            route = "religious_studies_detail/${Uri.encode("Muslim")}"
        ),
        GridItem(
            "Sikhism",
            Icons.Default.TempleBuddhist,
            Color(0xFFFFC107),
            route = "religious_studies_detail/${Uri.encode("Sikhism")}"
        ),
        GridItem(
            "Christianity",
            Icons.Default.Church,
            Color(0xFF3F51B5),
            route = "religious_studies_detail/${Uri.encode("Christianity")}"
        ),
        GridItem(
            "Buddhists",
            Icons.Default.SelfImprovement,
            Color(0xFF4CAF50),
            route = "religious_studies_detail/${Uri.encode("Buddhists")}"
        ),
        GridItem(
            "Jain",
            Icons.Default.Spa,
            Color(0xFF9C27B0),
            route = "religious_studies_detail/${Uri.encode("Jain")}"
        ),
        GridItem(
            "Parsi",
            Icons.Default.LocalFireDepartment,
            Color(0xFFFF7043),
            route = "religious_studies_detail/${Uri.encode("Parsi")}"
        ),
        GridItem(
            "Adivasi\nReligions",
            Icons.Default.Groups,
            Color(0xFF00695C),
            route = "religious_studies_detail/${Uri.encode("Adivasi Religions")}"
        )
    )

    GridSection(items) { item ->
        item.route?.let { navController.navigate(it) }
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