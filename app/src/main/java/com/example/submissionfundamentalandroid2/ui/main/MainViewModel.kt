package com.example.submissionfundamentalandroid2.ui.main

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.submissionfundamentalandroid2.BuildConfig
import com.example.submissionfundamentalandroid2.data.remote.response.UserData
import com.example.submissionfundamentalandroid2.data.remote.response.UserSearchResponse
import com.example.submissionfundamentalandroid2.data.remote.retrofit.GithubApiConfig
import com.example.submissionfundamentalandroid2.data.remote.retrofit.GithubApiService
import com.example.submissionfundamentalandroid2.utils.SettingsPreferences
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel(private val pref: SettingsPreferences): ViewModel() {
    private val _users = MutableLiveData<List<UserData>>()
    val users : LiveData<List<UserData>> = _users

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    fun getThemeSetting(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun searchUsers(query: String){
        _isLoading.value = true
        val client = GithubApiConfig.getGithubApiServices().searchUsers(query)
        client.enqueue(object : Callback<UserSearchResponse>{
            override fun onResponse(
                call: Call<UserSearchResponse>,
                response: Response<UserSearchResponse>
            ) {
                _isLoading.value = false
                if(response.isSuccessful){
                    _users.value = response.body()?.items
                    Log.e(TAG, response.body()?.items.toString())
                }else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserSearchResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}