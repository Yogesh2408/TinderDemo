package com.yogesh.tinderdemo.repository

import com.yogesh.tinderdemo.model.Result
import com.yogesh.tinderdemo.model.Results
import kotlinx.coroutines.flow.Flow

interface IUsersRepository {

    fun getUsers(onSuccess: (Results) -> Unit, onFailure: (String?) -> Unit)

    fun getLocalUsers(): Flow<List<Result>>

    fun insertProfile(results: List<Result>)

    fun updateUserProfile(result: Result)
}