package com.suveybesena.instagramclone.presentation.followers


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.suveybesena.instagramclone.model.FollowersModel

class FollowerViewModel : ViewModel() {

    private var auth = FirebaseAuth.getInstance()

    private val followerState = MutableLiveData<List<FollowersModel>>()
    var _followerState = followerState
    private val loadingState = MutableLiveData<Boolean>()
    val _loadingState = loadingState
    private val errorState = MutableLiveData<String>()
    val _errorState = errorState
    val currentUserId = auth.currentUser?.uid

    val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var tempList = ArrayList<FollowersModel>()
    var name = ""
    var image = ""

    fun getFollowersId() {
        auth = FirebaseAuth.getInstance()

        if (currentUserId != null) {
            firestore.collection("UsersName").document(currentUserId)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        errorState.value = error.localizedMessage
                    }
                    val doc = value?.get("followers") as List<String>?
                    if (doc != null) {
                        getFollowersProfiles(doc)
                    } else {
                        errorState.value = error?.localizedMessage
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
                        tempList.add(FollowersModel(name, image))
                        followerState.postValue(tempList)
                        loadingState.value = false
                    }
                }

        }

    }


}

