package com.example.submissionfundamentalandroid2.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionfundamentalandroid2.R
import com.example.submissionfundamentalandroid2.ui.component.ListUsersAdapter
import com.example.submissionfundamentalandroid2.databinding.ActivityMainBinding
import com.example.submissionfundamentalandroid2.data.remote.response.UserData
import com.example.submissionfundamentalandroid2.ui.detail.DetailActivity
import com.example.submissionfundamentalandroid2.ui.favorite.FavoriteActivity
import com.example.submissionfundamentalandroid2.ui.settings.SettingsActivity
import com.example.submissionfundamentalandroid2.utils.SettingsPreferences
import com.example.submissionfundamentalandroid2.utils.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingsPreferences.getInstance(dataStore)
        mainViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(MainViewModel::class.java)

        setupObserver()
    }

    private fun setupObserver(){
        mainViewModel.users.observe(this, { users ->
            setUsersData(users)
        })

        mainViewModel.isLoading.observe(this, {
            showLoading(it)
        })

        mainViewModel.getThemeSetting().observe(this, { isDarkModeActive: Boolean ->
            if(isDarkModeActive){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        })
    }

    private fun setUsersData(users: List<UserData>) {
        if(users.isNotEmpty()){
            binding.emptyDataMessage.visibility = View.INVISIBLE
            binding.rvUser.visibility = View.VISIBLE

            if(applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
                binding.rvUser.layoutManager = GridLayoutManager(this, 2)
            }else{
                binding.rvUser.layoutManager = LinearLayoutManager(this)
            }

            val listUserAdapter = ListUsersAdapter(users)
            binding.rvUser.adapter = listUserAdapter

            listUserAdapter.setOnItemClickCallback(object: ListUsersAdapter.OnItemClickCallback{
                override fun onItemClicked(data: UserData) {
                    showSelectedUser(data)
                }
            })
        }else{
            binding.emptyDataMessage.visibility = View.VISIBLE
            binding.rvUser.visibility = View.INVISIBLE
        }
    }

    private fun showSelectedUser(data: UserData) {
        val intent = Intent(this@MainActivity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.DATA_USER, data)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_options, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = "Enter Keyword"
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                if(query.length == 0){
                    Toast
                        .makeText(this@MainActivity, "Keyword Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
                    searchView.clearFocus()
                }else{
                    mainViewModel.searchUsers(query)
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(getCurrentFocus()?.getWindowToken(), 0)
                }

                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                return false
            }

        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favorite -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)

                return true
            }
            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)

                return true
            }
            else -> return true
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}