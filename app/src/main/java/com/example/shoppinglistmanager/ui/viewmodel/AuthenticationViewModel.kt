package com.example.shoppinglistmanager.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.example.shoppinglistmanager.ui.utils.showToast
import com.google.firebase.auth.FirebaseAuth

class AuthenticationViewModel(
    application: Application
) : AndroidViewModel(application) {

    private var auth: FirebaseAuth

    init {
        auth = FirebaseAuth.getInstance()
    }

    fun login(
        login: String,
        password: String,
        onSuccess: () -> Unit,
        context: Context
    ) {
        if (login.isBlank()) {
            showToast(context, "Login cannot be empty.")
            return
        }
        if (password.isBlank()) {
            showToast(context, "Password cannot be empty.")
            return
        }
        try {
            auth.signInWithEmailAndPassword(
                login,
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

}