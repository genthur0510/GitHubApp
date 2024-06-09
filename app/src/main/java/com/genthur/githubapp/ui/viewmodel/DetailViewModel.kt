package com.genthur.githubapp.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.genthur.githubapp.data.response.DetailUserResponse
import com.genthur.githubapp.data.retrofit.ApiConfig
import com.genthur.githubapp.database.FavoriteDao
import com.genthur.githubapp.database.FavoriteEntity
import com.genthur.githubapp.database.FavoriteRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailViewModel(application: Application): AndroidViewModel(application) {
    private val _detailUser = MutableLiveData<DetailUserResponse>()
    val detailUser: LiveData<DetailUserResponse> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _userFavoriteState = MutableLiveData<Boolean>()
    val userFavoriteState: LiveData<Boolean> = _userFavoriteState

    var userFavorite: Boolean = false

    private var favoriteDao: FavoriteDao
    private var db: FavoriteRoomDatabase

    init {
        db = FavoriteRoomDatabase.getDatabase(application)
        favoriteDao = db.favoriteDao()
    }

    init {
        _userFavoriteState.value = false

    }

    fun displayDetail(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _detailUser.postValue(responseBody!!)
                } else {
                    Log.e(TAG, "OnFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "OnFailure: ${t.message.toString()}")
            }

        })
    }

    fun addFavorite(username: String, avatarUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = FavoriteEntity(username, avatarUrl)
            favoriteDao?.addFavorite(user)
            userFavorite = true // Update the local variable
            _userFavoriteState.postValue(true) // Update UI state
        }
    }

    fun deleteFavorite(username: String, avatarUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = FavoriteEntity(username, avatarUrl)
            favoriteDao?.deleteFavorite(user)
            userFavorite = false // Update the local variable
            _userFavoriteState.postValue(false) // Update UI state
        }
    }

    fun isUserFavorite(username: String): LiveData<Boolean> {
        return favoriteDao.getFavoriteUserByUsername(username).map { it.isNotEmpty() }
    }



    companion object {
        private const val TAG = "DetailViewModel"
    }
}