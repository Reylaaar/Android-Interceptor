package com.example.androidassestment.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.androidassestment.models.CurrentUserModel
import com.example.androidassestment.models.ContactsDetailsModel

@Dao
interface DBDao {

    @Query("SELECT * FROM current_user")
    fun getCurrentUser(): CurrentUserModel

    @Query("SELECT count(*) FROM current_user")
    fun checkIfCurrentUserExist():Int

    @Query("SELECT count(*) FROM all_contacts")
    fun checkIfCurrentContactsExist():Int

    @Query("SELECT * FROM all_contacts")
    fun getAllContacts(): List<ContactsDetailsModel>

    @Insert
    fun insertCurrentUser(vararg currentUser: CurrentUserModel)

    @Insert
    fun insertAllContacts(vararg allContacts: ContactsDetailsModel)

    @Query("DELETE FROM current_user")
    fun deleteCurrentUser()

    @Query("DELETE FROM all_contacts")
    fun deleteAllContacts()
}