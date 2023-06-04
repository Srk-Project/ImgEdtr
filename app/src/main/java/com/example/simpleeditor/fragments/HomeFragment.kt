package com.example.simpleeditor.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.simpleeditor.R
import com.example.simpleeditor.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var homeBinding: FragmentHomeBinding
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeBinding=FragmentHomeBinding.inflate(inflater,container,false)
        return homeBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        intiView(view)

        homeBinding.group.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_photoSelectorFragment)
        }



    }

    private fun intiView(view: View) {
        navController=Navigation.findNavController(view)

    }

}