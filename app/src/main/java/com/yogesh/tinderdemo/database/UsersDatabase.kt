package com.yogesh.tinderdemo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yogesh.tinderdemo.database.typeConverter.DobConverter
import com.yogesh.tinderdemo.database.typeConverter.NameConverter
import com.yogesh.tinderdemo.database.typeConverter.PictureConverter
import com.yogesh.tinderdemo.model.Result

@Database(entities = [Result::class], version = 1, exportSchema = false)
@TypeConverters(*arrayOf(DobConverter::class, NameConverter::class, PictureConverter::class))
abstract class UsersDatabase : RoomDatabase() {

    abstract fun usersDao(): UsersDao

    companion object {

        @Volatile
        private var INSTANCE: UsersDatabase? = null

        private const val DATABASE_NAME = "users-database"

        fun getInstance(context: Context): UsersDatabase {
            synchronized(this) {
                var instance =
                    INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        UsersDatabase::class.java,
                        DATABASE_NAME
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}