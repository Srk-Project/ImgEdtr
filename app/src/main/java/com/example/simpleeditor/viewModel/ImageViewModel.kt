package com.example.simpleeditor.viewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ImageViewModel : ViewModel(){
    private val uriImage: MutableLiveData<Uri?> = MutableLiveData<Uri?>()

     fun getUriOfImg(): MutableLiveData<Uri?> {
        return uriImage
    }

     fun setUriOfImg(uri: Uri?){
         uriImage.postValue(uri)
    }





}