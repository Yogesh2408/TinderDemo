package com.yogesh.tinderdemo.viewmodel

import androidx.lifecycle.*
import com.yogesh.tinderdemo.model.Result
import com.yogesh.tinderdemo.repository.IUsersRepository
import com.yogesh.tinderdemo.util.NetworkHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val networkHelper: NetworkHelper,
    private val IUsersRepository: IUsersRepository
) : ViewModel() {
    companion object {
        private const val SOMETHING_WENT_WRONG = "Oops! Something Went Wrong :("
        private const val NO_INTERNET = "Oops! No Internet Connection :("
    }

    private var _usersResponse = IUsersRepository.getLocalUsers()
        .asLiveData(viewModelScope.coroutineContext)
    val usersResponse: LiveData<List<Result>> = _usersResponse

    private val _errorResponse = MutableLiveData<String>()
    val errorResponse: LiveData<String> = _errorResponse

    fun onRefresh() {
        if (networkHelper.isNetworkConnected()) {
            IUsersRepository.getUsers({ usersResponse ->
                viewModelScope.launch(Dispatchers.IO) {
                    IUsersRepository.insertProfile(usersResponse.results)
                }
            }, {
                _errorResponse.postValue(SOMETHING_WENT_WRONG)
            })
        } else {
            _errorResponse.postValue(NO_INTERNET)
        }
    }

    fun updateUserProfile(result: Result) {
        Thread {
            IUsersRepository.updateUserProfile(result)
        }.start()
    }
}

