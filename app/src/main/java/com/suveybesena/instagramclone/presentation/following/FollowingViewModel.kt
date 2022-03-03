package com.suveybesena.instagramclone.presentation.following

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.suveybesena.instagramclone.model.FollowModel

class FollowingViewModel : ViewModel() {
    private var auth = FirebaseAuth.getInstance()

    private val userList = MutableLiveData<List<FollowModel>>()
    val _userList = userList
    private val errorState = MutableLiveData<String>()
    val _errorState = errorState
    private val loadingState = MutableLiveData<Boolean>()
    val _loadingState = loadingState


    val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var tempList = ArrayList<FollowModel>()
    var name = ""
    var image = ""

    fun getFollowersId() {
        auth = FirebaseAuth.getInstance()
        var currentUser = auth.currentUser?.uid
        if (currentUser != null) {
            firestore.collection("UsersName").document(currentUser)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        errorState.value = error.localizedMessage
                    }
                    val doc = value?.get("following") as List<String>?
                    if (doc != null) {
                        if (doc.isNotEmpty()) {
                            getFollowersProfiles(doc)
                        }
                    }
                }
        }

    }

    private fun getFollowersProfiles(followerIdList: List<String>) {
        loadingState.value = true
        tempList.clear()
        followerIdList.forEach { followerId ->
            firestore.collection("UsersName").document(followerId)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        loadingState.value = false
                        errorState.value = error.localizedMessage
                    }
                    if (value != null) {
                        val image = value.getString("image")
                        val name = value.getString("name")
                        tempList.add(FollowModel(name, image))
                        userList.postValue(tempList)
                        loadingState.value = false
                    }
                }

        }

    }
}