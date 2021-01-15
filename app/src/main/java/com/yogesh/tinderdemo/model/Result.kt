package com.yogesh.tinderdemo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class Result(
    @PrimaryKey()
    val email: String,
    val gender: String,
    val name: Name,
    val phone: String,
    val picture: Picture,
    val dob: Dob,
    var extraInfo: String?
)