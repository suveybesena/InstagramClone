package com.suveybesena.instagramclone.presentation.followers


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.suveybesena.instagramclone.model.FollowersModel
import com.suveybesena.instagramclone.di.FirebaseModule
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class FollowerViewModel @Inject constructor(
    var firebaseInstance : FirebaseFirestore,
    var authInstance : FirebaseAuth
): ViewModel() {

    private val followerState = MutableLiveData<List<FollowersModel>>()
    var _followerState = followerState
    private val loadingState = MutableLiveData<Boolean>()
    val _loadingState = loadingState
    private val errorState = MutableLiveData<String>()
    val _errorState = errorState



    var tempList = ArrayList<FollowersModel>()
    var name = ""
    var image = ""

    fun getFollowersId() {

        val currentUserId = authInstance.currentUser?.uid
        if (currentUserId != null) {
            firebaseInstance.collection("UsersName").document(currentUserId.toString())
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
            firebaseInstance.collection("UsersName").document(followerId)
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

