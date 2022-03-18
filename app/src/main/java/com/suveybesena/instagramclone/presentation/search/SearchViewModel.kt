package com.suveybesena.instagramclone.presentation.search

import android.content.ContentValues
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.suveybesena.instagramclone.model.User
import com.suveybesena.instagramclone.utils.extensions.FirebaseInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject
    constructor( var instance : FirebaseInstance)
    : ViewModel() {

    private val userList = MutableLiveData<ArrayList<User>?>()
    val _userList = userList
    private val errorState = MutableLiveData<String?>()
    val _errorState = errorState
    private val loadingState = MutableLiveData<Boolean>()
    val _loadingState = loadingState
    private val auth = FirebaseAuth.getInstance()
    private val followState = MutableLiveData<Boolean>()
    val _followState = followState


    fun retrieveUsers(searchText: String) {
        loadingState.value = true
        val tempUserList = ArrayList<User>()

        instance.firestore.collection("UsersName")
            .orderBy("name")
            .startAt(searchText)
            .endAt(searchText + "\uf8ff")
            .addSnapshotListener { snapshot, exception ->
                tempUserList.clear()
                if (snapshot != null) {
                    if (!snapshot.isEmpty) {
                        errorState.value = null
                        val documentList = snapshot.documents
                        for (document in documentList) {
                            val username = document.get("name") as String?
                            val image = document.get("image") as String?
                            val uid = document.get("uid") as String?
                            val user = User(username, image, uid)
                            tempUserList.add(user)
                            userList.value = tempUserList
                            loadingState.value = false
                        }
                    }
                }

                exception?.let { errorMessage ->
                    userList.value = null
                    loadingState.value = false
                    errorState.value = errorMessage.localizedMessage
                }
            }
    }

    fun follow(userId: String) {
        val currentUser = auth.currentUser!!.uid
        currentUser.let { uid ->
            FirebaseFirestore.getInstance()
                .collection("UsersName").document(currentUser).update(
                    "following", FieldValue.arrayUnion(
                        userId
                    )
                )
                .addOnSuccessListener { Log.d(ContentValues.TAG, "success") }
                .addOnFailureListener { e -> Log.w(ContentValues.TAG, "error", e) }
        }
        FirebaseFirestore.getInstance()
            .collection("UsersName").document(userId).update(
                "followers", FieldValue.arrayUnion(
                    currentUser
                )
            )
            .addOnSuccessListener { Log.d(ContentValues.TAG, "success") }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "error", e) }


    }

    fun unFollow(userId: String) {

        instance.currentUserId.let { uid ->
            if (instance.currentUserId != null) {
                FirebaseFirestore.getInstance()
                    .collection("UsersName").document(instance.currentUserId.toString())
                    .update("following", FieldValue.arrayRemove(userId))
                    .addOnSuccessListener { Log.d(ContentValues.TAG, "success") }
                    .addOnFailureListener { e -> Log.w(ContentValues.TAG, "error", e) }
            }

        }

        instance.currentUserId.let { uid ->
            if (instance.currentUserId != null) {
                FirebaseFirestore.getInstance()
                    .collection("UsersName").document(userId)
                    .update("followers", FieldValue.arrayRemove(instance.currentUserId))
                    .addOnSuccessListener { Log.d(ContentValues.TAG, "success") }
                    .addOnFailureListener { e -> Log.w(ContentValues.TAG, "error", e) }
            }

        }
    }

    fun checkFollowingStatus(userId: String, follow: Button, unfollow: Button) {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser?.uid
        if (currentUser != null) {
            FirebaseFirestore.getInstance().collection("UsersName").document(currentUser)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        errorState.value = error.localizedMessage
                    }
                    val doc = value?.get("following") as List<String>?
                    if (doc != null) {
                        if (doc.isEmpty()) {
                            follow.visibility = View.VISIBLE
                        } else {
                            doc.forEach { list ->
                                if (list == userId) {
                                    followState.value = true
                                } else {
                                    followState.value = false

                                }

                            }
                        }
                    }
                }
        }

    }

}