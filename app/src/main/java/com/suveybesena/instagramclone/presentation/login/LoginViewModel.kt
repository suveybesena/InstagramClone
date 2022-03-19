package com.suveybesena.instagramclone.presentation.login


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.suveybesena.instagramclone.di.FirebaseModule
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject
constructor(
    var authInstance: FirebaseAuth
) : ViewModel() {

    private val loadingState = MutableLiveData<Boolean>()
    val _loadingState = loadingState
    private val errorState = MutableLiveData<String>()
    val _errorState = errorState


    fun login(email: String, password: String) {
        loadingState.value = true
        if (validate(email, password)) {
            authInstance.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    loadingState.value = false
                }
                .addOnFailureListener { error ->
                    loadingState.value = false
                    errorState.value = error.localizedMessage
                }
        }
    }

    private fun validate(email: String, password: String): Boolean {
        email.isNotEmpty() && password.isNotEmpty()
        return true
    }

}




