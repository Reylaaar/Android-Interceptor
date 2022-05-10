package com.example.androidassestment

import com.example.androidassestment.models.ContactsDetailsModel
import com.example.androidassestment.models.ContactsModel
import com.example.androidassestment.models.ContactsViewModel
import com.example.androidassestment.models.CurrentUserModel

object Globals {

    var baseUrl = "https://api.github.com/"

    lateinit var currentUserDetails: CurrentUserModel

    var contactList:ArrayList<ContactsDetailsModel> = ArrayList()

     lateinit var currentContactsView: ContactsViewModel
}