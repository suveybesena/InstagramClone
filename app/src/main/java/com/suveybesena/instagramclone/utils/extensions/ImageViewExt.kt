package com.suveybesena.instagramclone.utils.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.downloadImage(imageUrl:String){
    try {
        Glide.with(this.context).load(imageUrl).into(this)
    } catch (e:Exception){
        println(e.localizedMessage)
    }
}