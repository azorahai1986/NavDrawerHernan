package com.example.navdrawer.adapters

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.navdrawer.R
import kotlinx.android.synthetic.main.item_imagenescrear.view.*

class AdapterImagenCrear(var arrayImagenes: ArrayList<Uri>):RecyclerView.Adapter<AdapterImagenCrear.ImagenesViewHolder>() {

    inner class ImagenesViewHolder(itemview:View):RecyclerView.ViewHolder(itemview)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterImagenCrear.ImagenesViewHolder =
        ImagenesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_imagenescrear, parent, false))

    override fun getItemCount() = arrayImagenes.size

    override fun onBindViewHolder(holder: ImagenesViewHolder, position: Int) {
         var imagenes = arrayImagenes[position]
        Log.e("imagenadapter", imagenes.toString())

        holder.itemView.imageView_produ.setImageURI(imagenes)

    }


}