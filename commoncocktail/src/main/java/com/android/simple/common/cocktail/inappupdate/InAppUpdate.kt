package com.android.simple.common.cocktail.inappupdate

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.simple.common.cocktail.R
import com.android.simple.common.cocktail.inappupdate.UpdateConstant.Companion.EVERY_TIME
import com.android.simple.common.cocktail.inappupdate.UpdateConstant.Companion.ONCE_IN_DAY
import com.android.simple.common.cocktail.inappupdate.UpdateConstant.Companion.ONCE_IN_LIFE

import com.android.simple.common.cocktail.inappupdate.UpdateConstant.Companion.TYPE_FLEXIBLE
import com.android.simple.common.cocktail.inappupdate.UpdateConstant.Companion.TYPE_IMMEDIATE
import com.android.simple.common.cocktail.utility.AppUtitlty.Companion.GetCurrentDate
import com.android.simple.common.cocktail.utility.PreferencesUtility

import com.android.simple.common.cocktail.utility.PreferencesUtility.Companion.SetOnceInDayPref
import com.android.simple.common.cocktail.utility.PreferencesUtility.Companion.SetOnceInLifePref
import com.google.android.material.snackbar.Snackbar

import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability

class InAppUpdate(var activity: Activity,var type:Int,var showUpdate:Int) : InstallStateUpdatedListener {

    private var appUpdateManager: AppUpdateManager
    private val MY_REQUEST_CODE = 500
    private var parentActivity: Activity = activity

    private var currentType = AppUpdateType.FLEXIBLE

    init {





        appUpdateManager = AppUpdateManagerFactory.create(parentActivity)
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->





            // Check if update is available
            if (info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {

                if(type != 1) {
                    if (DailogShowTime(showUpdate, activity)) {
                        SetPrefernceValue(activity)
                        when(type)
                        {
                            TYPE_FLEXIBLE ->{
                                startUpdate(info, AppUpdateType.FLEXIBLE)
                            }
                            TYPE_IMMEDIATE -> {
                                startUpdate(info, AppUpdateType.IMMEDIATE)
                            }
                        }

                    }
                }



            } else {
                // UPDATE IS NOT AVAILABLE
            }
        }
        appUpdateManager.registerListener(this)
    }


    private fun startUpdate(info: AppUpdateInfo, type: Int) {
        appUpdateManager.startUpdateFlowForResult(info, type, parentActivity, MY_REQUEST_CODE)
        currentType = type
    }

    fun onResume() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            if (currentType == AppUpdateType.FLEXIBLE) {
                // If the update is downloaded but not installed, notify the user to complete the update.
                if (info.installStatus() == InstallStatus.DOWNLOADED)
                    flexibleUpdateDownloadCompleted()
            } else if (currentType == AppUpdateType.IMMEDIATE) {
                // for AppUpdateType.IMMEDIATE only, already executing updater
                if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    startUpdate(info, AppUpdateType.IMMEDIATE)
                }
            }
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != AppCompatActivity.RESULT_OK) {
                // If the update is cancelled or fails, you can request to start the update again.
                Log.e("ERROR", "Update flow failed! Result code: $resultCode")
            }
        }
    }

    private fun flexibleUpdateDownloadCompleted() {


        //  appUpdateManager.completeUpdate()

        Snackbar.make(
            parentActivity.findViewById(R.id.rootLayout),
            "An update has just been downloaded.",
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction("RESTART") { appUpdateManager.completeUpdate() }
            setActionTextColor(Color.WHITE)
            show()
        }
    }

    fun onDestroy() {
        appUpdateManager.unregisterListener(this)
    }

    override fun onStateUpdate(state: InstallState) {
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            flexibleUpdateDownloadCompleted()
        }
    }

    fun DailogShowTime(showTime:Int,context: Context):Boolean
    {

        return when(showTime)
        {
            EVERY_TIME -> true
            ONCE_IN_DAY -> {

                if(!GetShowOnceDayStatus(context))
                {
                    ShowDailog("Dialog will popup on Next Day")
                }
                GetShowOnceDayStatus(context)
            }
            ONCE_IN_LIFE -> {
                Log.e("@@@@@","=====GetShowOnceLifeStatus=====> "+GetShowOnceLifeStatus(context))
                GetShowOnceLifeStatus(context)
            }
            else -> false

        }

    }

    fun GetShowOnceDayStatus(context: Context):Boolean
    {

        return !GetCurrentDate().equals(PreferencesUtility.GetOnceInDayPref(context))
    }

    fun GetShowOnceLifeStatus(context: Context):Boolean
    {
        return !PreferencesUtility.GetOnceInLifePref(context)
    }

    fun SetPrefernceValue(context: Context)
    {
        SetOnceInDayPref(context,GetCurrentDate())
        SetOnceInLifePref(context,true)
    }

    fun ShowDailog( str:String)
    {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("InApp")
        builder.setMessage(str)

//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            Toast.makeText(activity,
                android.R.string.yes, Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            Toast.makeText(activity,
                android.R.string.no, Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }


        builder.show()
    }

}