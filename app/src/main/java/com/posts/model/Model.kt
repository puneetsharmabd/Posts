package com.posts.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Post(
    @ColumnInfo(name = "userId")
    @SerializedName("userId")
    val userId: String?,

    @ColumnInfo(name = "id")
    @SerializedName("id")
    val id: String?,

    @ColumnInfo(name = "title")
    @SerializedName("title")
    val title: String?,

    @ColumnInfo(name = "body")
    @SerializedName("body")
    val body: String
) {
    @PrimaryKey(autoGenerate = true)
    var uuid : Int = 0
}