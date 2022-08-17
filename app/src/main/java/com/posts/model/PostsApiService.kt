package com.posts.model

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class PostsApiService {

    private val BASE_URL = "https://jsonplaceholder.typicode.com/"

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(PostsApi::class.java)

    fun getPosts() : Single<List<Post>>{
        return api.getPosts()
    }

    fun getSinglePostComments(postId : String) : Single<List<SinglePost>>{
        return api.getSinglePost(postId)
    }
}