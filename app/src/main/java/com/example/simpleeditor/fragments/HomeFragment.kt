package com.example.simpleeditor.fragments

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View                               
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.example.simpleeditor.R
import com.example.simpleeditor.databinding.FragmentHomeBinding
import com.example.simpleeditor.reusable.Constants
import com.example.simpleeditor.viewModel.ImageViewModel
import kotlin.concurrent.thread


class HomeFragment : Fragment() {
    private  var homeBinding: FragmentHomeBinding?=null
    private lateinit var navController: NavController
    private lateinit var imageViewModel: ImageViewModel
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        homeBinding=FragmentHomeBinding.inflate(inflater,container,false)

        return homeBinding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        intiView(view)
        val navOptions:NavOptions = NavOptions.Builder().setPopUpTo(R.id.homeFragment,true)
            .build()
        observer(navOptions)
        clickOperations()








    }

    private fun clickOperations() {
        homeBinding?.group?.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_photoSelectorFragment)
        }
    }

    private fun observer(navOptions: NavOptions) {
        val observer:Observer<Uri?> = Observer {
            if(it!=null){
                var bundle=Bundle()
                bundle.putString("uri",it.toString());

                try {
                navController.navigate(R.id.action_homeFragment_to_editorFragment,bundle,navOptions)
                }catch (ex:Exception){

                }

                sharedPreferences.edit()
                    .putString(Constants.GLOBAL_PHOTO_NAME,"new")
                    .apply()
            }
        }
        imageViewModel.getUriOfImg().observe(requireActivity(),observer)


    }


    private fun intiView(view: View) {
        navController=Navigation.findNavController(view)
        imageViewModel= ViewModelProvider(requireActivity())[ImageViewModel::class.java]
        sharedPreferences=requireActivity().getSharedPreferences(Constants.GLOBAL_SHARED_PREFERENCES,MODE_PRIVATE)


    }

    override fun onDestroy() {
        super.onDestroy()
        homeBinding=null
    }



}