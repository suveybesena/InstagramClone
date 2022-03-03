package com.suveybesena.instagramclone.presentation.edit


import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class EditViewModel : ViewModel() {
    private var auth = FirebaseAuth.getInstance()
    private var firebaseFirestore = FirebaseFirestore.getInstance()
    private var firebaseStorage = FirebaseStorage.getInstance()

    private val loadingState = MutableLiveData<Boolean>()
    var _loadingState = loadingState
    private val errorState = MutableLiveData<String>()
    val _errorState = errorState


    fun saveEdit(name: String, surname: String, bio: String, webSite: String, pickedImage: Uri) {

        firebaseStorage.reference.child("images").child(auth.currentUser?.uid.toString()).delete()
        val storage =
            firebaseStorage.reference.child("images").child(auth.currentUser?.uid.toString())
        storage.putFile(pickedImage)
        storage.downloadUrl.addOnSuccessListener { uri ->
            val imageUrl = uri.toString()



            firebaseFirestore.collection("UsersName").document(auth.currentUser!!.uid)
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

