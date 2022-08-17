package com.posts.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SinglePostDao {

    @Insert
    suspend fun insertAll(vararg comments : SinglePost) : List<Long>

    @Query("SELECT * FROM singlepost WHERE postId = :postId")
    suspend fun getAllComments(postId : String) : List<SinglePost>
}