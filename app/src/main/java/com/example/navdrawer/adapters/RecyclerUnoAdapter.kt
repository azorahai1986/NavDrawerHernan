package com.example.navdrawer.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.navdrawer.R
import com.example.navdrawer.modelos_de_datos.ModeloDeIndumentaria
import kotlinx.android.synthetic.main.item_productos.view.*

class RecyclerUnoAdapter(var mutableListModel: ArrayList<ModeloDeIndumentaria>, val activity:FragmentActivity): RecyclerView.Adapter<RecyclerUnoAdapter.ViewHolderModel>() {

    inner class ViewHolderModel (itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerUnoAdapter.ViewHolderModel =
        ViewHolderModel(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_productos, parent, false))

    override fun getItemCount(): Int = mutableListModel.size
    // Enlazar ViewHolder
    override fun onBindViewHolder(holder: RecyclerUnoAdapter.ViewHolderModel, position: Int) {
        val modelosFb = mutableListModel[position]
        holder.itemView.textview_producto.text = modelosFb.nombre
        holder.itemView.textview_precio.text = modelosFb.precio
        holder.itemView.textview_descripcion.text = modelosFb.cate
        holder.itemView.textview_sub.text = modelosFb.sub
        Glide.with(activity).load(modelosFb.imagen).into(holder.itemView.imageview)

    }


}