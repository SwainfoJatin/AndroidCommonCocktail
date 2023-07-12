package com.android.simple.common.cocktail.utility

import android.app.Activity
import com.android.simple.common.cocktail.interfaces.InAppUpdateListner
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.UpdateAvailability

class InAppUpdateNew {



    fun checkUpdate(activity:Activity,updateListner: InAppUpdateListner)
    {
        var appUpdateManager = AppUpdateManagerFactory.create(activity)
        appUpdateManager.appUpdateInfo.addOnSuccessListener {
           if(it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE )
           {
               updateListner.onUpdateAvailable(it)
           }
           else
           {
               updateListner.onAlreadyUpdated()
           }
       }


    }

}