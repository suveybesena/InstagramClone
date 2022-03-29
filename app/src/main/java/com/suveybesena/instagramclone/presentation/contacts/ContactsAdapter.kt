package com.suveybesena.instagramclone.presentation.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.suveybesena.instagramclone.R
import kotlinx.android.synthetic.main.add_friends_item.view.*

class ContactsAdapter ( var list : ArrayList<String>) :RecyclerView.Adapter<ContactsAdapter.ContactsVH>() {
    class ContactsVH(itemView : View)  :RecyclerView.ViewHolder(itemView){
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsVH {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.contacts_item, parent, false)
        return ContactsVH(view)
    }

    override fun onBindViewHolder(holder: ContactsVH, position: Int) {
        val contacts = list[position]

        holder.itemView.apply {
            tw_name.text = contacts
            setOnClickListener {
                onItemClickListener?.let { it(contacts) }
            }
        }
    }

    override fun getItemCount(): Int {
      return list.size
    }

   private var onItemClickListener: ((String) -> Unit)? = null
   fun setOnItemClickListener(listener: (String) -> Unit) {
       onItemClickListener = listener
   }

}