package com.example.navdrawer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.navdrawer.R
import com.example.navdrawer.fragmentos.dependienteFragment
import com.example.navdrawer.modelos_de_datos.SubCategorias
import kotlinx.android.synthetic.main.item_subcate.view.*

class RecyclerSubcate(var mutableListSub: ArrayList<SubCategorias>, val activity:FragmentActivity): RecyclerView.Adapter<RecyclerSubcate.ViewHolderModel>() {

    inner class ViewHolderModel (itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerSubcate.ViewHolderModel =
        ViewHolderModel(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_subcate, parent, false))

    override fun getItemCount(): Int = mutableListSub.size
    // Enlazar ViewHolder
    override fun onBindViewHolder(holder: RecyclerSubcate.ViewHolderModel, position: Int) {
        val modelosFb = mutableListSub[position]

        holder.itemView.text_descripcion_sub.text = modelosFb.marca
        Glide.with(activity).load(modelosFb.imagen).into(holder.itemView.imageView_sub)

        holder.itemView.imageView_sub.setOnClickListener{
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, dependienteFragment.newInstance(modelosFb.marca, modelosFb.id, modelosFb.porcentaje))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(dependienteFragment.VOLVER).commit()
        }

    }




}