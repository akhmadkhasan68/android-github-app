package com.example.submissionfundamentalandroid2.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.submissionfundamentalandroid2.data.local.entity.FavoriteUser

@Database(entities = [FavoriteUser::class], version = 2)
abstract class FavoriteUserDatabase: RoomDatabase() {
    abstract fun favoriteUserDao(): FavoriteUserDao

    companion object{
        @Volatile
        private var INSTANCE : FavoriteUserDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): FavoriteUserDatabase{
            if(INSTANCE == null){
                synchronized(FavoriteUserDatabase::class.java){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, FavoriteUserDatabase::class.java, "favorite_user_database")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }

            return INSTANCE as FavoriteUserDatabase
        }
    }
}