package com.suveybesena.instagramclone.presentation.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.suveybesena.instagramclone.R
import com.suveybesena.instagramclone.model.User
import com.suveybesena.instagramclone.utils.extensions.downloadImage
import kotlinx.android.synthetic.main.add_friends_item.view.*

class FriendsAdapter(
    var list: List<User>,
    var followInterface: FollowInterface,
    var unfollowInterface: UnfollowInterface,
    var getUserInterface : GetUserInterface
) :
    RecyclerView.Adapter<FriendsAdapter.VH>() {
    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.add_friends_item, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val user = list[position]
        val userId = user.uid.toString()
        val image = user.image
        holder.itemView.apply {
            tw_name.text = user.username
            user.image?.let { imageString -> iw_user.downloadImage(imageString) }

            checkFollowingStatus(bw_follow, userId, bw_unfollow)

            bw_follow.setOnClickListener {
                if (image != null) {
                        followInterface.itemOnClick(userId)
                }
            }
            bw_unfollow.setOnClickListener {
                unfollowInterface.onItemClick(userId)

            }
        }
    }


    fun checkFollowingStatus(followButton : Button,userId : String, unfollowButton : Button ){
        getUserInterface.getUserList(userId, followButton, unfollowButton)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}