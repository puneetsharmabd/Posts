package com.posts.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class SinglePost(

    @ColumnInfo(name = "postId")
    @SerializedName("postId")
    val postId: String?,

    @ColumnInfo(name = "id")
    @SerializedName("id")
    val id: String?,

    @ColumnInfo(name = "name")
    @SerializedName("name")
    val name: String?,

    @ColumnInfo(name = "email")
    @SerializedName("email")
    val email: String?,

    @ColumnInfo(name = "body")
    @SerializedName("body")
    val body: String?
){
    @PrimaryKey(autoGenerate = true)
    var uuid : Int = 0
}