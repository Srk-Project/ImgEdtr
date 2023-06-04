package com.example.simpleeditor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleeditor.R
import com.example.simpleeditor.models.FeaturesModel

class FeaturesAdapter(private var mList: List<FeaturesModel>, private var mContext:Context) : RecyclerView.Adapter<FeaturesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturesAdapter.ViewHolder {
        val view=LayoutInflater.from(mContext).inflate(R.layout.model_features,parent,false)
        return ViewHolder(view)

           }

    override fun onBindViewHolder(holder: FeaturesAdapter.ViewHolder, position: Int) {
        val item= mList[position]
        holder.setAll(item.img,item.name)

    }

    override fun getItemCount(): Int {
        return mList.size;
    }
    class ViewHolder(itemView:View) :RecyclerView.ViewHolder(itemView){
        private val img:ImageView =itemView.findViewById(R.id.img_features)
        private val txt:TextView =itemView.findViewById(R.id.txt_features)

        fun setAll(image:Int,t:String){
            img.setImageResource(image)
            txt.text = t

        }


    }
}


