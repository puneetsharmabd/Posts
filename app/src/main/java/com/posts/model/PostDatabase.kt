package com.posts.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Post::class,SinglePost::class), version = 1)
abstract class PostDatabase : RoomDatabase() {
    abstract fun postDao() : PostDao
    abstract fun singlePostDao() : SinglePostDao

    companion object {
        @Volatile private var instance : PostDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context : Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also{
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            PostDatabase::class.java,
            name = "postdatabase"
        ).build()
    }
}