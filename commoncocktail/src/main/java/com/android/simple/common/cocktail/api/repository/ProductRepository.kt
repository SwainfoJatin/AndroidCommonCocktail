package com.android.simple.common.cocktail.api.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.android.simple.common.cocktail.api.ApiServices
import com.android.simple.common.cocktail.utility.AppUtitlty
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.File

class ProductRepository(private val apiServices: ApiServices) {

    private val infoData = MutableLiveData<ResponseBody>()
    private val feedbackData = MutableLiveData<ResponseBody>()

    val info :LiveData<ResponseBody>
        get() = infoData

    val feedback :LiveData<ResponseBody>
        get() = feedbackData




    suspend fun getInfo(tsHeader:String)
    {
        val result = apiServices.getInfo(AppUtitlty.PACKAGE_NAME!!,AppUtitlty.VERSION_CODE!!,tsHeader)

        if(result?.body() != null)
        {

            infoData.postValue(result?.body())
        }
    }

    suspend fun SendFeedBack(tsHeader:String,fb_title:String,fb_desc:String,file:File)
    {

        val reqFile = RequestBody.create(
            MediaType.parse("image/jpeg"),
            file
        )
        val desc = RequestBody.create(
            MediaType.parse("text/plain"),
            fb_desc
        )
        val title = RequestBody.create(
            MediaType.parse("text/plain"),
            fb_title
        )

        val result = apiServices.sendFeedBack(AppUtitlty.PACKAGE_NAME!!,AppUtitlty.VERSION_CODE!!,tsHeader,title,desc,reqFile)
       // val result = apiServices.sendFeedBack(AppUtitlty.PACKAGE_NAME!!,AppUtitlty.VERSION_CODE!!,tsHeader,fb_title,fb_desc,file)

        if(result?.body() != null)
        {

            feedbackData.postValue(result?.body())
        }
    }
    suspend fun SendFeedBackWithoutFile(tsHeader:String,fb_title:String,fb_desc:String)
    {
        val result = apiServices.sendFeedBackNotFile(AppUtitlty.PACKAGE_NAME!!,AppUtitlty.VERSION_CODE!!,tsHeader,fb_title,fb_desc)

        if(result?.body() != null)
        {

            feedbackData.postValue(result?.body())
        }
    }

}