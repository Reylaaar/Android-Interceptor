package com.example.androidassestment.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.cysion.wedialog.WeDialog
import com.example.androidassestment.Constants
import com.example.androidassestment.Globals
import com.example.androidassestment.R
import com.example.androidassestment.SharedPref
import com.example.androidassestment.api.RetrofitClient
import com.example.androidassestment.databinding.ActivityContactDetailsBinding
import com.example.androidassestment.models.ContactsViewModel
import com.mrntlu.toastie.Toastie
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.runOnUiThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactDetailsBinding
    private var getLogin: String? = ""

    private lateinit var profileImage: CircleImageView
    private lateinit var name: TextView
    private lateinit var followers: TextView
    private lateinit var following: TextView
    private lateinit var loginName: TextView
    private lateinit var profileLink: TextView
    private lateinit var gistsLink: TextView
    private lateinit var reposLink: TextView
    private lateinit var organizationsUrl: TextView
    private lateinit var eventsLink: TextView
    private lateinit var htmlLink: TextView
    private lateinit var userType: TextView
    private lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPref = SharedPref(this)
        if(sharedPref.loadNightModeState()){
            setTheme(R.style.darkTheme)
        }else{
            setTheme(R.style.Theme_AndroidAssestment)
        }

        super.onCreate(savedInstanceState)
        binding = ActivityContactDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        profileImage = binding.root.findViewById(R.id.contact_image)
        name = binding.root.findViewById(R.id.contact_name)
        followers = binding.root.findViewById(R.id.contact_followers)
        following = binding.root.findViewById(R.id.contact_following)
        loginName = binding.root.findViewById(R.id.contact_login)
        eventsLink = binding.root.findViewById(R.id.contact_event)
        htmlLink = binding.root.findViewById(R.id.contact_html)
        userType = binding.root.findViewById(R.id.contact_type)
        profileLink = binding.root.findViewById(R.id.contact_profile)
        gistsLink = binding.root.findViewById(R.id.contact_gist)
        reposLink = binding.root.findViewById(R.id.contact_repo)
        organizationsUrl = binding.root.findViewById(R.id.contact_org)


        if(intent != null){
            getLogin = intent.getStringExtra(Constants.EXTRA_LOGIN_NAME)
            loadData(getLogin!!)
        }




    }

    private fun loadData(login: String){
        RetrofitClient.getCurrentContacts(login, object : Callback<ContactsViewModel> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<ContactsViewModel>, response: Response<ContactsViewModel>) {
                val responseModel = response.body()

                when{
                    response.isSuccessful && responseModel != null -> {

                        Globals.currentContactsView = responseModel
                        Glide.with(this@ContactDetailsActivity).load(Globals.currentContactsView.avatar_url).into(profileImage)
                        name.text = Globals.currentContactsView.name
                        followers.text = "Followers: ${Globals.currentContactsView.followers}"
                        following.text = "Following: ${Globals.currentContactsView.following}"
                        userType.text = Globals.currentContactsView.type
                        loginName.text = Globals.currentContactsView.login
                        eventsLink.text = Globals.currentContactsView.events_url
                        htmlLink.text = Globals.currentContactsView.html_url
                        profileLink.text = Globals.currentContactsView.url
                        gistsLink.text = Globals.currentContactsView.gists_url
                        reposLink.text = Globals.currentContactsView.repos_url
                        organizationsUrl.text = Globals.currentContactsView.organizations_url

                    }
                }
            }
            override fun onFailure(call: Call<ContactsViewModel>, t: Throwable) {
                WeDialog.dismiss()
                Toastie.error(this@ContactDetailsActivity,resources.getString(R.string.error_getting_user), Toast.LENGTH_SHORT).show()
            }
        })
    }
}