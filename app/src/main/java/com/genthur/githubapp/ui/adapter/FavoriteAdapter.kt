package com.genthur.githubapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.genthur.githubapp.database.FavoriteEntity
import com.genthur.githubapp.databinding.UserListBinding
import com.genthur.githubapp.helper.FavoriteUserCallback

class FavoriteAdapter: RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private val listFavUser = ArrayList<FavoriteEntity>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setListFavorite(listFav: List<FavoriteEntity>) {
        val diffCallback = FavoriteUserCallback(this.listFavUser, listFav)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavUser.clear()
        this.listFavUser.addAll(listFav)
        diffResult.dispatchUpdatesTo(this)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteViewHolder {
        val binding = UserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavUser[position])
    }

    override fun getItemCount(): Int {
        return listFavUser.size
    }

    inner class FavoriteViewHolder(private val binding: UserListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(users: FavoriteEntity) {
            Glide.with(binding.imgUser.context)
                .load(users.avatarUrl)
                .into(binding.imgUser)
            binding.tvUsername.text = users.username
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(users, adapterPosition)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(favorite: FavoriteEntity, position: Int)
    }

}