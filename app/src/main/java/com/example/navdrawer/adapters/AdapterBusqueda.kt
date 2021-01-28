package com.example.navdrawer.adapters

import android.util.Log
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
import kotlinx.android.synthetic.main.item_recycler_busqueda.view.*

class AdapterBusqueda(var arrayModelBudqueda:ArrayList<ModeloDeIndumentaria>, val activity:FragmentActivity):RecyclerView.Adapter<AdapterBusqueda.ViewHolder>() {

    var arrayFiltro: ArrayList<ModeloDeIndumentaria> = ArrayList()

    fun setData(datos: ArrayList<ModeloDeIndumentaria>){
        arrayModelBudqueda = datos
        arrayFiltro = ArrayList(arrayModelBudqueda)

    }
    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterBusqueda.ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recycler_busqueda, parent, false))

    override fun getItemCount(): Int = arrayFiltro.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val modelosFb = arrayFiltro[position]
        indexModelo(modelosFb)

        holder.itemView.textview_nombre_busqueda.text = modelosFb.nombre + " "+modelosFb.marca
        holder.itemView.textview_precio_busqueda.text = " $ " + modelosFb.precio
        holder.itemView.textview_marca_busqueda.text = modelosFb.marca
        Glide.with(activity).load(modelosFb.imagen).into(holder.itemView.imageview_busqueda)

        holder.itemView.cardview_busqueda.setOnClickListener{
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, VerImagenFragment.newInstanceBusqueda(modelosFb.imagen, modelosFb.nombre, modelosFb.marca,"$"+modelosFb.precio, modelosFb.id))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(
                    VerImagenFragment.IMAGENRECIBIDABUSQUEDA).commit()
        }
    }
    fun indexModelo(dat:ModeloDeIndumentaria): Int{
        for (i in 0 until arrayModelBudqueda.size){
            if(arrayModelBudqueda[i].id == dat.id)
                return i
            Log.e("return INfo", i.toString())
        }
        return 0
        Log.e("return 0", 0.toString())
    }
    fun filtrado(editFiltro: String){
        Log.e("return INfo", editFiltro)

        if (editFiltro.isNotEmpty()) {
            arrayFiltro = ArrayList()
            for (d in arrayModelBudqueda) {
                if (editFiltro in d.nombre) {
                    arrayFiltro.add(d)
                }

            }

        } else{
            //arrayFiltro = ArrayList(arrayModelBudqueda)
        }
        notifyDataSetChanged()

    }



}