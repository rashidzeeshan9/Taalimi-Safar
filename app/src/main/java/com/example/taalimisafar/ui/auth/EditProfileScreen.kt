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
    val profile = viewModel.userProfile.value

    var username by remember(profile) { mutableStateOf(profile?.custom_username ?: "") }
    var email by remember(profile) { mutableStateOf(profile?.email?.takeIf { it.isNotBlank() } ?: "") }
    var fullName by remember(profile) { mutableStateOf(profile?.full_name ?: "") }
    var dob by remember(profile) { mutableStateOf(profile?.date_of_birth ?: "") }
    var phone by remember(profile) { mutableStateOf(profile?.phone_number ?: "") }
    var gender by remember(profile) { mutableStateOf(profile?.gender ?: "") }
    var school by remember(profile) { mutableStateOf(profile?.qualification ?: "") }

    // 🔥 NEW: Class/Course state
    var degree by remember(profile) { mutableStateOf(profile?.degree ?: "") }

    val daysSinceLastChange = profile?.days_since_last_change
    var usernameError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Keep your profile updated to get better recommendations and opportunities.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // ==========================================
            // ACCOUNT INFORMATION
            // ==========================================
            SectionHeader("Account Information")
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    EditableProfileItem(
                        icon = Icons.Default.Person, label = "Username", value = username, errorMessage = usernameError,
                        onSave = { newUsername ->
                            if (newUsername != username) {
                                if (daysSinceLastChange != null && daysSinceLastChange < 60) {
                                    val daysLeft = 60 - daysSinceLastChange
                                    val calendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, daysLeft) }
                                    val exactDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(calendar.time)
                                    usernameError = "Wait $daysLeft more days. Changeable on $exactDate."
                                    false
                                } else {
                                    username = newUsername
                                    usernameError = null
                                    viewModel.updateProfileData(mapOf("custom_username" to newUsername))
                                    Toast.makeText(context, "Username updated!", Toast.LENGTH_SHORT).show()
                                    true
                                }
                            } else { usernameError = null; true }
                        }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))

                    // 🔥 NEW: Advanced Email OTP Item
                    EmailOtpEditableItem(
                        currentEmail = email,
                        viewModel = viewModel,
                        onEmailVerified = { newEmail ->
                            email = newEmail
                            Toast.makeText(context, "Email successfully updated!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ==========================================
            // PERSONAL DETAILS
            // ==========================================
            SectionHeader("Personal Details")
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    EditableProfileItem(icon = Icons.Default.Badge, label = "Full Name", value = fullName, onSave = {
                        fullName = it
                        viewModel.updateProfileData(mapOf("full_name" to it))
                        Toast.makeText(context, "Name saved", Toast.LENGTH_SHORT).show()
                        true
                    })
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))

                    DatePickerProfileItem(icon = Icons.Default.CalendarToday, label = "Date of Birth", value = dob, onDateSelected = {
                        dob = it
                        try {
                            val backendDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(it)!!)
                            viewModel.updateProfileData(mapOf("dob" to backendDate))
                            Toast.makeText(context, "DOB saved", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(context, "Date format error", Toast.LENGTH_SHORT).show()
                        }
                    })
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))

                    EditableProfileItem(icon = Icons.Default.Phone, label = "Phone Number", value = phone, onSave = {
                        phone = it
                        viewModel.updateProfileData(mapOf("phone" to it))
                        Toast.makeText(context, "Phone saved", Toast.LENGTH_SHORT).show()
                        true
                    })
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))

                    EditableDropdownItem(icon = Icons.Default.Wc, label = "Gender", value = gender, options = listOf("Male", "Female", "Other", "Prefer not to say"), onSave = {
                        gender = it
                        viewModel.updateProfileData(mapOf("gender" to it))
                        Toast.makeText(context, "Gender saved", Toast.LENGTH_SHORT).show()
                    })
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ==========================================
            // EDUCATION
            // ==========================================
            SectionHeader("Education")
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    EditableProfileItem(icon = Icons.Default.School, label = "School/College Name", value = school, onSave = {
                        school = it
                        viewModel.updateProfileData(mapOf("qualification" to it))
                        Toast.makeText(context, "School saved", Toast.LENGTH_SHORT).show()
                        true
                    })

                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))

                    // 🔥 NEW: Class/Course Item
                    EditableProfileItem(icon = Icons.Default.MenuBook, label = "Current Class / Course", value = degree, onSave = {
                        degree = it
                        viewModel.updateProfileData(mapOf("degree" to it))
                        Toast.makeText(context, "Class/Course saved", Toast.LENGTH_SHORT).show()
                        true
                    })
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
    )
}

