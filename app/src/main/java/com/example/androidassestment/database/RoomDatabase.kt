package com.example.androidassestment.database
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androidassestment.dao.DBDao
import com.example.androidassestment.models.ContactsDetailsModel
import com.example.androidassestment.models.CurrentUserModel

@Database(entities = [CurrentUserModel::class,ContactsDetailsModel::class], version = 1)
abstract class RoomDatabase: RoomDatabase() {
    abstract fun dbDao(): DBDao
}