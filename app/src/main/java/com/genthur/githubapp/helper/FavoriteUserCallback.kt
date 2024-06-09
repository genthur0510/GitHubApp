package com.genthur.githubapp.helper

import androidx.recyclerview.widget.DiffUtil
import com.genthur.githubapp.database.FavoriteEntity

class FavoriteUserCallback(private val oldFavList: List<FavoriteEntity>, private val newFavList: List<FavoriteEntity>): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldFavList.size
    }

    override fun getNewListSize(): Int {
        return newFavList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFavList[oldItemPosition].username == newFavList[newItemPosition].username
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFav = oldFavList[oldItemPosition]
        val newFav = newFavList[newItemPosition]
        return oldFav.username == newFav.username && oldFav.avatarUrl == newFav.avatarUrl
    }

}