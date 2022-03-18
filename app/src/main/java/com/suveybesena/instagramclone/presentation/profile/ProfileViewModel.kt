package com.suveybesena.instagramclone.presentation.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.suveybesena.instagramclone.model.MyPostsModel
import com.suveybesena.instagramclone.utils.extensions.FirebaseInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel  @Inject
    constructor( var instance : FirebaseInstance)
    : ViewModel() {


    private val nameState = MutableLiveData<String>()
    val _nameState = nameState
    private val imageState = MutableLiveData<String>()
    val _imageState = imageState
    private val loadingState = MutableLiveData<Boolean>()
    val _loadingState = loadingState
    private val errorState = MutableLiveData<String>()
    val _errorState = errorState

    var tempList = ArrayList<MyPostsModel>()
    private val myPostList = MutableLiveData<List<MyPostsModel>>()
    val _myPostList = myPostList


    fun getDataFromFirebase() {
        loadingState.value = true

        instance.firestore.collection("UsersName").whereEqualTo("uid", instance.currentUserId.toString())
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    loadingState.value = false
                    errorState.value = exception.localizedMessage
                } else {
                    if (snapshot != null) {
                        if (!snapshot.isEmpty) {

                            val documentList = snapshot.documents

                            for (document in documentList) {

                                val user = document.get("name") as String
                                val downloadUri = document.get("image") as String
                                nameState.value = user
                                imageState.value = downloadUri

                            }
                        }
                    }
                }
            }



        instance.firestore.collection("feedImages").whereEqualTo("uid", instance.currentUserId.toString())
            .orderBy("date")
            .addSnapshotListener { value, error ->
                if (error!=null){
                    loadingState.value = false
                    errorState.value = error.localizedMessage
                }else{
                    tempList.clear()
                    if (value != null){
                        val documentList = value.documents
                        for (document in documentList){
                            val image = document.get("downloadUri") as String
                            val uid = document.get("uid") as String
                            var myPostList = MyPostsModel(image, uid)
                            tempList.add(myPostList)
                        }
                        myPostList.value = tempList
                        loadingState.value= false
                    }
                }
            }


    }

}