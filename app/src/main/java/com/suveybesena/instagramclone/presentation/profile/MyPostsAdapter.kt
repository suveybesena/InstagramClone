package com.suveybesena.instagramclone.presentation.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.suveybesena.instagramclone.R
import com.suveybesena.instagramclone.model.MyPostsModel
import kotlinx.android.synthetic.main.my_posts_item.view.*


class MyPostsAdapter(var myPostList: List<MyPostsModel>) :
    RecyclerView.Adapter<MyPostsAdapter.MyPostsVH>() {
    class MyPostsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPostsVH {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.my_posts_item, parent, false)
        return MyPostsVH(view)
    }

    override fun onBindViewHolder(holder: MyPostsVH, position: Int) {

        Glide.with(holder.itemView).load(myPostList[position].image)
            .into(holder.itemView.iw_my_posts)


    }

    override fun getItemCount(): Int {
        return myPostList.size
    }
}