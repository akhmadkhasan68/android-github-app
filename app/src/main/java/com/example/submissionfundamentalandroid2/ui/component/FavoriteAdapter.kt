package com.example.submissionfundamentalandroid2.ui.component

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submissionfundamentalandroid2.R
import com.example.submissionfundamentalandroid2.data.local.entity.FavoriteUser
import com.example.submissionfundamentalandroid2.data.remote.response.UserData

class FavoriteAdapter(private val listUsers: List<FavoriteUser>): RecyclerView.Adapter<FavoriteAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: FavoriteUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        return FavoriteAdapter.ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (_, login, avatar_url) = listUsers[position]
        holder.tvUsername.text = login
        Glide.with(holder.itemView.context)
            .load(avatar_url)
            .circleCrop()
            .into(holder.imgPhoto)

        holder.itemView.setOnClickListener{onItemClickCallback.onItemClicked(listUsers[holder.adapterPosition])}
    }

    override fun getItemCount(): Int = listUsers.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
        var tvUsername: TextView = itemView.findViewById(R.id.tv_item_username)
    }
}