package com.yogesh.tinderdemo.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yogesh.tinderdemo.model.Result
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(usersResponse: List<Result>)

    @Query("SELECT * FROM user_table")
    fun getAllUsers(): Flow<List<Result>>

    @Query("UPDATE user_table SET extraInfo = :string WHERE email = :emailId")
    fun updateProfile(emailId: String, string: String?)
}