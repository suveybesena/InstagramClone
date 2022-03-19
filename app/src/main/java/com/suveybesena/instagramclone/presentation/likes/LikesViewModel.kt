package com.suveybesena.instagramclone.presentation.likes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.suveybesena.instagramclone.model.LikesModel
import com.suveybesena.instagramclone.di.FirebaseModule
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.collections.ArrayList
@HiltViewModel
class LikesViewModel @Inject
    constructor(var firebaseInstance : FirebaseFirestore,
                var authInstance : FirebaseAuth
): ViewModel() {

    private var likesList = MutableLiveData<List<LikesModel>?>()
    var _likesList = likesList
    private var errorState = MutableLiveData<String?>()
    var _errorState = errorState
    private var loadingState = MutableLiveData<Boolean>()
    var _loadingState = loadingState



    var tempList = ArrayList<LikesModel>()


    fun loadImage() {
        loadingState.value = true

        tempList.clear()
        firebaseInstance.collection("UsersName").document(authInstance.currentUser?.uid.toString())
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




}
