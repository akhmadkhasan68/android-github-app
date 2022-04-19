package com.example.submissionfundamentalandroid2.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submissionfundamentalandroid2.data.local.entity.FavoriteUser
import com.example.submissionfundamentalandroid2.data.local.room.FavoriteUserDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteViewModel: ViewModel() {
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    private val _users = MutableLiveData<List<FavoriteUser>>()
    val users: LiveData<List<FavoriteUser>> = _users

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    fun getUsers(db: FavoriteUserDatabase){
        _isLoading.postValue(true)
        executorService.execute {
            _users.postValue(db.favoriteUserDao().getFavoriteUser())
            _isLoading.postValue(false)
        }
    }
}