package com.example.taalimisafar.ui.jobs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.taalimisafar.data.model.Job
import com.example.taalimisafar.utils.AppLanguage
import com.example.taalimisafar.utils.LanguageManager
import com.example.taalimisafar.viewmodel.JobViewModel

// --- MODERN COLOR PALETTE ---
val IndigoPrimary = Color(0xFF4F46E5)
val IndigoLight = Color(0xFFEEF2FF)
val BackgroundSlate = Color(0xFFF8FAFC)
val TextDark = Color(0xFF1E293B)
val TextMuted = Color(0xFF64748B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobListScreen(
    categoryId: Int,
    categoryName: String,
    viewModel: JobViewModel,
    onBackClick: () -> Unit,
    onJobClick: (Job) -> Unit
) {
    val jobs by viewModel.jobs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val language = LanguageManager.currentLanguage.value

    // Helper function to create "English / Translated" labels for UI
    fun dualLabel(en: String, hi: String, ur: String): String {
        return when (language) {
            AppLanguage.HINDI -> "$en / $hi"
            AppLanguage.URDU -> "$en / $ur"
            else -> en
        }
    }

    // --- DUAL TRANSLATIONS FOR UI LABELS ---
    val strSearch = dualLabel("Search jobs...", "नौकरी खोजें...", "نوکری تلاش کریں...")
    val strRemoteOnly = dualLabel("Remote Only", "केवल रिमोट", "صرف ریموٹ")
    val strLocation = dualLabel("Location", "स्थान", "مقام")
    val strExperience = dualLabel("Experience", "अनुभव", "تجربہ")
    val strQualification = dualLabel("Qualification", "योग्यता", "قابلیت")
    val strSalary = dualLabel("Salary", "वेतन", "تنخواہ")
    val strYear = dualLabel("Year", "वर्ष", "سال")
    val strClear = dualLabel("Clear", "साफ़ करें", "صاف کریں")
    val strAnyAll = dualLabel("Any / All", "कोई भी / सभी", "کوئی بھی / تمام")

    // --- TRANSLATE TOP APP BAR CATEGORY NAME ---
    val transCat = when (categoryName.uppercase().trim()) {
        "CORPORATE" -> when (language) { AppLanguage.HINDI -> "कॉर्पोरेट"; AppLanguage.URDU -> "کارپوریٹ"; else -> null }
        "FACTORY" -> when (language) { AppLanguage.HINDI -> "कारखाना"; AppLanguage.URDU -> "فیکٹری"; else -> null }
        "FMCG WHOLESALERS", "FMCG WHOLESALER" -> when (language) { AppLanguage.HINDI -> "FMCG थोक विक्रेता"; AppLanguage.URDU -> "FMCG تھوک فروش"; else -> null }
        "DAILY WAGES" -> when (language) { AppLanguage.HINDI -> "दैनिक वेतन"; AppLanguage.URDU -> "روزانہ اجرت"; else -> null }
        "CONTRACT 3RD PARTY" -> when (language) { AppLanguage.HINDI -> "अनुबंध थर्ड पार्टी"; AppLanguage.URDU -> "معاہدہ تھرڈ پارٹی"; else -> null }
        "SEMI-GOVT", "SEMI GOVT" -> when (language) { AppLanguage.HINDI -> "अर्ध-सरकारी"; AppLanguage.URDU -> "نیم سرکاری"; else -> null }
        "MANDI SYSTEM" -> when (language) { AppLanguage.HINDI -> "मंडी प्रणाली"; AppLanguage.URDU -> "منڈی سسٹم"; else -> null }
        "QUICK COMMERCE" -> when (language) { AppLanguage.HINDI -> "क्विक कॉमर्स"; AppLanguage.URDU -> "کوئیک کامرس"; else -> null }
        else -> null
    }
    val displayCategoryName = if (!transCat.isNullOrBlank()) "$categoryName / $transCat" else categoryName

    // --- FILTER STATES ---
    var searchQuery by remember { mutableStateOf("") }
    var isRemoteOnly by remember { mutableStateOf(false) }
    var selectedLocation by remember { mutableStateOf<String?>(null) }
    var selectedExp by remember { mutableStateOf<String?>(null) }
    var selectedQual by remember { mutableStateOf<String?>(null) }
    var selectedSalary by remember { mutableStateOf<String?>(null) }
    var selectedYear by remember { mutableStateOf<String?>(null) }

    // Backend Dummy Data Options
    val expOptions = listOf("Fresher", "1-3 Years", "3-5 Years", "5+ Years")
    val locationOptions = listOf("Delhi", "Mumbai", "Bangalore", "Hyderabad", "Pune")
    val qualOptions = listOf("10th Pass", "12th Pass", "Graduate", "Post Graduate", "Diploma")
    val salaryOptions = listOf("0-3 Lakhs", "3-6 Lakhs", "6-10 Lakhs", "10+ Lakhs")
    val yearOptions = listOf("2024", "2025", "2026")

    // Helper to get Dual display for dummy dropdown options so API calls don't break
    fun getDualOption(en: String): String {
        if (language == AppLanguage.NONE) return en
        // Quick translations for dummy data
        val trans = when(en) {
            "Fresher" -> if (language == AppLanguage.HINDI) "फ्रेशर" else "فریشر"
            "Delhi" -> if (language == AppLanguage.HINDI) "दिल्ली" else "دہلی"
            "Mumbai" -> if (language == AppLanguage.HINDI) "मुंबई" else "ممبئی"
            "Bangalore" -> if (language == AppLanguage.HINDI) "बैंगलोर" else "بنگلور"
            "10th Pass" -> if (language == AppLanguage.HINDI) "10वीं पास" else "10ویں پاس"
            "Graduate" -> if (language == AppLanguage.HINDI) "स्नातक" else "گریجویٹ"
            else -> en // Keep original if no quick translation
        }
        return if (trans != en) "$en / $trans" else en
    }

    LaunchedEffect(isRemoteOnly, selectedExp, selectedLocation, selectedQual, selectedSalary, selectedYear, categoryId, searchQuery) {
        viewModel.fetchJobs(
            categoryId = categoryId,
            searchQuery = if (searchQuery.isBlank()) null else searchQuery,
            isRemote = if (isRemoteOnly) true else null,
            experience = selectedExp
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(displayCategoryName, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = IndigoPrimary)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().background(BackgroundSlate).padding(padding)
        ) {
            // --- SEARCH BAR ---
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                placeholder = { Text(strSearch, color = TextMuted) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = IndigoPrimary) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) { Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextMuted) }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White, unfocusedContainerColor = Color.White,
                    focusedBorderColor = IndigoPrimary, unfocusedBorderColor = Color(0xFFE2E8F0)
                )
            )

            // --- FILTER BAR ---
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                item {
                    FilterChip(
                        selected = isRemoteOnly,
                        onClick = { isRemoteOnly = !isRemoteOnly },
                        label = { Text(strRemoteOnly, fontWeight = FontWeight.Medium) },
                        leadingIcon = if (isRemoteOnly) { { Icon(Icons.Default.Check, null, Modifier.size(16.dp)) } } else null,
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = IndigoLight, selectedLabelColor = IndigoPrimary)
                    )
                }
                item { ModernDropdownFilter(label = if (selectedLocation != null) getDualOption(selectedLocation!!) else strLocation, options = locationOptions, strAnyAll = strAnyAll, getDisplay = ::getDualOption) { selectedLocation = it } }
                item { ModernDropdownFilter(label = if (selectedExp != null) getDualOption(selectedExp!!) else strExperience, options = expOptions, strAnyAll = strAnyAll, getDisplay = ::getDualOption) { selectedExp = it } }
                item { ModernDropdownFilter(label = if (selectedQual != null) getDualOption(selectedQual!!) else strQualification, options = qualOptions, strAnyAll = strAnyAll, getDisplay = ::getDualOption) { selectedQual = it } }
                item { ModernDropdownFilter(label = if (selectedSalary != null) getDualOption(selectedSalary!!) else strSalary, options = salaryOptions, strAnyAll = strAnyAll, getDisplay = ::getDualOption) { selectedSalary = it } }

                if (isRemoteOnly || selectedExp != null || selectedLocation != null || selectedQual != null || selectedSalary != null) {
                    item {
                        TextButton(onClick = {
                            isRemoteOnly = false; selectedExp = null; selectedLocation = null; selectedQual = null; selectedSalary = null
                        }) {
                            Text(strClear, color = Color.Red, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // --- JOB LIST ---
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = IndigoPrimary) }
            } else if (jobs.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    val strNoJobs = dualLabel("No jobs found.", "कोई नौकरी नहीं मिली", "کوئی نوکری نہیں ملی")
                    Text(strNoJobs, color = TextMuted)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(jobs) { job ->
                        JobItemCard(job = job, language = language, onClick = { onJobClick(job) })
                    }
                }
            }
        }
    }
}

