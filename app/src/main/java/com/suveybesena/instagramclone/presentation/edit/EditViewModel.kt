package com.suveybesena.instagramclone.presentation.edit


import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.suveybesena.instagramclone.utils.extensions.FirebaseInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(var instance : FirebaseInstance) : ViewModel() {


    private val loadingState = MutableLiveData<Boolean>()
    var _loadingState = loadingState
    private val errorState = MutableLiveData<String>()
    val _errorState = errorState


    fun saveEdit(name: String, surname: String, bio: String, webSite: String, pickedImage: Uri) {

        instance.storage.reference.child("images").child(instance.currentUserId.toString()).delete()
        val storage =
            instance.storage.reference.child("images").child(instance.currentUserId.toString())
        storage.putFile(pickedImage)
        storage.downloadUrl.addOnSuccessListener { uri ->
            val imageUrl = uri.toString()



            instance.firestore.collection("UsersName").document(instance.currentUserId.toString())
                .update(
                    mapOf(
                        "name" to name,
                        "surname" to surname,
                        "bio" to bio,
                        "website" to webSite,
                        "image" to imageUrl

                    )
                ).addOnCompleteListener { loadingState.value = false }
                .addOnFailureListener { error ->
                    loadingState.value = false
                    errorState.value = error.localizedMessage
                }
        }
    }
}

