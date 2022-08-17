package com.posts.model

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PostsApi {
    @GET("posts")
    fun getPosts() : Single<List<Post>>

    @GET("comments")
    fun getSinglePost(@Query("postId") postId:String) : Single<List<SinglePost>>
}