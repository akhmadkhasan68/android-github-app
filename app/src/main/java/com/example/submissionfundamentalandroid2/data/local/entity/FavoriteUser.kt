package com.example.submissionfundamentalandroid2.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteUser(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val login: String,
    val avatar_url: String,
)
