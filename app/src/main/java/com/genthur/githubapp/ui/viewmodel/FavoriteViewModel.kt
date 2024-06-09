package com.genthur.githubapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.genthur.githubapp.database.FavoriteEntity
import com.genthur.githubapp.repository.FavoriteRepository

class FavoriteViewModel(application: Application): ViewModel() {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)


    fun getAllFavorites(): LiveData<List<FavoriteEntity>> {
        return mFavoriteRepository.getAllFavorites()
    }
}