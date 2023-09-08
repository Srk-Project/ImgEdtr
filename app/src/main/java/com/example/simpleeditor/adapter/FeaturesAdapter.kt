package com.example.simpleeditor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleeditor.R
import com.example.simpleeditor.fragments.IClickHandle
import com.example.simpleeditor.models.FeaturesModel
import com.google.android.material.slider.RangeSlider

class FeaturesAdapter(private var mList: List<FeaturesModel>, private var mContext:Context,private var iClick:IClickHandle) : RecyclerView.Adapter<FeaturesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturesAdapter.ViewHolder {
        val view=LayoutInflater.from(mContext).inflate(R.layout.model_features,parent,false)
        return ViewHolder(view)

           }

    override fun onBindViewHolder(holder: FeaturesAdapter.ViewHolder, position: Int) {
        val item= mList[position]
        holder.setAll(item.img,item.name,item.isRequired,item.min,item.max,item.start,item.step)
        holder.cardView.setOnClickListener {
            iClick.clickHandle(item.isRequired,item.min,item.max,item.start,holder.cardView,holder.txt,holder.rangeSlider,item.step)
        }

    }

    override fun getItemCount(): Int {
        return mList.size;
    }
    class ViewHolder(itemView:View) :RecyclerView.ViewHolder(itemView){
        private val img:ImageView =itemView.findViewById(R.id.img_features)
         var txt:TextView =itemView.findViewById(R.id.txt_features)
         var cardView:CardView=itemView.findViewById(R.id.cv_img_features)
        var rangeSlider:RangeSlider=itemView.findViewById(R.id.seekbar)


        fun setAll(image:Int,t:String,isReq:Boolean,min:Float,max:Float,start:Float,step:Float){
            if (isReq){
                rangeSlider.valueFrom=min
                rangeSlider.valueTo=max
                rangeSlider.stepSize=step
               rangeSlider.values= listOf(start,start)

            }

            img.setImageResource(image)
            txt.text = t

        }


    }
}


