package com.example.submissionfundamentalandroid2.ui.favorite

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionfundamentalandroid2.R
import com.example.submissionfundamentalandroid2.data.local.entity.FavoriteUser
import com.example.submissionfundamentalandroid2.data.local.room.FavoriteUserDatabase
import com.example.submissionfundamentalandroid2.data.remote.response.UserData
import com.example.submissionfundamentalandroid2.databinding.ActivityFavoriteBinding
import com.example.submissionfundamentalandroid2.ui.component.FavoriteAdapter
import com.example.submissionfundamentalandroid2.ui.component.ListUsersAdapter
import com.example.submissionfundamentalandroid2.ui.detail.DetailActivity

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private val detailViewModel by viewModels<FavoriteViewModel>()
    val db by lazy {
        FavoriteUserDatabase.getDatabase(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorite"

        setupObserver()
    }

    override fun onResume() {
        super.onResume()
        setupObserver()
    }

    private fun setupObserver(){
        detailViewModel.getUsers(db)
        detailViewModel.users.observe(this, { users ->
            setUsersData(users)
        })
        detailViewModel.isLoading.observe(this, {
            showLoading(it)
        })
    }

    private fun setUsersData(users: List<FavoriteUser>) {
        if(users.isNotEmpty()){
            binding.emptyDataMessage.visibility = View.INVISIBLE
            binding.rvFavorite.visibility = View.VISIBLE

            if(applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
                binding.rvFavorite.layoutManager = GridLayoutManager(this, 2)
            }else{
                binding.rvFavorite.layoutManager = LinearLayoutManager(this)
            }

            val listUserAdapter = FavoriteAdapter(users)
            binding.rvFavorite.adapter = listUserAdapter

            listUserAdapter.setOnItemClickCallback(object: FavoriteAdapter.OnItemClickCallback{
                override fun onItemClicked(data: FavoriteUser) {
                    showSelectedUser(data)
                }
            })
        }else{
            binding.emptyDataMessage.visibility = View.VISIBLE
            binding.rvFavorite.visibility = View.INVISIBLE
        }
    }

    private fun showSelectedUser(data: FavoriteUser) {
        val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.DATA_USER, UserData(login = data.login, avatarUrl = data.avatar_url))
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}