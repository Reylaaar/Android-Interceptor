package com.example.androidassestment.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.cysion.wedialog.WeDialog
import com.example.androidassestment.Globals
import com.example.androidassestment.R
import com.example.androidassestment.activities.DashboardActivity
import com.example.androidassestment.api.RetrofitClient
import com.example.androidassestment.models.ContactsDetailsModel
import com.example.androidassestment.models.ContactsViewModel
import com.example.androidassestment.models.CurrentUserModel
import com.mrntlu.toastie.Toastie
import de.hdodenhof.circleimageview.CircleImageView
import eu.inloop.simplerecycleradapter.SettableViewHolder
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.warn
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ContactsViewHolder(context: Context, parent: ViewGroup,listener: OnItemClickListener) :
    SettableViewHolder<ContactsDetailsModel>(context, R.layout.contact_item_layout_placeholder,parent), AnkoLogger {

    private val contactPhoto: CircleImageView = itemView.findViewById(R.id.contact_image)
    private val contactName: TextView = itemView.findViewById(R.id.contact_friendly_name)
    private val contactType: TextView = itemView.findViewById(R.id.contact_type)
    private val contactLink: TextView = itemView.findViewById(R.id.contact_link)
    private val contactLogin: TextView = itemView.findViewById(R.id.contact_login)
    private val contactFollowers: TextView = itemView.findViewById(R.id.contact_followers)
    private val contactFollowing: TextView = itemView.findViewById(R.id.contact_following)
    private val contactBase: LinearLayout = itemView.findViewById(R.id.contact_view_base)


    interface OnItemClickListener{
        fun OnItemClickListener(position: Int)
    }

    init {
        itemView.setOnClickListener {
            listener.OnItemClickListener(adapterPosition)
        }
    }
    override fun setData(contact: ContactsDetailsModel) {
        warn { "setData for $contact" }
        val context = itemView.context

        if(Globals.currentUserDetails.login != contact.login){
            Glide.with(context).load(contact.avatar_url).into(contactPhoto)
            contactLink.text = contact.url
            contactType.text = contact.type


            getLoginName(contact.login.toString(),context)
        }else{
            contactBase.visibility = View.GONE
        }
    }

    private fun getLoginName(getLogin: String,context: Context){

        RetrofitClient.getCurrentContacts(getLogin, object : Callback<ContactsViewModel> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<ContactsViewModel>, response: Response<ContactsViewModel>) {
                val responseModel = response.body()

                when{
                    response.isSuccessful && responseModel != null -> {

                        Globals.currentContactsView = responseModel

                        context.runOnUiThread {

                            if(Globals.currentContactsView.name != null){
                                contactName.text = Globals.currentContactsView.name
                            }else{
                                contactName.text = Globals.currentContactsView.login
                            }

                            contactLogin.text = Globals.currentContactsView.login
                            contactFollowers.text = "${context.resources.getString(R.string.followers)} : ${Globals.currentContactsView.followers}"
                            contactFollowing.text = "${context.resources.getString(R.string.following)} : ${Globals.currentContactsView.following}"
                        }

                    }
                }
            }
            override fun onFailure(call: Call<ContactsViewModel>, t: Throwable) {
                WeDialog.dismiss()
                Toastie.error(context,context.resources.getString(R.string.error_getting_user), Toast.LENGTH_SHORT).show()
            }
        })
    }
}
