package com.posts.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.posts.R
import com.posts.model.SinglePost
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentsListAdapter(val commentList : ArrayList<SinglePost>) : RecyclerView.Adapter<CommentsListAdapter.CommentViewHolder>() {

    fun updateCommentList(newPostList : List<SinglePost>)
    {
        commentList.clear()
        commentList.addAll(newPostList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_comment,parent,false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.view.commentName.text = commentList.get(position).name
        holder.view.commentBody.text = commentList.get(position).body
    }

    override fun getItemCount() = commentList.size

    class CommentViewHolder (var view : View) : RecyclerView.ViewHolder(view)
}