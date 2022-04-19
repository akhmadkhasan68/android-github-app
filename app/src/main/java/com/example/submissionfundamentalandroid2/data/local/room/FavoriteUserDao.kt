package com.example.submissionfundamentalandroid2.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.submissionfundamentalandroid2.data.local.entity.FavoriteUser

@Dao
interface FavoriteUserDao {
    @Insert
    fun addFavoriteUser(user: FavoriteUser)

    @Delete
    fun deleteFavoriteUser(user: FavoriteUser)

    @Query("SELECT * FROM favoriteuser WHERE login=:login")
    fun getUserbyUsername(login: String): FavoriteUser

    @Query("SELECT * FROM favoriteuser")
    fun getFavoriteUser(): List<FavoriteUser>
}