// --- DROPDOWN COMPONENT ---
@Composable
fun ModernDropdownFilter(label: String, options: List<String>, strAnyAll: String, getDisplay: (String) -> String, onSelectionChanged: (String?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        FilterChip(
            selected = label.contains("/").not() && label != "Location" && label != "Experience" && label != "Qualification" && label != "Salary",
            onClick = { expanded = true },
            label = { Text(label, fontWeight = FontWeight.Medium) },
            trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = Modifier.size(18.dp)) },
            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = IndigoLight, selectedLabelColor = IndigoPrimary)
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.background(Color.White)) {
            DropdownMenuItem(text = { Text(strAnyAll, color = TextMuted) }, onClick = { onSelectionChanged(null); expanded = false })
            options.forEach { option ->
                DropdownMenuItem(text = { Text(getDisplay(option), color = TextDark) }, onClick = { onSelectionChanged(option); expanded = false })
            }
        }
    }
}

// --- TRUE DUAL-LANGUAGE JOB CARD ---
@Composable
fun JobItemCard(job: Job, language: AppLanguage, onClick: () -> Unit) {
    // Labels
    val strEnds = when (language) { AppLanguage.HINDI -> "Ends / अंतिम तिथि:"; AppLanguage.URDU -> "Ends / آخری تاریخ:"; else -> "Ends:" }

    // Extracted English & Translated Data
    val enTitle = job.title ?: "Unknown Title"
    val transTitle = when (language) { AppLanguage.HINDI -> job.title_hi; AppLanguage.URDU -> job.title_ur; else -> null }

    val enOrg = job.organization_name ?: ""
    val transOrg = when (language) { AppLanguage.HINDI -> job.organization_name_hi; AppLanguage.URDU -> job.organization_name_ur; else -> null }

    val enExp = job.experience_required ?: "Fresher"
    val transExp = when (language) {
        AppLanguage.HINDI -> job.experience_required_hi.takeUnless { it.isNullOrBlank() } ?: if (enExp.equals("Fresher", true)) "फ्रेशर" else null
        AppLanguage.URDU -> job.experience_required_ur.takeUnless { it.isNullOrBlank() } ?: if (enExp.equals("Fresher", true)) "فریشر" else null
        else -> null
    }

    val enSalary = job.salary_or_payscale ?: "Not Disclosed"
    val transSalary = when (language) {
        AppLanguage.HINDI -> job.salary_or_payscale_hi.takeUnless { it.isNullOrBlank() } ?: if (enSalary == "Not Disclosed") "खुलासा नहीं" else null
        AppLanguage.URDU -> job.salary_or_payscale_ur.takeUnless { it.isNullOrBlank() } ?: if (enSalary == "Not Disclosed") "ظاہر نہیں" else null
        else -> null
    }

    val enLoc = if (job.is_remote) "Remote" else job.location_state_name ?: "Various"
    val transLoc = if (job.is_remote) {
        when (language) { AppLanguage.HINDI -> "रिमोट"; AppLanguage.URDU -> "ریموٹ"; else -> null }
    } else {
        when (language) {
            AppLanguage.HINDI -> job.location_hi.takeUnless { it.isNullOrBlank() } ?: if (enLoc == "Various") "विभिन्न" else null
            AppLanguage.URDU -> job.location_ur.takeUnless { it.isNullOrBlank() } ?: if (enLoc == "Various") "مختلف" else null
            else -> null
        }
    }

    val enSector = job.category_sector?.trim()?.uppercase() ?: "JOB"
    val transSector = when (enSector) {
        "PRIVATE JOB", "PRIVATE" -> when (language) { AppLanguage.HINDI -> "प्राइवेट नौकरी"; AppLanguage.URDU -> "نجی نوکری"; else -> null }
        "GOVERNMENT JOB", "GOVERNMENT" -> when (language) { AppLanguage.HINDI -> "सरकारी नौकरी"; AppLanguage.URDU -> "سرکاری نوکری"; else -> null }
        else -> null
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Logo & Titles
            Row(verticalAlignment = Alignment.Top) {
                Surface(modifier = Modifier.size(52.dp), shape = RoundedCornerShape(12.dp), color = IndigoLight) {
                    if (!job.organization_logo.isNullOrEmpty() && job.organization_logo != "null") {
                        val imageUrl = if (job.organization_logo.startsWith("http")) job.organization_logo else "http://10.0.2.2:8000" + job.organization_logo
                        AsyncImage(model = ImageRequest.Builder(LocalContext.current).data(imageUrl).crossfade(true).build(), contentDescription = null, contentScale = ContentScale.Crop)
                    } else {
                        Icon(Icons.Default.Business, contentDescription = null, modifier = Modifier.padding(12.dp), tint = IndigoPrimary)
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    // Stacked Title
                    Text(text = enTitle, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextDark, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    if (!transTitle.isNullOrBlank() && transTitle != enTitle) {
                        Text(text = transTitle, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = IndigoPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    // Stacked Org
                    Text(text = enOrg, fontSize = 14.sp, color = TextMuted, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    if (!transOrg.isNullOrBlank() && transOrg != enOrg) {
                        Text(text = transOrg, fontSize = 12.sp, color = IndigoPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Info Badges Row (Stacked Location & Experience)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                // Location Stacked Badge
                Row(modifier = Modifier.background(Color(0xFFF1F5F9), RoundedCornerShape(8.dp)).padding(horizontal = 8.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp), tint = TextMuted)
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(enLoc, fontSize = 12.sp, color = TextDark, fontWeight = FontWeight.Medium, maxLines = 1)
                        if (!transLoc.isNullOrBlank() && transLoc != enLoc) {
                            Text(transLoc, fontSize = 10.sp, color = IndigoPrimary, fontWeight = FontWeight.Medium, maxLines = 1)
                        }
                    }
                }
                // Experience Stacked Badge
                Row(modifier = Modifier.background(Color(0xFFF1F5F9), RoundedCornerShape(8.dp)).padding(horizontal = 8.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.WorkOutline, contentDescription = null, modifier = Modifier.size(16.dp), tint = TextMuted)
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(enExp, fontSize = 12.sp, color = TextDark, fontWeight = FontWeight.Medium, maxLines = 1)
                        if (!transExp.isNullOrBlank() && transExp != enExp) {
                            Text(transExp, fontSize = 10.sp, color = IndigoPrimary, fontWeight = FontWeight.Medium, maxLines = 1)
                        }
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 1.dp, color = Color(0xFFF1F5F9))

            // Footer Row: Stacked Sector, Salary, Date
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                Surface(color = IndigoLight, shape = RoundedCornerShape(6.dp)) {
                    Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = enSector, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = IndigoPrimary)
                        if (!transSector.isNullOrBlank() && transSector != enSector) {
                            Text(text = transSector, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = IndigoPrimary)
                        }
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    // Stacked Salary
                    Text(text = enSalary, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF10B981))
                    if (!transSalary.isNullOrBlank() && transSalary != enSalary) {
                        Text(text = transSalary, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = IndigoPrimary)
                    }

                    if (!job.last_date_to_apply.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "$strEnds ${job.last_date_to_apply}", fontSize = 11.sp, color = Color(0xFFEF4444), fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}