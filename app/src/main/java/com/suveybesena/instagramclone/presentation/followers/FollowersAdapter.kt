package com.suveybesena.instagramclone.presentation.followers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.suveybesena.instagramclone.R
import com.suveybesena.instagramclone.model.FollowersModel
import com.suveybesena.instagramclone.utils.extensions.downloadImage
import kotlinx.android.synthetic.main.add_friends_item.view.*

class FollowersAdapter(var list: List<FollowersModel>) :
    RecyclerView.Adapter<FollowersAdapter.FollowersVH>() {
    class FollowersVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowersVH {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.followers_item, parent, false)
        return FollowersVH(view)
    }

    override fun onBindViewHolder(holder: FollowersVH, position: Int) {
        val user = list[position]
        holder.itemView.apply {
            tw_name.text = user.name
            user.image.toString()?.let { iw_user.downloadImage(it) }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}