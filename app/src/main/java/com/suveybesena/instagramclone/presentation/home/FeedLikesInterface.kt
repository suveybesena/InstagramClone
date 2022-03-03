package com.suveybesena.instagramclone.presentation.home

import android.widget.ImageView

interface FeedLikesInterface {

    fun getLikes (image : String, like :ImageView, unlike :ImageView)
}