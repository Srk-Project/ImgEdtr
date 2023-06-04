package com.example.simpleeditor.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.simpleeditor.R
import com.example.simpleeditor.databinding.FragmentPhotoSelectorBinding


class PhotoSelectorFragment : DialogFragment() {
    private lateinit var photoSelectorBinding: FragmentPhotoSelectorBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        photoSelectorBinding = FragmentPhotoSelectorBinding.inflate(inflater, container, false)
        return photoSelectorBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickOperations(view)




    }


    private fun clickOperations(view: View) {
        photoSelectorBinding.consGallery.setOnClickListener {
            val intent=Intent(Intent.ACTION_PICK).apply {
                type= "image/*"

            }
            dismiss()
            startActivity(intent)
        }

        photoSelectorBinding.consCamera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            dismiss()
            startActivity(intent)

        }

    }

}