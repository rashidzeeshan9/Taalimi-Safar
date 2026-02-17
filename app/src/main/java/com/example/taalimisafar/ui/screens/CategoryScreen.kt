package com.example.taalimisafar.ui.screens

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
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.DesignServices
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Factory
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.filled.Work
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
        containerColor = Color(0xFFF5F5F5), // Match Home background
        topBar = {
            TopAppBar(
                title = { Text(text = categoryTitle) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF5F5F5) // Seamless blend
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (categoryId == "academic") {
                AcademicSection(navController)
            } else if (categoryId == "diploma") {
                DiplomaSection(navController)
            } else if (categoryId == "internships") {
                InternshipSection(navController)
            } else if (categoryId == "important_dates") {
                ImportantDatesSection(navController)
            } else if (categoryId == "skills") {
                SkillDevelopmentSection(navController)
            } else if (categoryId == "women") {
                WomenEmpowermentSection(navController)
            } else if (categoryId == "govt_jobs") {
                GovtJobsSection(navController)
            } else if (categoryId == "private_jobs") {
                PrivateJobsSection(navController)
            } else if (categoryId == "govt_schemes") {
                GovtSchemesSection(navController)
            } else {
                // Default placeholder for other sections
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Welcome to the $categoryTitle Section!\n(ID: $categoryId)",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// --- ACADEMIC SECTION ---
@Composable
fun AcademicSection(navController: NavController) {
    val streams = listOf(
        AcademicItem("Art", Icons.Default.Brush, Color(0xFFE91E63)),       // Pink
        AcademicItem("Commerce", Icons.Default.ShoppingCart, Color(0xFF4CAF50)), // Green
        AcademicItem("IT", Icons.Default.Computer, Color(0xFF2196F3)),     // Blue
        AcademicItem("Law", Icons.Default.Gavel, Color(0xFF9C27B0)),       // Purple
        AcademicItem("Science", Icons.Default.Science, Color(0xFFFF9800))  // Orange
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(streams) { stream ->
            AcademicCard(stream)
        }
    }
}

data class AcademicItem(
    val name: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun AcademicCard(item: AcademicItem) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White), // White like Home
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp) // Match Home height
            .clickable { /* Handle click later */ }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icon with Circular Background (Like Home)
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

            // Text Style (Like Home)
            Text(
                text = item.name,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF37474F),
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

// --- DIPLOMA SECTION ---
@Composable
fun DiplomaSection(navController: NavController) {
    val courses = listOf(
        DiplomaItem("Health Care", Icons.Default.HealthAndSafety, Color(0xFFE53935)),      // Red
        DiplomaItem("Trade Diploma", Icons.Default.Business, Color(0xFFFB8C00)),           // Orange
        DiplomaItem("Vocational Diploma", Icons.Default.Work, Color(0xFF43A047)),          // Green
        DiplomaItem("Technical Diploma", Icons.Default.Computer, Color(0xFF1E88E5)),       // Blue
        DiplomaItem("Steno Typist", Icons.Default.Keyboard, Color(0xFF8E24AA))             // Purple
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(courses) { course ->
            DiplomaCard(course)
        }
    }
}

data class DiplomaItem(
    val name: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun DiplomaCard(item: DiplomaItem) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clickable { /* Handle click later */ }
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
                fontSize = 14.sp, // Slightly smaller to fit "Vocational Diploma" if needed
                fontWeight = FontWeight.Bold,
                color = Color(0xFF37474F),
                textAlign = TextAlign.Center,
                maxLines = 2,
                lineHeight = 16.sp
            )
        }
    }
}

