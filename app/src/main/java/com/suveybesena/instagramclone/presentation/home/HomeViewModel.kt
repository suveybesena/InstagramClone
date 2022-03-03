package com.suveybesena.instagramclone.presentation.home


import android.view.View
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.suveybesena.instagramclone.model.Firebase


class HomeViewModel : ViewModel() {


    private var firebase = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

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
    var currentUserId = auth.currentUser?.uid


    fun readUserData() {

        firebase.collection("UsersName").document(currentUserId.toString())
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
            firebase.collection("feedImages").whereEqualTo("uid", following)
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
                                    firebase.collection("UsersName")
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

        FirebaseFirestore.getInstance().collection("UsersName").document(currentUserId.toString())
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


}