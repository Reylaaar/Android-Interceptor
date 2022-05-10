package com.example.androidassestment

import android.content.Context
import android.content.SharedPreferences

class SharedPref(context: Context) {
    var mySharedPref: SharedPreferences = context.getSharedPreferences("NightMode", Context.MODE_PRIVATE)

    fun setNightModeState(state: Boolean?) {
        val editor = mySharedPref.edit()
        editor.putBoolean("NightMode", state!!)
        editor.apply()
    }

    fun loadNightModeState(): Boolean {
        return mySharedPref.getBoolean("NightMode", false)
    }

}