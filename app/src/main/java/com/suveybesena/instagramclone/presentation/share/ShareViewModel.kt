package com.suveybesena.instagramclone.presentation.share

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ShareViewModel : ViewModel() {
    var auth = FirebaseAuth.getInstance()
    var firebase = FirebaseFirestore.getInstance()
    var storage = FirebaseStorage.getInstance()

    private val loadingState = MutableLiveData<Boolean>()
    val _loadingState = loadingState
    private val errorState = MutableLiveData<String>()
    val _errorState = errorState

    fun imageDownloader(pickedImage: Uri, comment: String, location: String) {
        loadingState.value = true
        val currentUserUid = auth.currentUser?.uid.toString()
        val reference = storage.reference
        val imageReference = reference.child("feedImages").child(currentUserUid)

        imageReference.putFile(pickedImage).addOnSuccessListener { taskSnapshot ->

            val uploadedImageReference =
                FirebaseStorage.getInstance().reference.child("feedImages").child(currentUserUid)
            uploadedImageReference.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                val currentUserMail = auth.currentUser!!.email.toString()
                val date = com.google.firebase.Timestamp.now()

                val postHashmap = hashMapOf<String, Any>()
                postHashmap.put("downloadUri", imageUrl)
                postHashmap.put("currentUserMail", currentUserMail)
                postHashmap.put("comment", comment)
                postHashmap.put("date", date)
                postHashmap.put("uid", auth.currentUser?.uid.toString())
                postHashmap.put("location", location)

                firebase.collection("feedImages").add(postHashmap).addOnCompleteListener { task ->
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