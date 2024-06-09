package com.genthur.githubapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.genthur.githubapp.database.FavoriteDao
import com.genthur.githubapp.database.FavoriteEntity
import com.genthur.githubapp.database.FavoriteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val favoriteDao: FavoriteDao

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        favoriteDao = db.favoriteDao()
    }

    fun getAllFavorites(): LiveData<List<FavoriteEntity>> {
        return favoriteDao.getAllFavorites()
    }
}