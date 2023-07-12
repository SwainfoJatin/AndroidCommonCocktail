package com.android.simple.common.cocktail.inappreview

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.android.simple.common.cocktail.utility.AppUtitlty


import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.tasks.Task

/*import android.app.Activity
import com.google.android.play.core.review.ReviewInfo

import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.tasks.Task*/


class InAppReview(val activity: Activity): Activity() {


   //  var reviewInfo: ReviewInfo? = null
    init {

       // CheckReview(ReviewManagerFactory.create(activity))
      }

    fun CheckReview(reviewManager: ReviewManager)
    {
        val request: Task<ReviewInfo> = reviewManager!!.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
                var  reviewInfo = task.getResult()
                val flow = reviewManager.launchReviewFlow(activity, reviewInfo)
                flow.addOnCompleteListener { task1: Task<Void?>? ->
                    val url = "https://play.google.com/store/apps/details?id="+ AppUtitlty.PACKAGE_NAME
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(url)
                    activity.startActivity(i)

                }



            } else {
                // There was some problem, continue regardless of the result.
            }
        }
    }
}