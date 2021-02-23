package com.example.navdrawer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.navdrawer.R
import com.example.navdrawer.fragmentos.SubCateFragment
import com.example.navdrawer.modelos_de_datos.Categorias
import kotlinx.android.synthetic.main.item_categorias.view.*

class CategoriasAdapter(var arrayCategorias:MutableList<Categorias>, val fragment:FragmentActivity):RecyclerView.Adapter<CategoriasAdapter.CateViewHolder>() {
    inner class CateViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CateViewHolder =
        CateViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_categorias, parent, false))

    override fun getItemCount(): Int = arrayCategorias.size

    override fun onBindViewHolder(holder: CateViewHolder, position: Int) {
        val categoriasArray = arrayCategorias[position]
        holder.itemView.text_item_categorias.text = categoriasArray.cate
        holder.itemView.text_item_categorias_arriba.text = categoriasArray.cate
        Glide.with(fragment).load(categoriasArray.imagen).into(holder.itemView.imagen_item_categorias)

        holder.itemView.cardview_categorias.setOnClickListener {
            fragment.supportFragmentManager.beginTransaction().replace(R.id.frame_layout, SubCateFragment.newInstance(categoriasArray.id))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).addToBackStack(SubCateFragment.VOLVER).commit()
        }


    }
}