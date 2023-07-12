package com.android.simple.common.cocktail.api

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import java.io.File

interface ApiServices {

    // @Headers("pn:"+BuildConfig.APPLICATION_ID, "av:"+BuildConfig.VERSION_NAME)
    @POST("info")
    suspend fun getInfo(@Header("pn")pName:String,@Header("av")avName:String,@Header("ts") tsheader:String):Response<ResponseBody>

    // feedback
    @Multipart
    // @FormUrlEncoded
    @POST("feedback")
    // suspend fun sendFeedBack(@Header("pn")pName:String,@Header("av")avName:String,@Header("ts") tsheader:String,@Field("fb_title") title:String,@Field("fb_desc") desc:String,@Field("fb_image") file: File):Response<ResponseBody>
    suspend fun sendFeedBack(@Header("pn")pName:String, @Header("av")avName:String, @Header("ts") tsheader:String, @Part("fb_title") title: RequestBody, @Part("fb_desc") desc: RequestBody, @Part("fb_image") file: RequestBody):Response<ResponseBody>
    // suspend fun sendFeedBack(@Header("pn")pName:String,@Header("av")avName:String,@Header("ts") tsheader:String,@Part body: MultipartBody.Part):Response<ResponseBody>


    @FormUrlEncoded
    @POST("feedback")
    suspend fun sendFeedBackNotFile(@Header("pn")pName:String,@Header("av")avName:String,@Header("ts") tsheader:String,@Field("fb_title") title:String,@Field("fb_desc") desc:String):Response<ResponseBody>

}