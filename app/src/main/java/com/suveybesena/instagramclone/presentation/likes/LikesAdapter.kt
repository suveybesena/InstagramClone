package com.suveybesena.instagramclone.presentation.likes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.suveybesena.instagramclone.R
import com.suveybesena.instagramclone.model.LikesModel
import com.suveybesena.instagramclone.utils.extensions.downloadImage
import kotlinx.android.synthetic.main.likes_item.view.*

class LikesAdapter(
    var likesList: List<LikesModel>,
): RecyclerView.Adapter<LikesAdapter.LikesVH>() {
    class LikesVH (itemView : View) : RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikesVH {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.likes_item, parent, false)
        return LikesVH(view)
    }

    override fun onBindViewHolder(holder: LikesVH, position: Int) {

        holder.itemView.iw_likes.downloadImage(likesList[position].postImage)


    }

    override fun getItemCount(): Int {
       return  likesList.size
    }



}