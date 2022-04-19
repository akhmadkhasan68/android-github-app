package com.example.submissionfundamentalandroid2.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.submissionfundamentalandroid2.ui.main.MainViewModel
import com.example.submissionfundamentalandroid2.ui.settings.SettingsViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(private val pref: SettingsPreferences): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(pref) as T
        }else if(modelClass.isAssignableFrom(SettingsViewModel::class.java)){
            return SettingsViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}