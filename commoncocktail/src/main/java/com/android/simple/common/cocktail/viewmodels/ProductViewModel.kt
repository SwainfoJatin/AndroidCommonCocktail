package com.android.simple.common.cocktail.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.android.simple.common.cocktail.api.repository.ProductRepository
import okhttp3.ResponseBody

class ProductViewModel(private val repository: ProductRepository): ViewModel() {
    /*init {
        viewModelScope.launch(Dispatchers.IO) {
           // repository.getProduct("5")
        }
    }
*/


    val feedback :LiveData<ResponseBody>
        get() = repository.feedback
}