package com.example.navdrawer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.navdrawer.R
import kotlinx.android.synthetic.main.item_pager_zoom.view.*

class PagerZoomAdapter(var arrayZoom:ArrayList<String>, val fragment:FragmentActivity):RecyclerView.Adapter<PagerZoomAdapter.ZoomViewHolder>() {

    inner class ZoomViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ZoomViewHolder =
        ZoomViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_pager_zoom, parent, false))

    override fun getItemCount(): Int = arrayZoom.size

    override fun onBindViewHolder(holder: ZoomViewHolder, position: Int) {
        val zoom = arrayZoom[position]


        //holder.itemView.imagenZoom.setImageResource(zoom.toInt())
        Glide.with(fragment.applicationContext).load(zoom).into(holder.itemView.imagenZoom)

    }

}