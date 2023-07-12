package com.android.simple.common.cocktail.interfaces

import com.google.android.play.core.appupdate.AppUpdateInfo

interface InAppUpdateListner {

    fun  onUpdateAvailable(info:AppUpdateInfo)
    fun  onAlreadyUpdated()
}