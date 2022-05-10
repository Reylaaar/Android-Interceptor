package com.example.androidassestment.models

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_user")
data class CurrentUserModel (
        @PrimaryKey val id: Int,
        val login: String,
        val avatar_url: String,
        val url: String,
        val html_url:String,
        val followers_url: String,
        val gists_url: String,
        val starred_url: String,
        val subscriptions_url: String,
        val organizations_url: String,
        val repos_url: String,
        val events_url: String,
        val received_events_url: String,
        val type: String,
        val site_admin: Boolean,
        val name: String,
        val followers: Int,
        val following: Int
        )
