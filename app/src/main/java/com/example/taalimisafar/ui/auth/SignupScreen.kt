package com.example.taalimisafar.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taalimisafar.viewmodel.AuthViewModel

@Composable
fun SignupScreen(
    viewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }

    // Clear state when the screen is first opened
    LaunchedEffect(Unit) {
        viewModel.resetSignupState()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (!viewModel.isOtpSent.value) {
            // --- STEP 1: CREATE ACCOUNT ---
            Text("Create Account", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(32.dp))

            OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(24.dp))

            if (viewModel.isLoading.value) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { viewModel.signup(username, email, password) },
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) { Text("Sign Up") }
            }

            Spacer(Modifier.height(16.dp))
            TextButton(onClick = onNavigateToLogin) {
                Text("Already have an account? Login")
            }
        } else {
            // --- STEP 2: ENTER OTP ---
            Text("Enter OTP", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text(
                "Check your email ($email) for the 6-digit code.",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
            Spacer(Modifier.height(32.dp))

            OutlinedTextField(
                value = otp,
                onValueChange = { if (it.length <= 6) otp = it },
                label = { Text("6-Digit OTP") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            if (viewModel.isLoading.value) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { viewModel.verifyOtp(email, otp) },
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) { Text("Verify & Login") }
            }

            Spacer(Modifier.height(16.dp))

            // --- RESEND OTP OPTION ---
            TextButton(onClick = { viewModel.resendOtp(email) }) {
                Text("Didn't receive code? Resend OTP", color = MaterialTheme.colorScheme.primary)
            }

            // --- GO BACK TO EDIT FORM ---
            TextButton(onClick = {
                viewModel.resetSignupState() // This fixes the loop!
            }) {
                Text("Go Back & Edit Details", color = Color.Gray)
            }
        }

        // Error Messaging
        viewModel.errorMessage.value?.let {
            Spacer(Modifier.height(16.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }
    }
}