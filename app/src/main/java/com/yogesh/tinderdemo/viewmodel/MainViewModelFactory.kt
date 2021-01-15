package com.yogesh.tinderdemo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yogesh.tinderdemo.repository.IUsersRepository
import com.yogesh.tinderdemo.util.NetworkHelper

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(
    private val networkHelper: NetworkHelper,
    private val IUsersRepository: IUsersRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(networkHelper, IUsersRepository) as T
    }
}