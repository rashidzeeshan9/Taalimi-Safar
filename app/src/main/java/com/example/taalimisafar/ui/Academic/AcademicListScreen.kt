package com.example.taalimisafar.ui.academic

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taalimisafar.data.model.Course
import com.example.taalimisafar.utils.AppLanguage
import com.example.taalimisafar.utils.LanguageManager
import com.example.taalimisafar.viewmodel.AcademicViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcademicListScreen(
    streamId: Int,
    streamName: String,
    viewModel: AcademicViewModel,
    onBackClick: () -> Unit,
    onCourseClick: (Int) -> Unit
) {

    val allCourses by viewModel.courses.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val language = LanguageManager.currentLanguage.value

    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(streamId) {
        viewModel.fetchCourses(streamId)
    }

    val filteredCourses = allCourses.filter { c ->
        searchQuery.isBlank() ||
                (c.courseName.contains(searchQuery, true)) ||
                (c.courseName_hi?.contains(searchQuery, true) == true) ||
                (c.courseName_ur?.contains(searchQuery, true) == true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(streamName, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4F46E5)
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
                .padding(padding)
        ) {

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search courses...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4F46E5),
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            Box(modifier = Modifier.fillMaxSize()) {

                if (isLoading && allCourses.isEmpty()) {

                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFF4F46E5)
                    )

                } else if (filteredCourses.isEmpty() && !isLoading) {

                    Text(
                        text = "No courses found.",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Gray
                    )

                } else {

                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        items(filteredCourses) { course ->
                            AcademicCourseCard(
                                course = course,
                                lang = language,
                                onClick = { onCourseClick(it.id) }
                            )
                        }

                    }

                }

            }

        }

    }

}

@Composable
fun AcademicCourseCard(
    course: Course,
    lang: AppLanguage,
    onClick: (Course) -> Unit
) {
    // Like Diploma: always show English first, then Hindi/Urdu below when that language is opted
    val transTitle = when (lang) {
        AppLanguage.HINDI -> course.courseName_hi
        AppLanguage.URDU -> course.courseName_ur
        else -> null
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(course) },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFEEF2FF)),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = "Course Icon",
                    tint = Color(0xFF4F46E5),
                    modifier = Modifier.size(36.dp)
                )

            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {

                // English course name (always shown)
                Text(
                    text = course.courseName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )

                // Hindi or Urdu below when that language is selected (like Diploma)
                if (!transTitle.isNullOrBlank() && transTitle != course.courseName) {
                    Text(
                        text = transTitle,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF4F46E5)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                    if (!course.duration.isNullOrBlank()) {

                        Surface(
                            color = Color(0xFFEEF2FF),
                            shape = RoundedCornerShape(8.dp)
                        ) {

                            Text(
                                course.duration,
                                modifier = Modifier.padding(
                                    horizontal = 8.dp,
                                    vertical = 4.dp
                                ),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4F46E5)
                            )

                        }

                    }

                }

            }

        }

    }

}