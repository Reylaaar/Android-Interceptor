package com.example.androidassestment.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.room.Room
import com.example.androidassestment.database.RoomDatabase
import com.cysion.wedialog.WeDialog
import com.example.androidassestment.Globals
import com.example.androidassestment.R
import com.example.androidassestment.api.RetrofitClient
import com.example.androidassestment.databinding.ActivityLoginBinding
import com.example.androidassestment.models.ContactsDetailsModel
import com.example.androidassestment.models.ContactsModel
import com.example.androidassestment.models.CurrentUserModel
import com.google.android.material.textfield.TextInputLayout
import com.mrntlu.toastie.Toastie
import org.jetbrains.anko.alert
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import retrofit2.Call

import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var roomDatabase: RoomDatabase

    private lateinit var login: Button
    private lateinit var username: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        login = binding.root.findViewById(R.id.login)
        username = binding.root.find(R.id.username)

        roomDatabase = Room.databaseBuilder(applicationContext,RoomDatabase::class.java,"maindb").allowMainThreadQueries().build()
        val currentUserModel: CurrentUserModel = roomDatabase.dbDao().getCurrentUser()

        if(roomDatabase.dbDao().checkIfCurrentUserExist() != 0){
            WeDialog.loading(this)
            username.editText?.setText(currentUserModel.login)
            loginUser(currentUserModel.login)
        }else{
            roomDatabase.dbDao().deleteAllContacts()
            roomDatabase.dbDao().deleteCurrentUser()
        }

        login.setOnClickListener {
            WeDialog.loading(this)
            val username = username.editText!!.text.toString().trim()
            loginUser(username)
        }
    }


    private fun loginUser(login: String){

        RetrofitClient.getCurrentUsers(login, object : Callback<CurrentUserModel>{
            override fun onResponse(call: Call<CurrentUserModel>, response: Response<CurrentUserModel> ) {
            val responseModel = response.body()

                when{
                    response.isSuccessful && responseModel != null -> {

                        Globals.currentUserDetails = responseModel
                        insertLocalData()
                        startActivity<DashboardActivity>()
                        username.editText!!.setText("")
                        WeDialog.dismiss()
                    }
                }
            }
            override fun onFailure(call: Call<CurrentUserModel>, t: Throwable) {
                WeDialog.dismiss()
                Toastie.error(this@LoginActivity,resources.getString(R.string.error_getting_user),Toast.LENGTH_SHORT).show()
                Log.d("response","$call \n $t")
            }
        })
    }

    private fun insertLocalData(){
        if(roomDatabase.dbDao().checkIfCurrentUserExist() != 0){

        }else{
            roomDatabase.dbDao().insertCurrentUser(Globals.currentUserDetails)
        }
    }
    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStop() {
        super.onStop()
    }
}