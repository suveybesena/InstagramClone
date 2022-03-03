package com.suveybesena.instagramclone.presentation.following

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.suveybesena.instagramclone.R
import com.suveybesena.instagramclone.model.FollowModel
import com.suveybesena.instagramclone.utils.extensions.downloadImage
import kotlinx.android.synthetic.main.add_friends_item.view.*

class FollowingAdapter(var list: List<FollowModel>) : RecyclerView.Adapter<FollowingAdapter.VH>() {
    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.following_item, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val user = list[position]
        holder.itemView.apply {
            tw_name.text = user.name
            user.image?.let { iw_user.downloadImage(it) }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}