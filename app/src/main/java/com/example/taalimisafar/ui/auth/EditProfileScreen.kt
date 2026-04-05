package com.example.taalimisafar.ui.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.taalimisafar.viewmodel.AuthViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
    val context = LocalContext.current

    // --- USER DATA ---
    // 🔥 TODO: Replace these initial values with actual data from your AuthViewModel!
    // Example: val email by viewModel.userEmail.collectAsState(initial = "")
    var username by remember { mutableStateOf("zaqwsg") }
    val email = "real.user@example.com" // Wire this to your ViewModel!
    var fullName by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var school by remember { mutableStateOf("") }
    var degree by remember { mutableStateOf("") }

    // --- 60 DAY USERNAME LOGIC ---
    val daysSinceLastUsernameChange = 45 // TODO: Fetch from backend
    var usernameError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = Color(0xFFF9FAFB), // Soft off-white background for contrast
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF9FAFB))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Keep your profile updated to get better recommendations.",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))

            // ==========================================
            // SECTION 1: ACCOUNT INFORMATION
            // ==========================================
            Text(
                text = "Account Information",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF6750A4),
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
            )
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    EditableProfileItem(
                        icon = Icons.Default.Person,
                        label = "Username",
                        value = username,
                        errorMessage = usernameError,
                        onSave = { newUsername ->
                            if (newUsername != username) {
                                if (daysSinceLastUsernameChange < 60) {
                                    usernameError = "You can only change your username once every 60 days."
                                    false
                                } else {
                                    username = newUsername
                                    usernameError = null
                                    Toast.makeText(context, "Username changed successfully!", Toast.LENGTH_SHORT).show()
                                    true
                                }
                            } else {
                                usernameError = null
                                true
                            }
                        }
                    )
                    HorizontalDivider(color = Color(0xFFF0F0F0))

                    // Read-Only Email Field
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Email, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = "Email Address (Account ID)", fontSize = 12.sp, color = Color.Gray)
                            Text(text = email, fontSize = 16.sp, color = Color.DarkGray)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ==========================================
            // SECTION 2: PERSONAL DETAILS
            // ==========================================
            Text(
                text = "Personal Details",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF6750A4),
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
            )
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    EditableProfileItem(
                        icon = Icons.Default.Badge,
                        label = "Full Name",
                        value = fullName,
                        onSave = { fullName = it; true }
                    )
                    HorizontalDivider(color = Color(0xFFF0F0F0))

                    // 🔥 NEW: Interactive Date Picker for DOB
                    DatePickerProfileItem(
                        icon = Icons.Default.CalendarToday,
                        label = "Date of Birth",
                        value = dob,
                        onDateSelected = { dob = it }
                    )
                    HorizontalDivider(color = Color(0xFFF0F0F0))

                    EditableProfileItem(
                        icon = Icons.Default.Phone,
                        label = "Phone Number",
                        value = phone,
                        onSave = { phone = it; true }
                    )
                    HorizontalDivider(color = Color(0xFFF0F0F0))

                    EditableDropdownItem(
                        icon = Icons.Default.Wc,
                        label = "Gender",
                        value = gender,
                        options = listOf("Male", "Female", "Other", "Prefer not to say"),
                        onSave = { gender = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ==========================================
            // SECTION 3: EDUCATION
            // ==========================================
            Text(
                text = "Education",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF6750A4),
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
            )
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    EditableProfileItem(
                        icon = Icons.Default.School,
                        label = "School/College Name",
                        value = school,
                        onSave = { school = it; true }
                    )
                    HorizontalDivider(color = Color(0xFFF0F0F0))

                    EditableProfileItem(
                        icon = Icons.Default.MenuBook,
                        label = "Current Class / Degree",
                        value = degree,
                        onSave = { degree = it; true }
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

/* --------------------------------------------------------------------------
   CUSTOM COMPONENT: Text Field with Leading Icon
-------------------------------------------------------------------------- */
@Composable
fun EditableProfileItem(
    icon: ImageVector,
    label: String,
    value: String,
    errorMessage: String? = null,
    onSave: (String) -> Boolean
) {
    var isEditing by remember { mutableStateOf(false) }
    var tempValue by remember { mutableStateOf(value) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = if (isEditing) Alignment.Top else Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp).padding(top = if(isEditing) 12.dp else 0.dp))
        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, fontSize = 12.sp, color = Color.Gray)

            if (isEditing) {
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = tempValue,
                    onValueChange = { tempValue = it },
                    modifier = Modifier.fillMaxWidth(),
                    isError = errorMessage != null,
                    shape = RoundedCornerShape(8.dp),
                    trailingIcon = {
                        Row {
                            IconButton(onClick = {
                                val success = onSave(tempValue)
                                if (success) isEditing = false
                            }) { Icon(Icons.Default.Check, contentDescription = "Save", tint = Color(0xFF4CAF50)) }
                            IconButton(onClick = {
                                isEditing = false
                                tempValue = value
                            }) { Icon(Icons.Default.Close, contentDescription = "Cancel", tint = Color(0xFFF44336)) }
                        }
                    }
                )
                if (errorMessage != null) {
                    Text(text = errorMessage, color = Color.Red, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp, start = 4.dp))
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            isEditing = true
                            tempValue = value
                        }
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = value.ifBlank { "Not set" }, fontSize = 16.sp, color = if (value.isBlank()) Color.LightGray else Color.Black)
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.Gray, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

/* --------------------------------------------------------------------------
   CUSTOM COMPONENT: Material 3 Date Picker Dialog
-------------------------------------------------------------------------- */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerProfileItem(
    icon: ImageVector,
    label: String,
    value: String,
    onDateSelected: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        val selectedDate = formatter.format(Date(millis))
                        onDateSelected(selectedDate)
                    }
                    showDialog = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, fontSize = 12.sp, color = Color.Gray)
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = value.ifBlank { "Not set" }, fontSize = 16.sp, color = if (value.isBlank()) Color.LightGray else Color.Black)
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.Gray, modifier = Modifier.size(16.dp))
            }
        }
    }
}

/* --------------------------------------------------------------------------
   CUSTOM COMPONENT: Dropdown Editor
-------------------------------------------------------------------------- */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditableDropdownItem(icon: ImageVector, label: String, value: String, options: List<String>, onSave: (String) -> Unit) {
    var isEditing by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        verticalAlignment = if (isEditing) Alignment.Top else Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp).padding(top = if(isEditing) 12.dp else 0.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, fontSize = 12.sp, color = Color.Gray)
            if (isEditing) {
                Spacer(modifier = Modifier.height(4.dp))
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    OutlinedTextField(
                        value = value.ifBlank { "Select Option" }, onValueChange = {}, readOnly = true,
                        modifier = Modifier.fillMaxWidth().menuAnchor(), shape = RoundedCornerShape(8.dp),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        options.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = { onSave(option); expanded = false; isEditing = false }
                            )
                        }
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth().clickable { isEditing = true }.padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = value.ifBlank { "Not set" }, fontSize = 16.sp, color = if (value.isBlank()) Color.LightGray else Color.Black)
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.Gray, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}