// --- INTERNSHIP SECTION ---
@Composable
fun InternshipSection(navController: NavController) {
    val internships = listOf(
        InternshipItem("Paid Internship", Icons.Default.MonetizationOn, Color(0xFF4CAF50)), // Green
        InternshipItem("Virtual/Remote", Icons.Default.Laptop, Color(0xFF2196F3)),            // Blue
        InternshipItem("Creative & Design", Icons.Default.DesignServices, Color(0xFFE91E63)), // Pink
        InternshipItem("Academic Credit", Icons.Default.School, Color(0xFFFFC107)),           // Amber
        InternshipItem("Summer/Winter", Icons.Default.AcUnit, Color(0xFF00BCD4)),             // Cyan
        InternshipItem("Corporate & Business", Icons.Default.BusinessCenter, Color(0xFF607D8B)) // Blue Grey
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(internships) { item ->
            InternshipCard(item)
        }
    }
}

data class InternshipItem(
    val name: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun InternshipCard(item: InternshipItem) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clickable { /* Handle click later */ }
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
                fontSize = 13.sp, // Slightly smaller for longer titles
                fontWeight = FontWeight.Bold,
                color = Color(0xFF37474F),
                textAlign = TextAlign.Center,
                maxLines = 2,
                lineHeight = 16.sp
            )
        }
    }
}

// --- IMPORTANT DATES SECTION ---
@Composable
fun ImportantDatesSection(navController: NavController) {
    val exams = listOf(
        ImportantDateItem("CUET", Icons.Default.Event, Color(0xFF673AB7)),            // Deep Purple
        ImportantDateItem("NEET", Icons.Default.HealthAndSafety, Color(0xFFBE1B59)),  // Pink/Red
        ImportantDateItem("JEE Mains", Icons.Default.Science, Color(0xFF1E88E5)),     // Blue
        ImportantDateItem("NDA", Icons.Default.VerifiedUser, Color(0xFF43A047)),      // Green
        ImportantDateItem("NTA", Icons.Default.EditCalendar, Color(0xFFFB8C00)),      // Orange
        ImportantDateItem("UPSC", Icons.Default.AccountBalance, Color(0xFF5D4037))    // Brown
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(exams) { exam ->
            ImportantDateCard(exam)
        }
    }
}

