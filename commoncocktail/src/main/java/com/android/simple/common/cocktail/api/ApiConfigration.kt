package com.android.simple.common.cocktail.api

import android.content.Context
import android.content.Intent
import android.net.Uri

import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.LiveData

import com.android.simple.common.cocktail.api.encryption.CryptLib
import com.android.simple.common.cocktail.api.models.InfoData
import com.android.simple.common.cocktail.api.repository.ProductRepository
import com.android.simple.common.cocktail.utility.AppUtitlty
import com.android.simple.common.cocktail.utility.AppUtitlty.Companion.GetCureentTimeStamp
import com.android.simple.common.cocktail.utility.AppUtitlty.Companion.infoData
import com.android.simple.common.cocktail.utility.AppUtitlty.Companion.isInfoData
import com.android.simple.common.cocktail.utility.AppUtitlty.Companion.isNetworkAvailable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import java.lang.reflect.Type
import java.util.*

class ApiConfigration(val context: Context,val PACKAGE_NAME:String,val VERSION_CODE:String,val SECRETE_KEY:String) {


    var repository: ProductRepository



    init {

        AppUtitlty.PACKAGE_NAME = PACKAGE_NAME
        AppUtitlty.VERSION_CODE = VERSION_CODE
        AppUtitlty.SECRETE_KEY = SECRETE_KEY

        var cryptLib = CryptLib()
        val apiServices = RetrofitHelper.getInstance().create(ApiServices::class.java)
        repository = ProductRepository(apiServices)

        if (isNetworkAvailable(context)) {
            GlobalScope.launch(Dispatchers.IO) {
                val header: String = "" + cryptLib.encryptPlainTextWithRandomIV(
                    GetCureentTimeStamp(),
                    SECRETE_KEY
                )
                repository.getInfo(header)
            }
            getApiCall().observeForever {
                try {
                    val str = "" + cryptLib.decryptCipherTextWithRandomIV(
                        it.string(),
                        SECRETE_KEY
                    )
                    val gson = Gson()
                    val userType: Type = object : TypeToken<InfoData>() {}.type
                    infoData = gson.fromJson(str, userType)

                    if (infoData!!.status_code != 0) {
                        isInfoData = true
                        if (!infoData!!.info.website_url.isEmpty()) {
                            launchWebView(infoData!!.info.website_url, context)
                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


    fun getApiCall(): LiveData<ResponseBody> {
        return repository.info
    }


    fun launchWebView(url: String, context: Context) {
        var uri = Uri.parse(url)
        var intent = CustomTabsIntent.Builder().build()
        intent.intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.launchUrl(context, uri)


    }
}

