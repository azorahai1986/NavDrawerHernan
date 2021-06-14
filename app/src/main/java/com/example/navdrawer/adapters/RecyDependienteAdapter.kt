package com.example.navdrawer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.navdrawer.R
import com.example.navdrawer.modelos_de_datos.Dependiente
import kotlinx.android.synthetic.main.item_dependiente.view.*
import java.math.BigDecimal
import java.math.RoundingMode

class RecyDependienteAdapter(var arrayDependiente:ArrayList<Dependiente>, val fragment:FragmentActivity):RecyclerView.Adapter<RecyDependienteAdapter.DepenViewHolder>() {

    inner class DepenViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepenViewHolder=
        DepenViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_dependiente, parent, false))
    override fun getItemCount(): Int = arrayDependiente.size

    override fun onBindViewHolder(holder: DepenViewHolder, position: Int) {
        val arrayDep = arrayDependiente[position]
        val redondeo = BigDecimal(arrayDep.precio).setScale(2, RoundingMode.HALF_EVEN)

        holder.itemView.text_dependiente_producto.text = arrayDep.nombre + "  " +arrayDep.marca
        holder.itemView.text_dependiente_precio.text = redondeo.toString()
        holder.itemView.text_dependiente_marca.text = arrayDep.marca
        Glide.with(fragment).load(arrayDep.imagen).into(holder.itemView.imageview_dependiente)



    }



}