data class ImportantDateItem(
    val name: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun ImportantDateCard(item: ImportantDateItem) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clickable { /* Handle click later */ }
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
                fontSize = 15.sp, // Standard size
                fontWeight = FontWeight.Bold,
                color = Color(0xFF37474F),
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

// --- SKILL DEVELOPMENT SECTION ---
@Composable
fun SkillDevelopmentSection(navController: NavController) {
    val skills = listOf(
        SkillItem("Soft Skill -\nProblem-Solving", Icons.Default.Lightbulb, Color(0xFFFFC107)), // Amber
        SkillItem("Communication\nSkills", Icons.Default.Chat, Color(0xFF2196F3)),             // Blue
        SkillItem("Interpersonal &\nSocial Skills", Icons.Default.Groups, Color(0xFFE91E63)),  // Pink
        SkillItem("Leadership &\nInfluence", Icons.Default.Campaign, Color(0xFFFF5722)),       // Deep Orange
        SkillItem("Computer\nSkills", Icons.Default.Computer, Color(0xFF00BCD4)),              // Cyan
        SkillItem("English", Icons.Default.Translate, Color(0xFF673AB7))                       // Deep Purple
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(skills) { skill ->
            SkillCard(skill)
        }
    }
}

data class SkillItem(
    val name: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun SkillCard(item: SkillItem) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .height(145.dp) // Taller to fit 3 lines if needed
            .clickable { /* Handle click later */ }
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

// --- WOMEN EMPOWERMENT SECTION ---
@Composable
fun WomenEmpowermentSection(navController: NavController) {
    val schemes = listOf(
        WomenItem("Business\nE-Commerce", Icons.Default.ShoppingBag, Color(0xFFE91E63)),       // Pink
        WomenItem("One Stop Centres\n(Sakhi)", Icons.Default.VolunteerActivism, Color(0xFF9C27B0)), // Purple
        WomenItem("Udyogini\nScheme", Icons.Default.BusinessCenter, Color(0xFFFB8C00)),  // Orange
        WomenItem("PMUY, IGMSY,\nMKSP", Icons.Default.Agriculture, Color(0xFF43A047)),               // Green
        WomenItem("Mahila Police\nVolunteers", Icons.Default.LocalPolice, Color(0xFF3F51B5))    // Indigo
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(schemes) { scheme ->
            WomenCard(scheme)
        }
    }
}

data class WomenItem(
    val name: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun WomenCard(item: WomenItem) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .height(145.dp) // Taller to fit 3 lines
            .clickable { /* Handle click later */ }
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

// --- GOVT JOBS SECTION ---
@Composable
fun GovtJobsSection(navController: NavController) {
    val jobs = listOf(
        GovtJobsItem("Civil Services\n(Administrative)", Icons.Default.Gavel, Color(0xFFE53935)), // Red
        GovtJobsItem("Defence &\nSecurity", Icons.Default.VerifiedUser, Color(0xFF43A047)),       // Green
        GovtJobsItem("Public Sector\nUndertakings", Icons.Default.Factory, Color(0xFFFB8C00)),    // Orange
        GovtJobsItem("Teaching &\nEducation", Icons.Default.School, Color(0xFF1E88E5)),           // Blue
        GovtJobsItem("Scientific &\nResearch", Icons.Default.Science, Color(0xFFE91E63)),         // Pink
        GovtJobsItem("Job Classifications\n(Hierarchy)", Icons.Default.AccountTree, Color(0xFF673AB7)) // Deep Purple
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(jobs) { job ->
            GovtJobsCard(job)
        }
    }
}

data class GovtJobsItem(
    val name: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun GovtJobsCard(item: GovtJobsItem) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .height(145.dp) // Taller to fit 3 lines
            .clickable { /* Handle click later */ }
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

// --- PRIVATE JOBS SECTION ---
@Composable
fun PrivateJobsSection(navController: NavController) {
    val jobs = listOf(
        PrivateJobItem("Accounts", Icons.Default.Receipt, Color(0xFF4CAF50)),          // Green
        PrivateJobItem("HR", Icons.Default.Groups, Color(0xFF9C27B0)),                 // Purple
        PrivateJobItem("Marketing", Icons.Default.TrendingUp, Color(0xFFE91E63)),      // Pink
        PrivateJobItem("Creative &\nContent", Icons.Default.DesignServices, Color(0xFFFF9800)), // Orange
        PrivateJobItem("Operations", Icons.Default.Settings, Color(0xFF607D8B)),       // Blue Grey
        PrivateJobItem("Quick\nCommerce", Icons.Default.LocalShipping, Color(0xFF2196F3)) // Blue
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(jobs) { job ->
            PrivateJobsCard(job)
        }
    }
}

data class PrivateJobItem(
    val name: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun PrivateJobsCard(item: PrivateJobItem) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .height(145.dp) // Taller to fit 3 lines
            .clickable { /* Handle click later */ }
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

// --- GOVT SCHEMES SECTION ---
@Composable
fun GovtSchemesSection(navController: NavController) {
    val schemes = listOf(
        GovtSchemeItem("PM VidyaLaxmi", Icons.Default.School, Color(0xFF1E88E5)),              // Blue
        GovtSchemeItem("CSIS", Icons.Default.AccountBalance, Color(0xFF43A047)),               // Green
        GovtSchemeItem("National\nScholarship Portal", Icons.AutoMirrored.Filled.MenuBook, Color(0xFFFF9800)), // Orange
        GovtSchemeItem("Begum Hazrat\nMahal Scholarship", Icons.Default.VolunteerActivism, Color(0xFF9C27B0)), // Purple
        GovtSchemeItem("PM Mudra\nYojna", Icons.Default.MonetizationOn, Color(0xFF009688)),    // Teal
        GovtSchemeItem("Ayushman Bharat\n- PMJAY", Icons.Default.HealthAndSafety, Color(0xFFE53935)) // Red
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(schemes) { scheme ->
            GovtSchemeCard(scheme)
        }
    }
}

data class GovtSchemeItem(
    val name: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun GovtSchemeCard(item: GovtSchemeItem) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .height(145.dp) // Taller to fit 3 lines
            .clickable { /* Handle click later */ }
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
