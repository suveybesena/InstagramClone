package com.suveybesena.instagramclone.presentation.likes

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.suveybesena.instagramclone.model.LikesModel
import kotlin.collections.ArrayList

class LikesViewModel : ViewModel() {
    private  var auth= FirebaseAuth.getInstance()
    private  var firestore=  FirebaseFirestore.getInstance()
    private var likesList = MutableLiveData<List<LikesModel>?>()
    var _likesList = likesList
    private var errorState = MutableLiveData<String?>()
    var _errorState = errorState
    private var loadingState = MutableLiveData<Boolean>()
    var _loadingState = loadingState


    val currentUser = auth.currentUser
    val uid = currentUser?.uid

    var tempList = ArrayList<LikesModel>()


    fun getLikesImage(image: String) {
        firestore.collection("UsersName").document(uid.toString()).update(
            "likes", FieldValue.arrayUnion(image)
        )
            .addOnSuccessListener { Log.d(ContentValues.TAG, "success") }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "error", e) }
    }

    fun loadImage() {
        loadingState.value = true
        auth = FirebaseAuth.getInstance()
        tempList.clear()
        firestore.collection("UsersName").document(uid.toString())
            .addSnapshotListener { value, exception ->
                if (exception != null) {
                    likesList.value = null
                    loadingState.value = false
                    errorState.value = exception.localizedMessage
                } else {
                    errorState.value = null
                                val image = value?.get("likes") as List<String>?
                                image?.forEach { images->
                                    val list = LikesModel(images)
                                    tempList.add(list)
                                }
                            likesList.value = tempList
                            loadingState.value = false
                        }
                    }


            }


    fun removeLike (image : String){
        firestore.collection("UsersName").document(uid.toString()).update(
            "likes", FieldValue.arrayRemove(image)
        )

    }

}
