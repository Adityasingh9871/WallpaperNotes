package com.example.wallpapernotes.data
import com.google.gson.Gson

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.wallpapernotes.Note

import com.google.gson.reflect.TypeToken
import okhttp3.internal.concurrent.TaskRunner.Companion.logger

//data class Note(val title:String,val desc:String,val date:String)
class dataApi (){

    fun getdata(context:Context): MutableList<Note>?{
        val preferences:SharedPreferences=context.getSharedPreferences("data",Context.MODE_PRIVATE)
        val gson=Gson()
        val jsonText = preferences.getString("ListofNotes", null)
        Log.d("retunr ", jsonText.toString())
        return if(jsonText != null) { gson.fromJson(jsonText,object : TypeToken<MutableList<Note>>() {}.type) }
        else{
            return null
        }


    }

    fun updatedata(data:MutableList<Note>?,context: Context)
    {
        val preferences:SharedPreferences=context.getSharedPreferences("data",Context.MODE_PRIVATE)
        val prefsEditor=preferences.edit()
        val gson=Gson()

        val jsonText=gson.toJson(data)
        prefsEditor.putString("ListofNotes",jsonText)
        prefsEditor.apply()
        Log.d("update", jsonText)

    }


}