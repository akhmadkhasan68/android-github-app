package com.example.submissionfundamentalandroid2.ui.detail

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submissionfundamentalandroid2.data.local.entity.FavoriteUser
import com.example.submissionfundamentalandroid2.data.local.room.FavoriteUserDatabase
import com.example.submissionfundamentalandroid2.data.remote.response.UserData
import com.example.submissionfundamentalandroid2.data.remote.response.UserDetailResponse
import com.example.submissionfundamentalandroid2.data.remote.retrofit.GithubApiConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DetailViewModel: ViewModel() {
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    private val _data = MutableLiveData<UserDetailResponse>()
    val data: LiveData<UserDetailResponse> = _data

    private val _followers = MutableLiveData<List<UserData>>()
    val followers: LiveData<List<UserData>> = _followers

    private val _following = MutableLiveData<List<UserData>>()
    val following: LiveData<List<UserData>> = _following

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _followersLoading = MutableLiveData<Boolean>()
    val followersLoading: LiveData<Boolean> = _followersLoading

    private val _followingLoading = MutableLiveData<Boolean>()
    val followingLoading: LiveData<Boolean> = _followingLoading

    private val _userOnDb = MutableLiveData<Boolean>()
    val userOnDb : LiveData<Boolean> = _userOnDb

    fun getDetailUser(username: String){
        Log.e(ContentValues.TAG, "getDetailUserStart")
        _isLoading.value = true
        val client = GithubApiConfig.getGithubApiServices().detailUser(username)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                _isLoading.value = false
                if(response.isSuccessful){
                    _data.value = response.body()
                    Log.e(ContentValues.TAG, response.body().toString())
                }else {
                    Log.e(ContentValues.TAG, "error fetch detail user ${response.toString()}")
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getFollowers(query: String){
        _followersLoading.value = true
        val client = GithubApiConfig.getGithubApiServices().getFollowers(query)
        client.enqueue(object : Callback<List<UserData>> {
            override fun onResponse(
                call: Call<List<UserData>>,
                response: Response<List<UserData>>
            ) {
                _followersLoading.value = false
                if(response.isSuccessful){
                    _followers.value = response.body()
                    Log.e(ContentValues.TAG, response.body().toString())
                }else {
                    Log.e(ContentValues.TAG, "error fetch followers user ${response.toString()}")
                    Log.e(ContentValues.TAG, "onFailure: ${response.message().toString()}")
                }
            }

            override fun onFailure(call: Call<List<UserData>>, t: Throwable) {
                _followersLoading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getFollowing(query: String){
        _followingLoading.value = true
        val client = GithubApiConfig.getGithubApiServices().getFollowing(query)
        client.enqueue(object : Callback<List<UserData>> {
            override fun onResponse(
                call: Call<List<UserData>>,
                response: Response<List<UserData>>
            ) {
                _followingLoading.value = false
                if(response.isSuccessful){
                    _following.value = response.body()
                    Log.e(ContentValues.TAG, response.body().toString())
                }else {
                    Log.e(ContentValues.TAG, "error fetch following user ${response.toString()}")
                    Log.e(ContentValues.TAG, "onFailure: ${response.message().toString()}")
                }
            }

            override fun onFailure(call: Call<List<UserData>>, t: Throwable) {
                _followingLoading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun actionBtnFavorite(login: String, avatarUrl: String, db: FavoriteUserDatabase){
        executorService.execute {
            val userIsExist = db.favoriteUserDao().getUserbyUsername(login)
            if(userIsExist != null){
                db.favoriteUserDao().deleteFavoriteUser(
                    FavoriteUser(userIsExist.id, login, avatarUrl)
                )
                _userOnDb.postValue(false)
            }else{
                db.favoriteUserDao().addFavoriteUser(
                    FavoriteUser(0, login, avatarUrl)
                )
                _userOnDb.postValue(true)
            }
        }
    }

    fun checkUserExistDb(login: String, db: FavoriteUserDatabase){
        executorService.execute {
            val userIsExist = db.favoriteUserDao().getUserbyUsername(login)
            if(userIsExist != null) {
                _userOnDb.postValue(true)
            }else{
                _userOnDb.postValue(false)
            }
        }
    }
}