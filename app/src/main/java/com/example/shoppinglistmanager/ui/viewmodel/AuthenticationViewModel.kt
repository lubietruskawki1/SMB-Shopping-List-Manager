package com.example.shoppinglistmanager.ui.viewmodel

import android.app.Application
import android.content.Context
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import com.example.shoppinglistmanager.ui.utils.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class AuthenticationViewModel(
    application: Application
) : AndroidViewModel(application) {

    private var auth: FirebaseAuth

    init {
        auth = FirebaseAuth.getInstance()
    }

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        context: Context
    ) {
        if (email.isBlank()) {
            showToast(context, "Email cannot be empty.")
            return
        }
        if (password.isBlank()) {
            showToast(context, "Password cannot be empty.")
            return
        }

        try {
            auth.signInWithEmailAndPassword(
                email,
                password
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess.invoke()
                } else {
                    showToast(context,
                        "Login unsuccessful. Please try again.")
                }
            }
        } catch (e: Exception) {
            showToast(context, e.message)
        }
    }

    fun register(
        email: String,
        password: String,
        confirmPassword: String,
        onSuccess: () -> Unit,
        context: Context
    ) {
        if (!isValidEmail(email, context) ||
            !isValidPassword(password, confirmPassword, context)) {
            return
        }

        try {
            auth.createUserWithEmailAndPassword(
                email,
                password
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess.invoke()
                } else {
                    try {
                        throw it.exception!!
                    } catch (e: FirebaseAuthUserCollisionException) {
                        showToast(context, "Email already in use.")
                    }
                    showToast(context,
                        "Registration unsuccessful. Please try again.")
                }
            }
        } catch (e: Exception) {
            showToast(context, e.message)
        }
    }

    private fun isValidEmail(email: String, context: Context): Boolean {
        if (email.isBlank()) {
            showToast(context, "Email cannot be empty.")
            return false
        }
        // Check if the email entered matches a normal email address pattern
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            showToast(context, "Enter a valid email address.")
            return false
        }
        return true
    }

    private fun isValidPassword(
        password: String,
        confirmPassword: String,
        context: Context
    ): Boolean {
        if (password.isBlank()) {
            showToast(context, "Password cannot be empty.")
            return false
        }
        if (password.length < 6){
            showToast(context,
                "Password must be at least 6 characters.")
            return false
        }
        if (password != confirmPassword) {
            showToast(context, "Passwords do not match.")
            return false
        }
        return true
    }

}