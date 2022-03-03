package com.suveybesena.instagramclone.model

import android.net.Uri
import android.view.View

data class RegisterModel (
   var mail: String,
   var password: String,
   var pickedImage: Uri,
   var view: View,
   var  username: String,
   var name : String
        ){
}