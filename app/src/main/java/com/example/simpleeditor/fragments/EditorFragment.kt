package com.example.simpleeditor.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.simpleeditor.R
import com.example.simpleeditor.databinding.FragmentEditorBinding

class EditorFragment : Fragment() {

    private lateinit var editorBinding: FragmentEditorBinding
    private lateinit var navController: NavController



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        editorBinding =FragmentEditorBinding.inflate(inflater,container,false)
        return editorBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController=Navigation.findNavController(view)
        navController.navigate(R.id.action_editorFragment_to_featuresBottomSheetFragment2)
        editorBinding.img.setOnClickListener {
            navController.navigate(R.id.action_editorFragment_to_featuresBottomSheetFragment2)

        }


    }

}