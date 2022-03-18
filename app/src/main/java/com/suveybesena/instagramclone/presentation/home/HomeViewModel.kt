package com.suveybesena.instagramclone.presentation.home


import android.content.ContentValues
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.suveybesena.instagramclone.model.Firebase
import com.suveybesena.instagramclone.utils.extensions.FirebaseInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject
    constructor(
    var instance : FirebaseInstance
    ): ViewModel() {


    private val firebaseList = MutableLiveData<List<Firebase>>()
    val _firebaseList = firebaseList
    private val errorState = MutableLiveData<String?>()
    val _errorState = errorState
    private val loadingState = MutableLiveData<Boolean>()
    var _loadingState = loadingState
    private var likeState = MutableLiveData<Boolean>()
    var _likeState = likeState


    private var allInOne = HashMap<String, String>()
    private var userImage = ""
    val tempUserList = ArrayList<Firebase>()



    fun readUserData() {

        instance.firestore.collection("UsersName").document(instance.currentUserId.toString())
            .addSnapshotListener { value, error ->
                if (error != null) {
                    errorState.value = error.localizedMessage
                }
                val docum = value?.get("following") as List<String>?
                if (docum?.isEmpty() == false) {
                    getFollowerFeed(docum)

                }
            }
    }

    private fun getFollowerFeed(doc: List<String>) {
        doc.forEach { following ->
            loadingState.value = true
            instance.firestore.collection("feedImages").whereEqualTo("uid", following)
                .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        errorState.value = exception.localizedMessage
                        loadingState.value = false
                    } else {
                        if (snapshot != null) {
                            if (!snapshot.isEmpty) {

                                val documentList = snapshot.documents

                                for (document in documentList) {
                                    val user = document.get("currentUserMail") as String
                                    val downloadUri = document.get("downloadUri") as String
                                    val comment = document.get("comment") as String
                                    val location = document.get("location") as String

                                    tempUserList.clear()
                                    instance.firestore.collection("UsersName")
                                        .whereEqualTo("uid", following)
                                        .addSnapshotListener { snap, except ->
                                            if (except != null) {
                                                errorState.value = except.localizedMessage
                                            } else {
                                                if (snap != null) {
                                                    if (!snap.isEmpty) {
                                                        val documentLists = snap.documents
                                                        for (documents in documentLists) {
                                                            userImage =
                                                                documents.get("image") as String

                                                            allInOne.put(
                                                                downloadUri,
                                                                userImage
                                                            )
                                                        }
                                                        val list = Firebase(
                                                            downloadUri,
                                                            comment,
                                                            user,
                                                            userImage,
                                                            location,
                                                            allInOne
                                                        )
                                                        tempUserList.add(list)
                                                        firebaseList.value = tempUserList
                                                        loadingState.value = false

                                                    }
                                                }

                                            }


                                        }
                                }
                            }
                        }


                    }
                }

        }

    }

    fun checkLikeStatus(image: String, like: ImageView, unLike: ImageView) {

       instance.firestore.collection("UsersName").document(instance.currentUserId.toString())
            .addSnapshotListener { value, error ->
                if (error != null) {
                    errorState.value = error.localizedMessage
                }
                val doc = value?.get("likes") as List<String>?
                if (doc?.isEmpty() == true) {
                    like.visibility = View.VISIBLE
                } else {
                    doc?.forEach { list ->
                        if (list == image) {
                            likeState.value = true

                        } else {
                            likeState.value = false

                        }

                    }
                }
            }


    }


    fun getLikesImage(image: String) {
        instance.firestore.collection("UsersName").document(instance.currentUserId.toString()).update(
            "likes", FieldValue.arrayUnion(image)
        )
            .addOnSuccessListener { Log.d(ContentValues.TAG, "success") }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "error", e) }
    }

    fun removeLike (image : String){
        instance.firestore.collection("UsersName").document(instance.currentUserId.toString()).update(
            "likes", FieldValue.arrayRemove(image)
        )

    }


}