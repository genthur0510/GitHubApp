package com.genthur.githubapp.data.retrofit

import com.genthur.githubapp.data.response.DetailUserResponse
import com.genthur.githubapp.data.response.GithubResponse
import com.genthur.githubapp.data.response.ItemsItem
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getUsers(
        @Query("q") query: String
    ): Call<GithubResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getDetailUser(@Path("username") username: String): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(@Path("username") username: String): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(@Path("username") username: String): Call<List<ItemsItem>>
}