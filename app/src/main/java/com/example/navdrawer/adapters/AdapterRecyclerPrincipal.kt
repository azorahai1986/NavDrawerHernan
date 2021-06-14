package com.example.navdrawer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.navdrawer.R
import com.example.navdrawer.fragmentos.VerImagenFragment
import com.example.navdrawer.modelos_de_datos.ModeloDeIndumentaria
import kotlinx.android.synthetic.main.item_productos.view.*
import java.math.BigDecimal
import java.math.RoundingMode

class AdapterRecyclerPrincipal(var mutableListModel: ArrayList<ModeloDeIndumentaria>, val activity:FragmentActivity): RecyclerView.Adapter<AdapterRecyclerPrincipal.ViewHolderModel>() {

    var arrayFiltro: ArrayList<ModeloDeIndumentaria> = ArrayList()
    fun setData(datos: ArrayList<ModeloDeIndumentaria>){
        mutableListModel = datos
        arrayFiltro = ArrayList(mutableListModel)

    }

    inner class ViewHolderModel (itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterRecyclerPrincipal.ViewHolderModel =
        ViewHolderModel(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_productos, parent, false))

    override fun getItemCount(): Int = arrayFiltro.size
    // Enlazar ViewHolder
    override fun onBindViewHolder(holder: AdapterRecyclerPrincipal.ViewHolderModel, position: Int) {
        val modelosFb = arrayFiltro[position]

        val precio = modelosFb.precio.toDouble()
        val redondeo = BigDecimal(precio).setScale(2, RoundingMode.HALF_EVEN)

        holder.itemView.textview_nombre.text = modelosFb.nombre + " "+modelosFb.marca
        holder.itemView.textview_precio.text = " $ $redondeo"
        holder.itemView.textview_marca.text = modelosFb.marca
        Glide.with(activity).load(modelosFb.imagen).into(holder.itemView.imageview)


        holder.itemView.imageview.setOnClickListener{


            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, VerImagenFragment.newInstance(
                    modelosFb.imagen, modelosFb.arrayImagen, modelosFb.nombre, modelosFb.marca,
                    "$ $redondeo", modelosFb.id))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(VerImagenFragment.IMAGENRECIBIDA).commit()
        }

    }



}
