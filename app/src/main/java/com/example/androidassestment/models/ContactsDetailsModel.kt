package com.example.androidassestment.models

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "all_contacts")
data class  ContactsDetailsModel (
    @PrimaryKey val id: Int,
    val login: String?,
    val avatar_url: String?,
    val url: String?,
    val html_url:String?,
    val followers_url: String?,
    val gists_url: String?,
    val starred_url: String?,
    val subscriptions_url: String?,
    val organizations_url: String?,
    val repos_url: String?,
    val events_url: String?,
    val received_events_url: String?,
    val type: String?,
    val site_admin: Boolean)
    :Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(login)
        parcel.writeString(avatar_url)
        parcel.writeString(url)
        parcel.writeString(html_url)
        parcel.writeString(followers_url)
        parcel.writeString(gists_url)
        parcel.writeString(starred_url)
        parcel.writeString(subscriptions_url)
        parcel.writeString(organizations_url)
        parcel.writeString(repos_url)
        parcel.writeString(events_url)
        parcel.writeString(received_events_url)
        parcel.writeString(type)
        parcel.writeByte(if (site_admin) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ContactsDetailsModel> {
        override fun createFromParcel(parcel: Parcel): ContactsDetailsModel {
            return ContactsDetailsModel(parcel)
        }

        override fun newArray(size: Int): Array<ContactsDetailsModel?> {
            return arrayOfNulls(size)
        }
    }
}
