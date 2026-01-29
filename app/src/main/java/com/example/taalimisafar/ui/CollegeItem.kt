//package com.example.taalimisafar.ui
//
//package com.example.taalimisafar.ui.colleges
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import com.example.taalimisafar.data.model.College // Your data model
//import com.example.taalimisafar.ui.components.DualLangText // Import your new component
//
//@Composable
//fun CollegeItem(college: College) {
//    Card(
//        modifier = Modifier.padding(8.dp),
//        elevation = CardDefaults.cardElevation(4.dp)
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//
//            // 🔥 AUTOMATIC DUAL LANGUAGE TITLE
//            DualLangText(
//                english = college.name,       // Backend: "Jamia Millia Islamia"
//                hindi = college.name_hi,      // Backend: "जामिया मिलिया इस्लामिया"
//                urdu = college.name_ur,       // Backend: "جامعہ ملیہ اسلامیہ"
//                style = MaterialTheme.typography.titleLarge,
//                fontWeight = FontWeight.Bold
//            )
//
//            // 🔥 AUTOMATIC DUAL LANGUAGE ADDRESS
//            DualLangText(
//                english = college.address,
//                hindi = college.address_hi,
//                urdu = college.address_ur,
//                style = MaterialTheme.typography.bodyMedium,
//                color = Color.Gray
//            )
//        }
//    }
//}