package com.yogesh.tinderdemo.retrofit

import com.yogesh.tinderdemo.model.Results
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("/api/?results=10")
    fun getUsers(): Call<Results>
}
