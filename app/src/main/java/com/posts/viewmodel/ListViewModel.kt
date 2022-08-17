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
import com.posts.model.Post
import com.posts.model.PostDatabase
import com.posts.model.PostsApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class ListViewModel(application: Application):BaseViewModel(application) {
    private val postsService = PostsApiService()
    private val disposable = CompositeDisposable()
    val posts = MutableLiveData<List<Post>>()
    val postsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    @RequiresApi(Build.VERSION_CODES.M)
    fun refresh()
    {
        if (isOnline(getApplication())) {
            fetchFromRemote()
        }
        else
        {
            fetchFromDatabase()
        }
    }

    private fun fetchFromDatabase(){
        loading.value = true
        launch {
            val posts = PostDatabase(getApplication()).postDao().getAllPosts()
            postsRetrieved(posts)
            Toast.makeText(getApplication(),"Posts retrieved from database.",Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchFromRemote()
    {
        loading.value = true
        disposable.add(
            postsService.getPosts()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Post>>(){
                    override fun onSuccess(postsList: List<Post>) {
                        storePostsLocally(postsList)
                        Toast.makeText(getApplication(),"Posts retrieved from endpoint.",Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(e: Throwable) {
                        postsLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }

                })
        )
    }

    private fun postsRetrieved(postsList : List<Post>){
        posts.value = postsList
        postsLoadError.value = false
        loading.value = false
    }

    private fun storePostsLocally(list : List<Post>){
        launch {
            val dao = PostDatabase(getApplication()).postDao()
            dao.deleteAllPosts()
            val result = dao.insertAll(*list.toTypedArray())
            var i = 0
            while (i < list.size){
                list.get(i).uuid = result.get(i).toInt()
                ++i
            }
            postsRetrieved(list)
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

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun filter(text: String) : List<Post> {
        val temp: ArrayList<Post> = arrayListOf()
        for (d in posts.value!!) {
            if (d.title.toString().contains(text.toString())) {
                temp.add(d)
            }
        }
        return temp
    }
}