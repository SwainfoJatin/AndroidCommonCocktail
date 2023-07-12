package com.android.simple.common.cocktail.inappreview

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.android.simple.common.cocktail.databinding.LayoutExitDailogBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.play.core.review.ReviewManagerFactory


class RatingDailog(var activity: Activity): BottomSheetDialog(activity) {
    lateinit var binding: LayoutExitDailogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutExitDailogBinding.inflate(layoutInflater)
        setContentView(binding.root)





         binding.BtnGood.setOnClickListener({

             Log.e("@@@@@","====BtnGood Click====> ")

             var inappreview  = InAppReview(activity)
             inappreview.CheckReview(ReviewManagerFactory.create(activity))

         })
         binding.BtnNotReally.setOnClickListener({
              this@RatingDailog.dismiss()
           activity.startActivity(Intent(activity, FeedBackDialog::class.java))

         })

    }

   

}