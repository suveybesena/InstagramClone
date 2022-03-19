package com.suveybesena.instagramclone.presentation.following

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.suveybesena.instagramclone.model.FollowModel
import com.suveybesena.instagramclone.di.FirebaseModule
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FollowingViewModel
    @Inject constructor(
        var firebaseInstance : FirebaseFirestore,
        var authInstance : FirebaseAuth
    )
    : ViewModel() {


    private val userList = MutableLiveData<List<FollowModel>>()
    val _userList = userList
    private val errorState = MutableLiveData<String>()
    val _errorState = errorState
    private val loadingState = MutableLiveData<Boolean>()
    val _loadingState = loadingState


    var tempList = ArrayList<FollowModel>()
    var name = ""
    var image = ""

    fun getFollowersId() {
        var currentUserId = authInstance.currentUser?.uid

        if (currentUserId!= null) {
            firebaseInstance.collection("UsersName").document(currentUserId.toString())
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
            firebaseInstance.collection("UsersName").document(followerId)
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