package com.example.simpleeditor.fragments

import android.widget.TextView
import androidx.cardview.widget.CardView

interface IClickHandle {
    fun clickHandle(isReq:Boolean,min:Int,max:Int,start:Int,cardView:CardView,tv:TextView)
}