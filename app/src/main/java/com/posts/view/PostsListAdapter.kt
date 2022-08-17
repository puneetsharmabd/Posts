package com.posts.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.posts.R
import com.posts.model.Post
import kotlinx.android.synthetic.main.item_post.view.*

class PostsListAdapter(val postsList : ArrayList<Post>) : RecyclerView.Adapter<PostsListAdapter.PostViewHolder>() {
    fun updatePostList(newPostList : List<Post>)
    {
        postsList.clear()
        postsList.addAll(newPostList)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_post,parent,false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.view.postTitle.text = postsList.get(position).title
        holder.view.postBody.text = postsList.get(position).body
        holder.view.setOnClickListener {
            val action = ListFragmentDirections.actionDetailFragment()
            action.postId = postsList.get(position).uuid
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount() = postsList.size

    class PostViewHolder(var view: View) : RecyclerView.ViewHolder(view)
}