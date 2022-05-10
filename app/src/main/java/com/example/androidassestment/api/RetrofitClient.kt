package com.example.androidassestment.api

import com.example.androidassestment.Globals
import com.example.androidassestment.models.ContactsDetailsModel
import com.example.androidassestment.models.ContactsModel
import com.example.androidassestment.models.ContactsViewModel
import com.example.androidassestment.models.CurrentUserModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClient {

    var BASE_URL = Globals.baseUrl
    var BASE_TOKEN = "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"

    var headerAutorizationInterceptor: Interceptor = Interceptor { chain ->
        var request: Request = chain.request()
        val headers = request.headers().newBuilder().add("Authorization","Bearer "+ BASE_TOKEN).build()
        request = request.newBuilder().headers(headers).build()
        chain.proceed(request)
    }

    var okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(headerAutorizationInterceptor)
        .build()


    val instance: ApiManager by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        retrofit.create(ApiManager::class.java)
    }


    fun getCurrentUsers(login: String,callback: Callback<CurrentUserModel>){
        val userCall = instance.getCurrentUser(login)
        userCall.enqueue(callback)
    }

    fun getCurrentContacts(login: String,callback: Callback<ContactsViewModel>){
        val userCall = instance.getCurrentContacts(login)
        userCall.enqueue(callback)
    }

    fun getContacts(callback: Callback<ArrayList<ContactsDetailsModel>>){
        val userCall = instance.getContacts()
        userCall.enqueue(callback)
    }
}