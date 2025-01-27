package com.example.budgettracker.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.budgettracker.R
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.material3.*

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToHome: () -> Unit // Callback to navigate to the home screen
) {
    val auth = FirebaseAuth.getInstance()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current // Required for displaying Toast messages
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    // Check if the user is already logged in and navigate to the home screen if they are
    LaunchedEffect(auth.currentUser) {
        if (auth.currentUser != null) {
            onNavigateToHome()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome back!",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
//            colors = TextFieldDefaults.outlinedTextFieldColors(
//                containerColor = Color.White,
//                focusedBorderColor = MaterialTheme.colorScheme.primary,
//                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
//                focusedLabelColor = MaterialTheme.colorScheme.primary,
//                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
//                focusedTextColor = MaterialTheme.colorScheme.onSurface,
//                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
//                cursorColor = MaterialTheme.colorScheme.primary
//            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//            colors = TextFieldDefaults.outlinedTextFieldColors(
//                containerColor = Color.White,
//                focusedBorderColor = MaterialTheme.colorScheme.primary,
//                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
//                focusedLabelColor = MaterialTheme.colorScheme.primary,
//                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
//                focusedTextColor = MaterialTheme.colorScheme.onSurface,
//                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
//                cursorColor = MaterialTheme.colorScheme.primary
//            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text("Forgot Password?")

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val editor = sharedPreferences.edit()
                            val userName =
                                sharedPreferences.getString("username", "Guest") ?: "Guest"
                            editor.putString("username", userName).apply()

                            // Successful login
                            Toast.makeText(context, "Login successfully!", Toast.LENGTH_SHORT)
                                .show()
                            onNavigateToHome() // Navigate to the home screen
                        } else {
                            // Login failed
                            Toast.makeText(
                                context,
                                "Email or password is incorrect!",
                                Toast.LENGTH_SHORT
                            ).show()
                            errorMessage = task.exception?.message ?: "Login failed"
                        }
                    }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Or sign in with")
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = {
                Toast.makeText(context, "This function will add later.", Toast.LENGTH_SHORT)
                    .show()
            }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google), // Replace with your Google icon resource
                    contentDescription = "Google sign-in",
                    modifier = Modifier.size(60.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = {
                Toast.makeText(context, "This function will add later.", Toast.LENGTH_SHORT)
                    .show()
            }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_facebook), // Replace with your Facebook icon resource
                    contentDescription = "Facebook sign-in",
                    modifier = Modifier.size(60.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Haven't an account?")
            Spacer(modifier = Modifier.width(4.dp))
            TextButton(onClick = onNavigateToRegister) {
                Text("Sign up here!", color = MaterialTheme.colorScheme.primary)
            }
        }
        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
        }
    }
}