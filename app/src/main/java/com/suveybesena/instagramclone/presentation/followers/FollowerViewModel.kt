package com.suveybesena.instagramclone.presentation.followers


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.suveybesena.instagramclone.model.FollowersModel
import com.suveybesena.instagramclone.utils.extensions.FirebaseInstance
import com.suveybesena.instagramclone.utils.extensions.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class FollowerViewModel @Inject constructor(
    var instance: FirebaseInstance
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


        if (instance.currentUserId != null) {
            instance.firestore.collection("UsersName").document(instance.currentUserId.toString())
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
            instance.firestore.collection("UsersName").document(followerId)
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

