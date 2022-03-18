package com.suveybesena.instagramclone.presentation.share

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
class ShareViewModel @Inject
    constructor(var instance : FirebaseInstance)
    : ViewModel() {


    private val loadingState = MutableLiveData<Boolean>()
    val _loadingState = loadingState
    private val errorState = MutableLiveData<String>()
    val _errorState = errorState

    fun imageDownloader(pickedImage: Uri, comment: String, location: String) {
        loadingState.value = true


        val imageReference = instance.storage.reference.child("feedImages").child(instance.currentUserId.toString())

        imageReference.putFile(pickedImage).addOnSuccessListener { taskSnapshot ->

            val uploadedImageReference =
                FirebaseStorage.getInstance().reference.child("feedImages").child(instance.currentUserId.toString())
            uploadedImageReference.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                val currentUserMail = instance.currentUser?.email.toString()
                val date = com.google.firebase.Timestamp.now()

                val postHashmap = hashMapOf<String, Any>()
                postHashmap.put("downloadUri", imageUrl)
                postHashmap.put("currentUserMail", currentUserMail)
                postHashmap.put("comment", comment)
                postHashmap.put("date", date)
                postHashmap.put("uid", instance.currentUserId.toString())
                postHashmap.put("location", location)

                instance.firestore.collection("feedImages").add(postHashmap).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        loadingState.value = false
                    }
                }.addOnFailureListener { exception ->
                    loadingState.value = false
                    errorState.value = exception.localizedMessage
                }

            }.addOnFailureListener { exception ->
                loadingState.value = false
                errorState.value = exception.localizedMessage
            }
        }
    }


}