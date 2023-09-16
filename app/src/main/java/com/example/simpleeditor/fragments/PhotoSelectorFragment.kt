package com.example.simpleeditor.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.simpleeditor.databinding.FragmentPhotoSelectorBinding
import com.example.simpleeditor.viewModel.ImageViewModel


class PhotoSelectorFragment : DialogFragment() {
    private  var photoSelectorBinding: FragmentPhotoSelectorBinding?=null
    private lateinit var imageViewModel: ImageViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        photoSelectorBinding = FragmentPhotoSelectorBinding.inflate(inflater, container, false)
        return photoSelectorBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        clickOperations(view)
    }

    private fun initView(view: View) {
        imageViewModel= ViewModelProvider(requireActivity())[ImageViewModel::class.java]

    }


    private fun clickOperations(view: View) {
        photoSelectorBinding?.consGallery?.setOnClickListener {
            val intent=Intent(Intent.ACTION_PICK).apply {
                type="image/*"
            }

            galleryLauncher.launch(intent)

        }

        photoSelectorBinding?.consCamera?.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        }

    }
    private var cameraLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
        if (result.resultCode==Activity.RESULT_OK){
            val data:Uri?=result.data?.data
            if (data!=null){
                imageViewModel.setUriOfImg(data)
            }
            dismiss()
        }

    }

    private var galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if (result.resultCode==Activity.RESULT_OK){
            val data:Uri?=result.data?.data


            if (data != null) {
                imageViewModel.setUriOfImg(data)
            }
            dismiss()


        }


    }

    override fun onDestroy() {
        super.onDestroy()
        photoSelectorBinding=null
    }

}