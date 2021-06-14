package com.example.navdrawer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.navdrawer.R
import com.example.navdrawer.modelos_de_datos.PagerSimilares
import kotlinx.android.synthetic.main.item_pager_similares.view.*
import java.math.BigDecimal
import java.math.RoundingMode

class PagerSimilaresAdapter(var similaresArray:ArrayList<PagerSimilares>, val activity:FragmentActivity):
    RecyclerView.Adapter<PagerSimilaresAdapter.SimilaresViewHolder>() {

    inner class SimilaresViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerSimilaresAdapter.SimilaresViewHolder=
        SimilaresViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_pager_similares, parent, false))


    override fun getItemCount(): Int= similaresArray.size

    override fun onBindViewHolder(holder: PagerSimilaresAdapter.SimilaresViewHolder, position: Int) {
        val arraySimilar= similaresArray[position]

        val precio = arraySimilar.precio.toDouble()
        val redondeo = BigDecimal(precio).setScale(2, RoundingMode.HALF_EVEN)

        holder.itemView.text_nombre_similar.text = arraySimilar.nombre
        holder.itemView.text_precio_similar.text = " $ $redondeo x unidad"
        holder.itemView.text_sub_similar.text = arraySimilar.marca
        Glide.with(activity).load(arraySimilar.imagen).into(holder.itemView.imageView_similar)
    }
}