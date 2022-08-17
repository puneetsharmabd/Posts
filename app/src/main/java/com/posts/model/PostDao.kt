package com.posts.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PostDao {

    @Insert
    suspend fun insertAll(vararg posts : Post) : List<Long>

    @Query("SELECT * FROM post")
    suspend fun getAllPosts() : List<Post>

    @Query("SELECT * FROM post WHERE uuid = :postId")
    suspend fun getPost(postId : Int) : Post

    @Query("DELETE FROM post")
    suspend fun deleteAllPosts()
}