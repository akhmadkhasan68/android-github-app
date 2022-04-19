package com.example.submissionfundamentalandroid2.data.remote.retrofit

import com.example.submissionfundamentalandroid2.BuildConfig
import com.example.submissionfundamentalandroid2.data.remote.response.UserData
import com.example.submissionfundamentalandroid2.data.remote.response.UserDetailResponse
import com.example.submissionfundamentalandroid2.data.remote.response.UserSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiService {
    @GET("search/users")
    fun searchUsers(
        @Query("q") query: String
    ): Call<UserSearchResponse>

    @GET("users/{username}")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun detailUser(
        @Path("username") username: String
    ): Call<UserDetailResponse>

    @GET("users/{username}/following")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<UserData>>

    @GET("users/{username}/followers")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<UserData>>
}