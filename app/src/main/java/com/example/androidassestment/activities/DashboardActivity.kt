package com.example.androidassestment.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.bumptech.glide.Glide
import com.cysion.wedialog.WeDialog
import com.example.androidassestment.Globals
import com.example.androidassestment.R
import com.example.androidassestment.api.RetrofitClient
import com.example.androidassestment.database.RoomDatabase
import com.example.androidassestment.databinding.ActivityDashboardBinding
import com.example.androidassestment.models.ContactsDetailsModel
import com.example.androidassestment.models.ContactsModel
import com.example.androidassestment.models.CurrentUserModel
import com.facebook.shimmer.ShimmerFrameLayout
import com.mrntlu.toastie.Toastie
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.androidassestment.Constants
import com.example.androidassestment.SharedPref
import com.example.androidassestment.views.ContactsViewHolder
import eu.inloop.simplerecycleradapter.SettableViewHolder
import eu.inloop.simplerecycleradapter.SimpleRecyclerAdapter
import kotlinx.coroutines.newSingleThreadContext
import org.jetbrains.anko.alert

class DashboardActivity : AppCompatActivity() {

    private lateinit var roomDatabase: RoomDatabase
    private lateinit var binding: ActivityDashboardBinding

    private lateinit var profile_image: CircleImageView
    private lateinit var shimmerFrame: ShimmerFrameLayout
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var contact_list: RecyclerView
    private lateinit var layout_theme: SwitchCompat
    private lateinit var sharedPref: SharedPref

    private lateinit var adapter: SimpleRecyclerAdapter<ContactsDetailsModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPref = SharedPref(this)
        if(sharedPref.loadNightModeState()){
            setTheme(R.style.darkTheme)
        }else{
            setTheme(R.style.Theme_AndroidAssestment)
        }
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        profile_image = binding.root.findViewById(R.id.profile_image)
        shimmerFrame = binding.root.findViewById(R.id.shimmer_view_container)
        swipeRefresh = binding.root.findViewById(R.id.swipeRefreshContainer)
        contact_list = binding.root.findViewById(R.id.contact_list)
        layout_theme = binding.root.findViewById(R.id.layout_theme)

        roomDatabase = Room.databaseBuilder(applicationContext,RoomDatabase::class.java,"maindb").allowMainThreadQueries().build()

        setUpFunctions()
        setUpListView()
        getContacts()
    }

    private fun getContacts(){
        shimmerFrame.visibility = View.VISIBLE

        Handler(Looper.getMainLooper()).postDelayed({
            runOnUiThread {
                shimmerFrame.animate().alpha(0.0f)
                shimmerFrame.visibility = View.GONE
                contact_list.visibility = View.VISIBLE
                gerUserContacts()
            }
        },1000)
    }

    private fun setUpFunctions(){

        layout_theme.isChecked = sharedPref.loadNightModeState()
        Glide.with(this).load(Globals.currentUserDetails.avatar_url).into(profile_image)

        profile_image.setOnClickListener{

        }

        swipeRefresh.setOnRefreshListener {
            gerUserContacts()
            swipeRefresh.isRefreshing = false
        }

        layout_theme.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                sharedPref.setNightModeState(true)
                this.recreate()
            }else{
                sharedPref.setNightModeState(false)
                this.recreate()
            }
        }
    }

    private fun setUpListView(){

        adapter = SimpleRecyclerAdapter<ContactsDetailsModel>(null,
        object : SimpleRecyclerAdapter.CreateViewHolder<ContactsDetailsModel>(),ContactsViewHolder.OnItemClickListener{
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int, ): SettableViewHolder<ContactsDetailsModel> {
                    return ContactsViewHolder(this@DashboardActivity,parent,this)
            }

            override fun OnItemClickListener(position: Int) {
                val data= adapter.items[position]

                startActivity<ContactDetailsActivity>(
                    Constants.EXTRA_LOGIN_NAME to data.login
                )
            }

        })

        contact_list.adapter = adapter
        contact_list.itemAnimator!!.changeDuration = 0
        contact_list.layoutManager = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.VERTICAL
            (contact_list)
        }
    }

    private  fun gerUserContacts(){

        RetrofitClient.getContacts(object : Callback<ArrayList<ContactsDetailsModel>> {
            override fun onResponse(call: Call<ArrayList<ContactsDetailsModel>>, response: Response<ArrayList<ContactsDetailsModel>>, ) {
                val responseModel = response.body()

                when{
                    response.isSuccessful && responseModel != null ->{

                        if(!Globals.contactList.contains(responseModel[1])){
                            Globals.contactList = responseModel
                        }
                        addAdapter(Globals.contactList)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<ContactsDetailsModel>>, t: Throwable) {
                Toastie.error(this@DashboardActivity,resources.getString(R.string.error_getting_contact_details), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addAdapter(contacts: ArrayList<ContactsDetailsModel>){
        Handler(Looper.getMainLooper()).postDelayed({
            runOnUiThread {
                adapter.clear()
                adapter.addItems(contacts)
                adapter.notifyDataSetChanged()
            }
        },0)
    }



    override fun onBackPressed() {

        alert("Do you want to logout?","Hi, ${Globals.currentUserDetails.login}") {
            positiveButton(R.string.logout) {
                    dialog -> dialog.dismiss()
                roomDatabase.clearAllTables()
                super.onBackPressed()
            }
            negativeButton(R.string.cancel) {dialog ->  dialog.cancel() }
        }.show()
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