package com.example.simpleeditor.fragments

import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.android.material.slider.RangeSlider

interface IClickHandle {
    fun clickHandle(isReq:Boolean,min:Float,max:Float,start:Float,cardView:CardView,tv:TextView,rangeSlider:RangeSlider,step:Float)
}