package com.example.taalimisafar.ui.scholarships

import android.net.Uri // ✅ Added for encoding
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.taalimisafar.utils.AppLanguage
import com.example.taalimisafar.utils.LanguageManager
import com.example.taalimisafar.viewmodel.TypeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DynamicTypeScreen(
    navController: NavController,
    categoryId: Int,
    categoryName: String,
    viewModel: TypeViewModel = viewModel()
) {
    val types by viewModel.types.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val currentLanguage = LanguageManager.currentLanguage.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(categoryName, fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A237E))
            )
        },
        containerColor = Color(0xFFF5F7FA)
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF1A237E))
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.padding(padding).fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 1. ALL TYPES BUTTON
                item {
                    val allText = when (currentLanguage) {
                        AppLanguage.HINDI -> "All Types / सभी प्रकार"
                        AppLanguage.URDU -> "All Types / تمام اقسام"
                        else -> "All Types"
                    }
                    Card(
                        onClick = {
                            // ✅ Fix: Encode the title to handle the "/"
                            val encodedTitle = Uri.encode(allText)
                            navController.navigate("scholarship_list/$categoryId/0/$encodedTitle")
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EAF6)),
                        modifier = Modifier.fillMaxWidth().height(70.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxSize().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Category, null, tint = Color(0xFF1A237E))
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(allText, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A237E))
                        }
                    }
                }

                // 2. DYNAMIC TYPES
                items(types) { type ->
                    val displayName = remember(currentLanguage, type) {
                        when (currentLanguage) {
                            AppLanguage.HINDI -> if (!type.nameHi.isNullOrBlank()) "${type.name} / ${type.nameHi}" else type.name
                            AppLanguage.URDU -> if (!type.nameUr.isNullOrBlank()) "${type.name} / ${type.nameUr}" else type.name
                            else -> type.name
                        }
                    }

                    Card(
                        onClick = {
                            // ✅ Fix: Encode the title to handle the "/"
                            val encodedTitle = Uri.encode(displayName)
                            navController.navigate("scholarship_list/$categoryId/${type.id}/$encodedTitle")
                        },
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth().height(80.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.CenterStart) {
                            Text(displayName, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}