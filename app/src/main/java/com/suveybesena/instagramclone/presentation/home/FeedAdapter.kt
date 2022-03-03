package com.suveybesena.instagramclone.presentation.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.suveybesena.instagramclone.presentation.likes.LikesInterface
import com.suveybesena.instagramclone.R
import com.suveybesena.instagramclone.model.Firebase
import com.suveybesena.instagramclone.presentation.likes.UnlikeInterface
import com.suveybesena.instagramclone.utils.extensions.downloadImage
import kotlinx.android.synthetic.main.feed_recycler_row.view.*


class FeedAdapter(
    var list: List<Firebase>,
    var likeInterface: LikesInterface,
    var getLikesInterface: FeedLikesInterface,
    var unlikeInterface: UnlikeInterface
) : RecyclerView.Adapter<FeedAdapter.FeedVH>() {
    class FeedVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedVH {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.feed_recycler_row, parent, false)
        return FeedVH(view)
    }

    override fun onBindViewHolder(holder: FeedVH, position: Int) {
        val user = list[position]
        val image = user.image

        holder.itemView.apply {
            tw_comment.text = user.comment
            tw_username_comment.text = user.user
            tw_username_feed.text = user.location
            imw_post.downloadImage(user.image)
            iw_user_profile_feed.downloadImage(user.allInOne[user.image].toString())

        }
        val like = holder.itemView.imw_like
        val unlike = holder.itemView.imw_unLike
        getLikesInterface.getLikes(list.get(position).image, like, unlike)

        holder.itemView.imw_like.setOnClickListener {
            likeInterface.likeItemClick(image)
        }
        holder.itemView.imw_unLike.setOnClickListener {
            unlikeInterface.unlikeItemClick(image)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }


}