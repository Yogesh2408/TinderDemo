package com.yogesh.tinderdemo.repository

import com.yogesh.tinderdemo.database.UsersDao
import com.yogesh.tinderdemo.model.Result
import com.yogesh.tinderdemo.model.Results
import com.yogesh.tinderdemo.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersRepositoryImpl(
    private val usersDao: UsersDao,
    private val apiService: ApiService
) : IUsersRepository {
    override fun getUsers(
        onSuccess: (Results) -> Unit,
        onFailure: (String?) -> Unit
    ) {
        val call = apiService.getUsers()

        call.enqueue(object : Callback<Results> {
            override fun onResponse(
                call: Call<Results>,
                response: Response<Results>
            ) {
                if (response.isSuccessful && response.body() != null) {//200
                    onSuccess(response.body()!!)
                } else { // status code range: 300,400,500
                    onFailure(response.message())
                }
            }

            override fun onFailure(call: Call<Results>, t: Throwable) {
                onFailure(t.message)
            }
        })
    }

    override fun insertProfile(results: List<Result>) {
        usersDao.insertUsers(results)
    }

    override fun getLocalUsers(): Flow<List<Result>> {
        return usersDao.getAllUsers()
    }

    override fun updateUserProfile(result: Result) {
        Thread {
            usersDao.updateProfile(result.email, result.extraInfo)
        }.start()
    }
}