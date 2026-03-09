package com.example.taalimisafar.ui.schemes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.taalimisafar.data.model.Schemes
import com.example.taalimisafar.utils.AppLanguage
import com.example.taalimisafar.utils.LanguageManager
import com.example.taalimisafar.viewmodel.SchemeViewModel

private val IndigoPrimary = Color(0xFF4F46E5)
private val IndigoLight = Color(0xFFE0E7FF)
private val BackgroundSlate = Color(0xFFF8FAFC)
private val TextDark = Color(0xFF1E293B)
private val TextMuted = Color(0xFF64748B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchemeListScreen(
    categoryId: Int,
    categoryName: String,
    viewModel: SchemeViewModel,
    onBackClick: () -> Unit,
    onSchemeClick: (Int) -> Unit
) {
    val currentLanguage = LanguageManager.currentLanguage.value
    val categories by viewModel.categories.collectAsState()
    val schemes by viewModel.schemes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Filters State
    var selectedCategoryId by remember { mutableStateOf<Int?>(if (categoryId == 0) null else categoryId) }

    // Status Dropdown State
    var isStatusActive by remember { mutableStateOf(true) }
    var statusMenuExpanded by remember { mutableStateOf(false) }


    LaunchedEffect(selectedCategoryId) {
        viewModel.fetchCategories()
        viewModel.fetchSchemes(categoryId = selectedCategoryId)
    }

    val filteredSchemes = schemes.filter { it.is_active == isStatusActive }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Government Schemes", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = IndigoPrimary)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundSlate)
                .padding(paddingValues)
        ) {
            // Header
            val selectedCategoryObj = categories.find { it.id == selectedCategoryId }
            val headerTitleEng = selectedCategoryObj?.name ?: "All Schemes"

            val headerTitleTrans = when (currentLanguage) {
                AppLanguage.HINDI -> selectedCategoryObj?.name_hi
                AppLanguage.URDU -> selectedCategoryObj?.name_ur
                else -> null
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Text(
                    text = headerTitleEng,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                if (!headerTitleTrans.isNullOrBlank() && headerTitleTrans != headerTitleEng) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = headerTitleTrans,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = IndigoPrimary
                    )
                }
            }

            //category
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(bottom = 12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    ModernChip(
                        text = "All Categories",
                        isSelected = selectedCategoryId == null,
                        onClick = { selectedCategoryId = null }
                    )
                }
                items(categories) { category ->
                    val transName = when (currentLanguage) {
                        AppLanguage.HINDI -> category.name_hi
                        AppLanguage.URDU -> category.name_ur
                        else -> null
                    }
                    val displayName = if (!transName.isNullOrBlank()) transName else category.name

                    ModernChip(
                        text = displayName,
                        isSelected = selectedCategoryId == category.id,
                        onClick = { selectedCategoryId = category.id }
                    )
                }
            }

            // status drop
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Status:", fontSize = 14.sp, color = TextMuted, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.width(8.dp))

                Box {
                    Surface(
                        modifier = Modifier.clickable { statusMenuExpanded = true },
                        shape = RoundedCornerShape(8.dp),
                        color = BackgroundSlate,
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isStatusActive) "Active" else "Closed",
                                color = TextDark,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown", tint = TextDark)
                        }
                    }
                    DropdownMenu(
                        expanded = statusMenuExpanded,
                        onDismissRequest = { statusMenuExpanded = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Active Schemes", color = TextDark) },
                            onClick = {
                                isStatusActive = true
                                statusMenuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Closed Schemes", color = TextDark) },
                            onClick = {
                                isStatusActive = false
                                statusMenuExpanded = false
                            }
                        )
                    }
                }
            }

            HorizontalDivider(color = Color(0xFFE2E8F0))

            // scheme list
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = IndigoPrimary)
                }
            } else if (filteredSchemes.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = if (isStatusActive) "No active schemes found." else "No closed schemes found.",
                        color = TextMuted
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredSchemes) { scheme ->
                        SchemeCard(
                            scheme = scheme,
                            currentLanguage = currentLanguage,
                            onClick = { onSchemeClick(scheme.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ModernChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) IndigoPrimary else Color.White,
        border = if (isSelected) null else BorderStroke(1.dp, Color(0xFFE2E8F0)),
        shadowElevation = if (isSelected) 4.dp else 0.dp
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else TextMuted,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun SchemeCard(
    scheme: Schemes,
    currentLanguage: AppLanguage,
    onClick: () -> Unit
) {
    fun getTrans(hi: String?, ur: String?): String? = when (currentLanguage) {
        AppLanguage.HINDI -> hi
        AppLanguage.URDU -> ur
        else -> null
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = BackgroundSlate
                ) {
                    val imageUrl = scheme.image?.let { img ->
                        if (img.startsWith("http")) img else "http://10.0.2.2:8000$img"
                    }
                    if (!imageUrl.isNullOrBlank() && imageUrl != "null") {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(imageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(Icons.Default.AccountBalance, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.padding(12.dp))
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    // English Title
                    Text(text = scheme.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextDark, maxLines = 2, overflow = TextOverflow.Ellipsis)

                    // Translated Title
                    val transTitle = getTrans(scheme.title_hi, scheme.title_ur)
                    if (!transTitle.isNullOrBlank() && transTitle != "null" && transTitle.trim() != scheme.title.trim()) {
                        Text(text = transTitle, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = IndigoPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top = 2.dp))
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // ENGLISH Introduced By
                    Text(text = "By: ${scheme.introduced_by}", fontSize = 13.sp, color = TextMuted, fontWeight = FontWeight.Medium)

                    // TRANSLATED Introduced By
                    val transProvider = getTrans(scheme.introduced_by_hi, scheme.introduced_by_ur)
                    if (!transProvider.isNullOrBlank() && transProvider != "null" && transProvider.trim() != scheme.introduced_by.trim()) {
                        Text(text = transProvider, fontSize = 12.sp, color = IndigoPrimary, fontWeight = FontWeight.Medium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Target Audience
            Surface(color = IndigoLight, shape = RoundedCornerShape(6.dp)) {
                Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                    Text(
                        text = "For: ${scheme.target_audience.uppercase()}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = IndigoPrimary
                    )

                    val transAudience = getTrans(scheme.target_audience_hi, scheme.target_audience_ur)
                    if (!transAudience.isNullOrBlank() && transAudience != "null" && transAudience.trim() != scheme.target_audience.trim()) {
                        Text(
                            text = transAudience,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = IndigoPrimary,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(12.dp))

            // Footer Info
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(scheme.state ?: "Central Government", fontSize = 13.sp, color = TextMuted)
                }

                if (!scheme.last_date_to_apply.isNullOrBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DateRange, contentDescription = null, tint = Color(0xFFE11D48), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(scheme.last_date_to_apply, fontSize = 13.sp, color = Color(0xFFE11D48), fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}