package com.example.navdrawer.adapters

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.navdrawer.R
import com.example.navdrawer.fragmentos.SearchFragment
import com.example.navdrawer.fragmentos.VerImagenFragment
import com.example.navdrawer.modelos_de_datos.ModeloDeIndumentaria
import kotlinx.android.synthetic.main.dialog_ir_pdf.view.*
import kotlinx.android.synthetic.main.item_recycler_busqueda.view.*
import java.math.BigDecimal
import java.math.RoundingMode

class AdapterBusqueda(var arrayModelBudqueda:ArrayList<ModeloDeIndumentaria>, val activity:FragmentActivity, val busqueda: SearchFragment):RecyclerView.Adapter<AdapterBusqueda.ViewHolder>() {

    var arrayFiltro: ArrayList<ModeloDeIndumentaria> = ArrayList()
    var arrayCantidades: ArrayList<Int> = ArrayList()
    var arrayPecios: ArrayList<Double> = ArrayList()
    var cantidad:Int? = null

    fun setData(datos: ArrayList<ModeloDeIndumentaria>){
        arrayModelBudqueda = datos
        arrayCantidades = ArrayList()
        arrayFiltro = ArrayList(arrayModelBudqueda)

        for (d in datos){

            for(d in datos){
                arrayCantidades.add(0)
                arrayPecios.add(0.0)


            }
        }

    }
    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterBusqueda.ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recycler_busqueda, parent, false))

    override fun getItemCount(): Int = arrayFiltro.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val modelosFb = arrayFiltro[position]
        val index = indexModelo(modelosFb)

        holder.itemView.textview_nombre_busqueda.text = modelosFb.nombre + " "+modelosFb.marca
        holder.itemView.textview_precio_busqueda.text = " $ " + modelosFb.precio
       // holder.itemView.textview_marca_busqueda.text = modelosFb.marca
        Glide.with(activity).load(modelosFb.imagen).into(holder.itemView.imageview_busqueda)


        //daré funciones a los botones de agregar y restar.................................
        holder.itemView.tvValorCantidad.text = arrayCantidades[index].toString()
        holder.itemView.tv_template_cantidad.text = arrayCantidades[index].toString()
        holder.itemView.tv_template_precio.text = arrayPecios[index].toString()
        if (arrayCantidades[index] == 0)holder.itemView.tv_template_cantidad.text = "0 pedido"
        if (arrayCantidades[index] == 0)holder.itemView.tv_template_precio.text = " $ 0..."

        // boton para sumar valores............................................
        holder.itemView.btSumar.setOnClickListener {

            arrayCantidades[index] = arrayCantidades[index] + 1

            holder.itemView.tvValorCantidad.text = arrayCantidades[index].toString()
            holder.itemView.tv_template_cantidad.text = "x ${arrayCantidades[index]} unidades"
            arrayPecios[index] = arrayPecios[index] + modelosFb.precio.toDouble()

            val redondeo = BigDecimal(arrayPecios[index]).setScale(2, RoundingMode.HALF_EVEN)
            holder.itemView.tv_template_precio.text = " sub total $ ${redondeo}"

            // desde aquí envio los arrays al main activity. a la funcion obtenerdatosadapter

            busqueda.obtenerDatosAdapter(arrayCant = arrayCantidades, arrayPrec = arrayPecios)



        }

        // boton para restar los valores.....................................................
        holder.itemView.btRestar.setOnClickListener {
            if (arrayCantidades[index] > 0){
                arrayCantidades[index] = arrayCantidades[index] - 1
                holder.itemView.tvValorCantidad.text = arrayCantidades[index].toString()
                holder.itemView.tv_template_cantidad.text = "x ${arrayCantidades[index]} unidades"
                arrayPecios[index] = arrayPecios[index] - modelosFb.precio.toDouble()
                holder.itemView.tv_template_precio.text = " sub total $ ${arrayPecios[index]}"
                busqueda.obtenerDatosAdapter(arrayCant = arrayCantidades, arrayPrec = arrayPecios)
            }
        }

        holder.itemView.imageview_busqueda.setOnClickListener{
            Log.e("en el adapter1", cantidad.toString())
            if (cantidad == null){
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.cordinat, VerImagenFragment.newInstance(modelosFb.imagen, modelosFb.nombre, modelosFb.marca,"$"+modelosFb.precio, modelosFb.id))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(
                        VerImagenFragment.VOLVERBUSQUEDA).commit()
            }else{
                dialIrAVerImagen(modelosFb.imagen, modelosFb.nombre, modelosFb.marca, modelosFb.precio, modelosFb.id)
            }
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
        Log.e("editFiltro", editFiltro)


       if (editFiltro.isNotEmpty()) {
            arrayFiltro = ArrayList()
            for (d in arrayModelBudqueda) {
                if (editFiltro in d.nombre) {
                    arrayFiltro.add(d)
                }

            }

        } else{

            arrayFiltro = ArrayList(arrayModelBudqueda)
        }
        notifyDataSetChanged()

    }
    fun traerdatosParaDialog(totalCant:Int){
        cantidad = totalCant

    }

    fun dialIrAVerImagen(imagen:String, nombre:String, marca:String, precio:String, id:String){
        val dialIrVerImagen =LayoutInflater.from(activity).inflate(R.layout.dialog_ir_pdf, null)
        val constructorDialog = AlertDialog.Builder(activity).setView(dialIrVerImagen)
        // mostrar dialog.
        val alertDialog= constructorDialog.show()
        dialIrVerImagen.flbt_permanecer.setOnClickListener { alertDialog.dismiss() }
        dialIrVerImagen.flbt_avanzar.setOnClickListener {
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.cordinat, VerImagenFragment.newInstance(imagen, nombre, marca, precio, id))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(VerImagenFragment.VOLVERBUSQUEDA).commit()
            alertDialog.dismiss()
        }

    }

}