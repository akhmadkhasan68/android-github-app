package com.example.submissionfundamentalandroid2.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.submissionfundamentalandroid2.R
import com.example.submissionfundamentalandroid2.data.local.room.FavoriteUserDatabase
import com.example.submissionfundamentalandroid2.ui.component.SectionsPagerAdapter
import com.example.submissionfundamentalandroid2.databinding.ActivityDetailBinding
import com.example.submissionfundamentalandroid2.data.remote.response.UserData
import com.example.submissionfundamentalandroid2.data.remote.response.UserDetailResponse
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel>()

    val db by lazy {
        FavoriteUserDatabase.getDatabase(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.bringToFront()

        supportActionBar?.hide()

        val userData = intent.getParcelableExtra<UserData>(DATA_USER) as UserData
        detailViewModel.getDetailUser(userData.login)
        detailViewModel.getFollowers(userData.login)
        detailViewModel.getFollowing(userData.login)
        detailViewModel.checkUserExistDb(userData.login, db)

        setupTabLayout()
        setupListener(userData)
        observer()
    }

    private fun setupListener(userData: UserData){
        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.btnFavorite.setOnClickListener {
            addToFavorites(userData.login, userData.avatarUrl)
            observerIsUserExistOnDb()
        }
    }

    private fun observer(){
        detailViewModel.data.observe(this, { data ->
            setUserData(data)
        })

        detailViewModel.isLoading.observe(this, {
            showLoading(it)
        })

        observerIsUserExistOnDb()
    }

    private fun observerIsUserExistOnDb() {
        detailViewModel.userOnDb.observe(this, {
            if(it){
                binding.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite)
            }else{
                binding.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_border)
            }
        })
    }

    private fun setupTabLayout(){
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.bringToFront()
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun addToFavorites(login: String, avatarUrl: String) {
        detailViewModel.actionBtnFavorite(login, avatarUrl, db)
    }

    private fun setUserData(data: UserDetailResponse) {
        binding.tvToolbar.text = data.login
        binding.tvUsername.text = data.login
        binding.tvName.text = data.name
        binding.tvLocation.text = data.location
        binding.tvCompany.text = data.company
        binding.tvFollower.text = data.followers.toString()
        binding.tvFollowing.text = data.following.toString()
        binding.tvRepository.text = data.publicRepos.toString()
        Glide.with(this)
            .load(data.avatarUrl)
            .into(binding.imgAvatar)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object{
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers_label,
            R.string.following_label
        )

        const val DATA_USER = "data_user"
    }
}