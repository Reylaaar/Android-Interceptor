package com.example.androidassestment.api

import com.example.androidassestment.models.ContactsDetailsModel
import com.example.androidassestment.models.ContactsModel
import com.example.androidassestment.models.ContactsViewModel
import com.example.androidassestment.models.CurrentUserModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*

interface ApiManager {

    @GET("users")
    fun getContacts(): Call<ArrayList<ContactsDetailsModel>>

    @GET("users/{login}")
    fun getCurrentUser(@Path("login") login: String): Call<CurrentUserModel>

    @GET("users/{login}")
    fun getCurrentContacts(@Path("login") login: String): Call<ContactsViewModel>
}