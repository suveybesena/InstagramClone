package com.suveybesena.instagramclone.presentation.login


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth


class LoginViewModel : ViewModel() {

   private val loadingState = MutableLiveData<Boolean>()
    val _loadingState = loadingState
    private val errorState = MutableLiveData<String>()
    val _errorState = errorState

    private val auth = FirebaseAuth.getInstance()

    fun login(email : String, password: String) {
        loadingState.value= true
        if (validate(email, password)) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    loadingState.value=false
                }
                .addOnFailureListener { error->
                    loadingState.value= false
                    errorState.value = error.localizedMessage
                }
        }
    }
        private fun validate(email: String, password: String): Boolean {
            email.isNotEmpty() && password.isNotEmpty()
            return true
        }

    }




