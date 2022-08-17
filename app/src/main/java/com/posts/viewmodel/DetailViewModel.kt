package com.posts.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.posts.model.Post
import com.posts.model.PostDatabase
import com.posts.model.PostsApiService
import com.posts.model.SinglePost
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.util.*

class DetailViewModel(application: Application) : BaseViewModel(application) {

    private val postsService = PostsApiService()
    private val disposable = CompositeDisposable()
    val postLiveData = MutableLiveData<Post>()
    val postComment = MutableLiveData<List<SinglePost>>()

    @RequiresApi(Build.VERSION_CODES.M)
    fun fetch (uuid: Int)
    {
        launch {
            val post = PostDatabase(getApplication()).postDao().getPost(uuid)
            postLiveData.value = post
            if (isOnline(getApplication())) {
                post.id?.let { fetchFromRemote(it) }
            }
            else
            {
                post.id?.let { fetchFromDatabase(it, arrayListOf()) }
            }
        }
    }
    private fun fetchFromRemote(userId : String)
    {
        disposable.add(
            postsService.getSinglePostComments(userId!!)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<SinglePost>>() {
                    override fun onSuccess(postComments: List<SinglePost>) {
                        fetchFromDatabase(userId,postComments)
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
        )
    }

    private fun fetchFromDatabase(postId : String,postComments: List<SinglePost>){
        launch {
            val comments = PostDatabase(getApplication()).singlePostDao().getAllComments(postId)
            if (comments.size>0)
            {
                postComment.value = comments
            }
            else
            {
                PostDatabase(getApplication()).singlePostDao().insertAll(*postComments.toTypedArray())
                val comments = PostDatabase(getApplication()).singlePostDao().getAllComments(postId)
                postComment.value = comments
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    fun filter(text: String) : List<SinglePost> {
        val temp: ArrayList<SinglePost> = arrayListOf()
        for (d in postComment.value!!) {
            if (d.name.toString().contains(text.toString())) {
                temp.add(d)
            }
        }
        return temp
    }
}