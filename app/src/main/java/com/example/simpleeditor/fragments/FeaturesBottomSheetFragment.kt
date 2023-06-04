package com.example.simpleeditor.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.simpleeditor.R
import com.example.simpleeditor.adapter.FeaturesAdapter
import com.example.simpleeditor.databinding.FragmentFeaturesBottomSheetBinding
import com.example.simpleeditor.models.FeaturesModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FeaturesBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var bottomSheetBinding: FragmentFeaturesBottomSheetBinding
    private lateinit var adapters: FeaturesAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bottomSheetBinding=FragmentFeaturesBottomSheetBinding.inflate(inflater,container,false)
        return bottomSheetBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view);
        handleRecView();

    }

    private fun handleRecView() {

        adapters= FeaturesAdapter(getList(),requireActivity())
        bottomSheetBinding.recView.adapter=adapters
        val gridLayoutManager= GridLayoutManager(requireActivity(),5)
        bottomSheetBinding.recView.layoutManager=gridLayoutManager


    }

    private fun getList(): List<FeaturesModel> {
      var lists: MutableList<FeaturesModel> =ArrayList<FeaturesModel>()
        lists.add(FeaturesModel(R.drawable.baseline_save_as_24,"Filter"))
        lists.add(FeaturesModel(R.drawable.baseline_save_as_24,"Filter"))
        lists.add(FeaturesModel(R.drawable.baseline_save_as_24,"Filter"))
        lists.add(FeaturesModel(R.drawable.baseline_save_as_24,"Filter"))
        lists.add(FeaturesModel(R.drawable.baseline_save_as_24,"Filter"))
        lists.add(FeaturesModel(R.drawable.baseline_save_as_24,"Filter"))
        lists.add(FeaturesModel(R.drawable.baseline_save_as_24,"Filter"))
        lists.add(FeaturesModel(R.drawable.baseline_save_as_24,"Filter"))
        lists.add(FeaturesModel(R.drawable.baseline_save_as_24,"Filter"))
        lists.add(FeaturesModel(R.drawable.baseline_save_as_24,"Filter"))
        lists.add(FeaturesModel(R.drawable.baseline_save_as_24,"Filter"))


return lists


    }



    private fun initView(view: View) {


    }

}