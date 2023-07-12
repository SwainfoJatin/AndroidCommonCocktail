package com.android.simple.common.cocktail.utility

import android.Manifest.permission
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.android.simple.common.cocktail.api.models.InfoData
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


class AppUtitlty {
    companion object{
        var infoData: InfoData? = null
        var PACKAGE_NAME:String? = null
        var VERSION_CODE:String? = null
        var SECRETE_KEY:String? = null

        var isInfoData = false

        fun GetCurrentDate():String
        {
            val simpleDate = SimpleDateFormat("dd/MM/yyyy")
            return simpleDate.format(Date())
        }

        fun GetCureentTimeStamp():String
        {
            return Date().time.toString()
        }

         fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager?.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

         fun requestPermission(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(activity, arrayOf(permission.ACCESS_MEDIA_LOCATION, permission.READ_MEDIA_IMAGES), 100)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(activity, arrayOf(permission.READ_EXTERNAL_STORAGE, permission.WRITE_EXTERNAL_STORAGE, permission.ACCESS_MEDIA_LOCATION), 100)
            } else {
                ActivityCompat.requestPermissions(activity, arrayOf(permission.READ_EXTERNAL_STORAGE, permission.WRITE_EXTERNAL_STORAGE), 100)
            }
        }

        fun checkSelfPermission(activity: Activity?, permissionName: String?): Boolean {
            return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                activity!!,
                permissionName!!
            )
        }

         fun checkPermission(activity: Activity): Boolean {
            var isPermissionGranted = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (checkSelfPermission(activity, permission.ACCESS_MEDIA_LOCATION) &&
                    checkSelfPermission(activity, permission.READ_MEDIA_IMAGES)
                ) {
                    isPermissionGranted = true
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (checkSelfPermission(
                        activity,
                        permission.READ_EXTERNAL_STORAGE
                    ) && checkSelfPermission(
                        activity,
                        permission.WRITE_EXTERNAL_STORAGE
                    ) && checkSelfPermission(activity, permission.ACCESS_MEDIA_LOCATION)
                ) {
                    isPermissionGranted = true
                }
            } else {
                if (checkSelfPermission(
                        activity,
                        permission.READ_EXTERNAL_STORAGE
                    ) && checkSelfPermission(activity, permission.WRITE_EXTERNAL_STORAGE)
                ) {
                    isPermissionGranted = true
                }
            }
            return isPermissionGranted
        }

        fun getPathFromURI(activity: Activity,contentUri: Uri?): String? {
            var res: String? = null
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor? = activity.contentResolver.query(contentUri!!, proj, null, null, null)
            if (cursor!!.moveToFirst()) {
                val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                res = cursor.getString(column_index)
            }
            cursor.close()
            return res
        }

        fun convertBitmapToFile(context: Context, bitmap: Bitmap): File {
            val wrapper = ContextWrapper(context)
            var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
            file = File(file,"${UUID.randomUUID()}.png")
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG,25,stream)
            stream.flush()
            stream.close()
            return file
        }





        fun getActivityName(activity: Class<*>):String
        {
            return activity.canonicalName
        }

        fun getClass(str: String):Class<*>
        {
            return Class.forName(str)
        }


    }
}