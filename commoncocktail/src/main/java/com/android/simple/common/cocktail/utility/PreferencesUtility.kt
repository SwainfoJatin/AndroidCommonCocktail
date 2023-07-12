package com.android.simple.common.cocktail.utility

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.android.simple.common.cocktail.inappupdate.UpdateConstant.Companion.PREF_ONCE_IN_DAY
import com.android.simple.common.cocktail.inappupdate.UpdateConstant.Companion.PREF_ONCE_IN_LIFE

class PreferencesUtility {
    companion object{
        fun GetOnceInDayPref(context:Context):String
        {
            val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return sp.getString(PREF_ONCE_IN_DAY, "NoDate")!!
        }

        fun SetOnceInDayPref(context:Context,date:String)
        {
            val sp = PreferenceManager.getDefaultSharedPreferences(context)
            sp.edit().putString(PREF_ONCE_IN_DAY, date).apply()
        }

        fun GetOnceInLifePref(context:Context):Boolean
        {
            val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return sp.getBoolean(PREF_ONCE_IN_LIFE, false)!!
        }

        fun SetOnceInLifePref(context:Context,value:Boolean)
        {
            val sp = PreferenceManager.getDefaultSharedPreferences(context)
            sp.edit().putBoolean(PREF_ONCE_IN_LIFE, value).apply()
        }


    }
}