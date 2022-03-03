package com.suveybesena.instagramclone.presentation.search

import android.widget.Button

interface GetUserInterface {

    fun getUserList (userId : String, follow : Button, unfollow : Button)
}