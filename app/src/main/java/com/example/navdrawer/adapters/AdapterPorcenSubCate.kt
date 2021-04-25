package com.example.navdrawer.adapters

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.navdrawer.R
import com.example.navdrawer.modelos_de_datos.SubCategorias
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_porcen_sub.view.*
import kotlinx.android.synthetic.main.item_subcate.view.imageView_sub
import kotlinx.android.synthetic.main.item_subcate.view.text_descripcion_sub

class AdapterPorcenSubCate(var mutableListSub: ArrayList<SubCategorias>, val activity:FragmentActivity): RecyclerView.Adapter<AdapterPorcenSubCate.ViewHolderModel>() {


    inner class ViewHolderModel (itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterPorcenSubCate.ViewHolderModel =
        ViewHolderModel(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_porcen_sub, parent, false))

    override fun getItemCount(): Int = mutableListSub.size
    // Enlazar ViewHolder
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: AdapterPorcenSubCate.ViewHolderModel, position: Int) {
        val modelSubPorcen = mutableListSub[position]
        holder.itemView.text_descripcion_sub.text = modelSubPorcen.marca
        Glide.with(activity).load(modelSubPorcen.imagen).into(holder.itemView.imageView_sub)

        var editPorcen = holder.itemView.ed_text_Porcen
        var porcenText = holder.itemView.textview_pocen
        val porcenDialog = LayoutInflater.from(activity).inflate(R.layout.dialog_porcentaje, null)

//        Log.e("ArrayValor", arrayDouble[position].toString())
        if (modelSubPorcen.check && modelSubPorcen.valor){
            holder.itemView.cardView_sub_porcen.setBackgroundResource(R.color.gris)
            porcenText.text = editPorcen.text

        }else{
            holder.itemView.cardView_sub_porcen.setBackgroundResource(R.color.WhiteColor)
            porcenText.text = "hola"

        }



        holder.itemView.cardView_sub_porcen.setOnLongClickListener {
            if (modelSubPorcen.check && modelSubPorcen.valor){
                holder.itemView.cardView_sub_porcen.setBackgroundResource(R.color.WhiteColor)

            }else{
                holder.itemView.cardView_sub_porcen.setBackgroundResource(R.color.gris)
                editPorcen.visibility = View.VISIBLE
                porcenText.visibility = View.GONE
                porcenText.text = editPorcen.text


                //dialogPorcentaje(porcenText)

            }
            modelSubPorcen.check = !modelSubPorcen.check
            modelSubPorcen.valor = !modelSubPorcen.valor
            mutableListSub[position] = modelSubPorcen


            true

        }

        Log.e("ArrayValor 2", modelSubPorcen.valor.toString())








        /*holder.itemView.imageView_sub.setOnClickListener{
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, dependienteFragment.newInstance(modelosFb.marca, modelosFb.id))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(dependienteFragment.VOLVER).commit()
        }*/

    }

    fun porcentajes(idPorcen:String){

        var map = mutableMapOf<String, Any>()
        map["porcentaje"] = idPorcen
        val editar = FirebaseFirestore.getInstance().collection("Subcatergoria")
            .document()
        editar.update(map)
            .addOnSuccessListener {
                Toast.makeText(activity, "Producto Modificado con exito", Toast.LENGTH_SHORT) .show()
            }.addOnFailureListener {
                Toast.makeText(activity, "Falló Modificación", Toast.LENGTH_SHORT).show()

            }
    }

    /*fun dialogPorcentaje(porcenText: TextView) {
        //Log.e("arrayPorcen", id.toString())

        val porcenDialog = LayoutInflater.from(activity).inflate(R.layout.dialog_porcentaje, null)
        val constructor = AlertDialog.Builder(activity).setView(porcenDialog)

        val alertDialog = constructor.show()
        porcenDialog.bt_aceptar.setOnClickListener {
            porcenText.text = porcenDialog.edit_porcen.text
            alertDialog.dismiss()

        }

        porcenDialog.bt_cancelar.setOnClickListener {
            alertDialog.dismiss()
        }

    }*/
}