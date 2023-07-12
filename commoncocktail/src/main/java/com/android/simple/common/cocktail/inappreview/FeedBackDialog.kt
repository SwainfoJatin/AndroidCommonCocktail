package com.android.simple.common.cocktail.inappreview

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager

import com.android.simple.common.cocktail.R
import com.android.simple.common.cocktail.api.ApiServices
import com.android.simple.common.cocktail.api.RetrofitHelper
import com.android.simple.common.cocktail.api.encryption.CryptLib
import com.android.simple.common.cocktail.api.models.FeedBackData
import com.android.simple.common.cocktail.api.repository.ProductRepository
import com.android.simple.common.cocktail.databinding.FeedbackLayoutBinding
import com.android.simple.common.cocktail.utility.AppUtitlty
import com.android.simple.common.cocktail.utility.AppUtitlty.Companion.SECRETE_KEY
import com.android.simple.common.cocktail.utility.AppUtitlty.Companion.checkPermission
import com.android.simple.common.cocktail.utility.AppUtitlty.Companion.requestPermission
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.lang.reflect.Type


class FeedBackDialog:AppCompatActivity(){


    lateinit var binding: FeedbackLayoutBinding

    lateinit var repository: ProductRepository
    var isFiile = false

    var file:File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FeedbackLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val apiServices = RetrofitHelper.getInstance().create(ApiServices::class.java)
        repository = ProductRepository(apiServices)

        binding.tagRecyler.layoutManager = GridLayoutManager(this,3)
       // binding.tagRecyler.layoutManager = StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        binding.tagRecyler.adapter = ReviewAdapter(this@FeedBackDialog,resources.getStringArray(R.array.Suggetion_List).asList(),object :OnSelectTag{
            override fun setSelectedTag(title: String) {
                binding.Edtitle.setText(title)
            }

        })

        binding.addImageContainer.setOnClickListener {
            if (checkPermission(this)) {
                val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
              startActivityForResult(gallery, 103)
            } else {
                requestPermission(this)
            }

        }

        binding.Edtitle.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(binding.Edtitle.text.toString().length > 99)
                {
                    Toast.makeText(this@FeedBackDialog,"You can enter Maximum 100 Charecters",Toast.LENGTH_SHORT).show()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding.description.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(binding.description.text.toString().length > 299)
                {
                    Toast.makeText(this@FeedBackDialog,"You can enter Maximum 300 Charecters",Toast.LENGTH_SHORT).show()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.addImageContainer.setOnClickListener(View.OnClickListener {
            if (!checkPermission(this@FeedBackDialog)) {
                Log.e("@@@@@","Btn Click=====if")
                requestPermission(this@FeedBackDialog)
            } else {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "image/*"
                startActivityForResult(
                    Intent.createChooser(intent, "Select Picture"),
                  103
                )
            }

        })

        binding.BtnSubmit.setOnClickListener(View.OnClickListener {

            var titleText = binding.Edtitle.text.toString()
            var descText = binding.description.text.toString()
            titleText = titleText.replace(" ","")
            descText = descText.replace(" ","")

            if(titleText.length > 0 && descText.length > 0)
            {
                SendFeedBack(binding.Edtitle.text.toString(),binding.description.text.toString(),isFiile)
            }
            else
            {
                Toast.makeText(this,"Please enter title and descrption",Toast.LENGTH_SHORT).show()
            }


        })


        binding.BtnClose.setOnClickListener(View.OnClickListener {
            binding.SelectedImageContainer.visibility = View.GONE
            binding.addImageContainer.visibility = View.VISIBLE


        })
        var cryptLib = CryptLib()

        repository.feedback.observe(this, Observer {
            try {
                var str = cryptLib.decryptCipherTextWithRandomIV(it.string(), SECRETE_KEY)
                val gson = Gson()
                val userType: Type = object : TypeToken<FeedBackData>() {}.type
                val feedBackData: FeedBackData = gson.fromJson(str, userType)

                if(feedBackData.status_code != 0)
                {
                    Toast.makeText(this@FeedBackDialog,"Thanks for your feedback!",Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                finish()
            }
        })





    }

    fun SendFeedBack(fb_title:String,fb_desc:String,is_File:Boolean)
    {

        GlobalScope.launch(Dispatchers.IO) {

            var cryptLib = CryptLib()
            val header:String =  "" +cryptLib.encryptPlainTextWithRandomIV(AppUtitlty.GetCureentTimeStamp(),SECRETE_KEY)

            if(!is_File)
            repository.SendFeedBackWithoutFile(header,fb_title,fb_desc)
            else
                repository.SendFeedBack(header,fb_title,fb_desc,file!!)
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            var isPermissionGranted = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
              //  Log.e("@@@@@","onRequestPermissionsResult=====TIRAMISU")
                if (AppUtitlty.checkSelfPermission(this, Manifest.permission.ACCESS_MEDIA_LOCATION) &&
                    AppUtitlty.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)) {
                    isPermissionGranted = true
               //     Log.e("@@@@@","onRequestPermissionsResult=====TIRAMISU==in==")
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
             //   Log.e("@@@@@","onRequestPermissionsResult=====Q")
                if (AppUtitlty.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) &&
                    AppUtitlty.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ) {
                    isPermissionGranted = true
                //    Log.e("@@@@@","onRequestPermissionsResult=====Q==in==")
                }
            } else {
             //   Log.e("@@@@@","onRequestPermissionsResult=====else")
                if (AppUtitlty.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) &&
                    AppUtitlty.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    isPermissionGranted = true
               //     Log.e("@@@@@","onRequestPermissionsResult=====else===in")
                }
            }
            if (isPermissionGranted) {

                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "image/*"
                startActivityForResult(
                    Intent.createChooser(intent, "Select Picture"),
                    103
                )

            } else {
                Toast.makeText(this, resources.getString(R.string.please_allow_storage), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        try {
            if (resultCode === RESULT_OK) {
                if (requestCode === 103) {
                    var selectedImageUri: Uri? = data!!.data
                    // Get the path from the Uri
                    val path = AppUtitlty.getPathFromURI(this@FeedBackDialog,selectedImageUri!!)
                    if (path != null) {
                        val f = File(path)
                        binding.addImageContainer.visibility = View.GONE
                        binding.SelectedImageContainer.visibility = View.VISIBLE
                       binding.SelectedImage.setImageURI(selectedImageUri)
                        isFiile = true
                        file = f
                     //   Log.e("@@@@@","=====f.absolutePath========> "+f.absolutePath)
                        // selectedImageUri = Uri.fromFile(f)
                    }

                }
            }
        } catch (e: Exception) {
            Log.e("FileSelectorActivity", "File select error", e)
        }
    }




}