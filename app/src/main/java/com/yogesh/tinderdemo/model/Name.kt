package com.yogesh.tinderdemo.model

data class Name(
    val first: String,
    val last: String,
    val title: String
) {
    fun getFullName(): String {
        return "$title. $first $last"
    }
}