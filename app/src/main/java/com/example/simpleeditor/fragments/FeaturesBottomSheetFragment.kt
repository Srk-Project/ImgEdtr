package com.example.simpleeditor.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.simpleeditor.R
import com.example.simpleeditor.adapter.FeaturesAdapter
import com.example.simpleeditor.databinding.FragmentFeaturesBottomSheetBinding
import com.example.simpleeditor.models.FeaturesModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FeaturesBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var bottomSheetBinding: FragmentFeaturesBottomSheetBinding
    private lateinit var adapters: FeaturesAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NO_FRAME, R.style.MyBottomSheetTheme)
        return super.onCreateDialog(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bottomSheetBinding = FragmentFeaturesBottomSheetBinding.inflate(inflater, container, false)
        dialog?.requestWindowFeature(STYLE_NO_TITLE)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return bottomSheetBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        handleRecView()

    }

    private fun handleRecView() {

     /* //  adapters = FeaturesAdapter(getList(), requireActivity())
        bottomSheetBinding.recView.adapter = adapters
        val gridLayoutManager = GridLayoutManager(requireActivity(), 5)
        bottomSheetBinding.recView.layoutManager = gridLayoutManager
*/

    }

    private fun getList(): List<FeaturesModel> {
        val lists: MutableList<FeaturesModel> = ArrayList<FeaturesModel>()

        lists.add(FeaturesModel(R.drawable.baseline_save_as_24, "Filter",true,-60,180,50))
        lists.add(FeaturesModel(R.drawable.baseline_add_a_photo_24, "Filter1",true,-60,180,50))
        lists.add(FeaturesModel(R.drawable.baseline_camera_alt_24, "Filter2",true,-60,180,50))
        lists.add(FeaturesModel(R.drawable.baseline_gallery, "Filter3",true,-60,180,50))
        lists.add(FeaturesModel(R.drawable.baseline_gallery, "Filter4",true,-60,180,50))
        lists.add(FeaturesModel(R.drawable.baseline_save_as_24, "Filter",true,-60,180,50))
        lists.add(FeaturesModel(R.drawable.baseline_add_a_photo_24, "Filter1",true,-60,180,50))
        lists.add(FeaturesModel(R.drawable.baseline_camera_alt_24, "Filter2",true,-60,180,50))
        lists.add(FeaturesModel(R.drawable.baseline_gallery, "Filter3",true,-60,180,50))
        lists.add(FeaturesModel(R.drawable.baseline_gallery, "Filter4",true,-60,180,50))
        lists.add(FeaturesModel(R.drawable.baseline_save_as_24, "Filter",true,-60,180,50))



        return lists


    }


    private fun initView(view: View) {


    }

}