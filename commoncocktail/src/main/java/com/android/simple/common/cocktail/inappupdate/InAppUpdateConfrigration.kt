package com.android.simple.common.cocktail.inappupdate

import android.app.Activity
import android.os.Handler
import android.util.Log

import com.android.simple.common.cocktail.utility.AppUtitlty


class InAppUpdateConfrigration(var activity: Activity) {


    init {

        Handler().postDelayed(Runnable { //check delay show ad splash

            //  Log.e("@@@@@","==InAppUpdateConfrigration=======> "+ AppUtitlty.infoData.toString())

            if(AppUtitlty.isInfoData){
                if(AppUtitlty.infoData!!.info.latest_version.toDouble() > AppUtitlty.VERSION_CODE!!.toDouble())
                {
                    InAppUpdate(activity, AppUtitlty.infoData!!.info.update_type.toInt(), AppUtitlty.infoData!!.info.dialog_type.toInt())
                }
            }
        }, 3000)




    }

}