package com.suveybesena.instagramclone.presentation.register


import android.net.Uri
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.suveybesena.instagramclone.R
import com.suveybesena.instagramclone.utils.extensions.FirebaseInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject
    constructor(var instance : FirebaseInstance)
    : ViewModel() {

    private val loadingState = MutableLiveData<Boolean>()
    val _loadingState = loadingState
    private val errorState = MutableLiveData<String>()
    val _errorState = errorState
    private val authState = MutableLiveData<Boolean>()
    val _authState = authState






    fun register(
        mail: String,
        password: String,
        pickedImage: Uri,
        view: View,
        username: String,
        name: String
    ) {

        loadingState.value = true
        val user = hashMapOf<String, Any>()
        user.put("name", username)
        user.put("password", password)
        user.put("surname", name)
        user.put("bio", "")
        user.put("website", "www.test.com")
        user.put("image", pickedImage)

        instance.auth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                instance.currentUser?.let { user.put("uid", it.uid) }
                uploadPhoto(pickedImage, username)
                view.let { view ->
                    authState.value = true
                }
            }
        }.addOnFailureListener { exception ->
            loadingState.value = false
            errorState.value = exception.localizedMessage
        }

    }

    fun uploadPhoto(pickedImage: Uri, username: String) {


        val reference = instance.storage.reference
        val imageReference = reference.child("images").child(instance.currentUserId.toString())

        imageReference.putFile(pickedImage).addOnSuccessListener { taskSnapshot ->

            val uploadedImageReference =
                FirebaseStorage.getInstance().reference.child("images").child(instance.currentUserId.toString())
            uploadedImageReference.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                val postHashmap = hashMapOf<String, Any>()
                postHashmap.put("image", imageUrl)
                postHashmap.put("name", username)
                postHashmap.put("uid", instance.currentUserId.toString())

                instance.firestore.collection("UsersName").document(instance.currentUserId.toString()).set(postHashmap)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            loadingState.value = false
                        }
                    }.addOnFailureListener { exception ->
                        errorState.value = exception.localizedMessage

                    }
            }

        }.addOnFailureListener { exception ->
            errorState.value = exception.localizedMessage
        }

    }


}