package com.suveybesena.instagramclone.utils.extensions

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class FirebaseInstance @Inject constructor() {
    var auth = FirebaseAuth.getInstance()
    var firestore = FirebaseFirestore.getInstance()
    var storage = FirebaseStorage.getInstance()
    var currentUser = auth.currentUser
    var currentUserId = currentUser?.uid
}