// ==========================================
// EMAIL OTP COMPONENT
// ==========================================
@Composable
fun EmailOtpEditableItem(
    currentEmail: String,
    viewModel: AuthViewModel,
    onEmailVerified: (String) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var tempEmail by remember { mutableStateOf("") }
    var otpCode by remember { mutableStateOf("") }
    var step by remember { mutableStateOf(1) } // 1: Enter Email, 2: Enter OTP
    val context = LocalContext.current

    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = if (isEditing) Alignment.Top else Alignment.CenterVertically) {
        Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(24.dp).padding(top = if(isEditing) 12.dp else 0.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "Email Address", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

            if (isEditing) {
                Spacer(modifier = Modifier.height(8.dp))
                if (step == 1) {
                    OutlinedTextField(
                        value = tempEmail, onValueChange = { tempEmail = it }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), placeholder = { Text("New Email Address") },
                        trailingIcon = {
                            IconButton(onClick = { isEditing = false }) { Icon(Icons.Default.Close, contentDescription = "Cancel", tint = MaterialTheme.colorScheme.error) }
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            if (tempEmail.isNotBlank() && tempEmail.contains("@")) {
                                viewModel.requestEmailChangeOtp(tempEmail) // Triggers ViewModel
                                step = 2
                                Toast.makeText(context, "OTP Sent to $tempEmail", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Enter a valid email", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Send OTP") }
                } else if (step == 2) {
                    OutlinedTextField(
                        value = otpCode, onValueChange = { otpCode = it }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), placeholder = { Text("Enter 6-digit OTP") },
                        trailingIcon = { IconButton(onClick = { isEditing = false; step = 1 }) { Icon(Icons.Default.Close, contentDescription = "Cancel", tint = MaterialTheme.colorScheme.error) } }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            // Verify OTP via ViewModel. On success, update UI.
                            viewModel.verifyEmailChangeOtp(tempEmail, otpCode) { success ->
                                if (success) {
                                    onEmailVerified(tempEmail)
                                    isEditing = false
                                    step = 1
                                } else {
                                    Toast.makeText(context, "Invalid OTP", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Verify & Save") }
                }
            } else {
                Row(modifier = Modifier.fillMaxWidth().clickable { isEditing = true; tempEmail = currentEmail; step = 1 }.padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = currentEmail.ifBlank { "Fetching..." }, style = MaterialTheme.typography.bodyLarge, color = if (currentEmail.isBlank()) Color.LightGray else MaterialTheme.colorScheme.onSurface)
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

// ==========================================
// STANDARD COMPONENTS (Polished)
// ==========================================
@Composable
fun EditableProfileItem(icon: ImageVector, label: String, value: String, errorMessage: String? = null, onSave: (String) -> Boolean) {
    var isEditing by remember { mutableStateOf(false) }
    var tempValue by remember { mutableStateOf(value) }

    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = if (isEditing) Alignment.Top else Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(24.dp).padding(top = if(isEditing) 12.dp else 0.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (isEditing) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = tempValue, onValueChange = { tempValue = it }, modifier = Modifier.fillMaxWidth(), isError = errorMessage != null, shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        Row {
                            IconButton(onClick = { if (onSave(tempValue)) isEditing = false }) { Icon(Icons.Default.Check, contentDescription = "Save", tint = Color(0xFF4CAF50)) }
                            IconButton(onClick = { isEditing = false; tempValue = value }) { Icon(Icons.Default.Close, contentDescription = "Cancel", tint = MaterialTheme.colorScheme.error) }
                        }
                    }
                )
                if (errorMessage != null) Text(text = errorMessage, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 4.dp, start = 4.dp))
            } else {
                Row(modifier = Modifier.fillMaxWidth().clickable { isEditing = true; tempValue = value }.padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = value.ifBlank { "Not set" }, style = MaterialTheme.typography.bodyLarge, color = if (value.isBlank()) Color.LightGray else MaterialTheme.colorScheme.onSurface)
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerProfileItem(icon: ImageVector, label: String, value: String, onDateSelected: (String) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = { TextButton(onClick = { datePickerState.selectedDateMillis?.let { millis -> onDateSelected(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(millis))) }; showDialog = false }) { Text("OK") } },
            dismissButton = { TextButton(onClick = { showDialog = false }) { Text("Cancel") } }
        ) { DatePicker(state = datePickerState) }
    }

    Row(modifier = Modifier.fillMaxWidth().clickable { showDialog = true }.padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = value.ifBlank { "Not set" }, style = MaterialTheme.typography.bodyLarge, color = if (value.isBlank()) Color.LightGray else MaterialTheme.colorScheme.onSurface)
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditableDropdownItem(icon: ImageVector, label: String, value: String, options: List<String>, onSave: (String) -> Unit) {
    var isEditing by remember { mutableStateOf(false) }

    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = if (isEditing) Alignment.Top else Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(24.dp).padding(top = if(isEditing) 12.dp else 0.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (isEditing) {
                Spacer(modifier = Modifier.height(8.dp))
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    OutlinedTextField(
                        value = value.ifBlank { "Select Option" }, onValueChange = {}, readOnly = true, modifier = Modifier.fillMaxWidth().menuAnchor(), shape = RoundedCornerShape(12.dp),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        options.forEach { option -> DropdownMenuItem(text = { Text(option) }, onClick = { onSave(option); expanded = false; isEditing = false }) }
                    }
                }
            } else {
                Row(modifier = Modifier.fillMaxWidth().clickable { isEditing = true }.padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = value.ifBlank { "Not set" }, style = MaterialTheme.typography.bodyLarge, color = if (value.isBlank()) Color.LightGray else MaterialTheme.colorScheme.onSurface)
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}