package com.example.navdrawer.adapters

import android.app.AlertDialog
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.navdrawer.R
import com.example.navdrawer.clases.AplicarPorcentajesPrecios
import com.example.navdrawer.clases.ModificarFirebase
import com.example.navdrawer.fragmentos.dependienteFragment
import com.example.navdrawer.modelos_de_datos.SubCategorias
import kotlinx.android.synthetic.main.dialog_porcentaje.view.*
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
    override fun onBindViewHolder(holder: AdapterPorcenSubCate.ViewHolderModel, position: Int) {
        val modelSubPorcen = mutableListSub[position]
        holder.itemView.text_descripcion_sub.text = modelSubPorcen.marca
        holder.itemView.textview_pocen.text = modelSubPorcen.porcentaje
        Glide.with(activity).load(modelSubPorcen.imagen).into(holder.itemView.imageView_sub)

        var porcenText = holder.itemView.textview_pocen
        if (!modelSubPorcen.porcentaje.isNullOrEmpty()){
            holder.itemView.cardView_sub_porcen.setBackgroundResource(R.color.design_default_color_surface)
            //porcenText.text = editPorcen.text
            porcenText.text = modelSubPorcen.porcentaje + " %"
           
        }else{
            holder.itemView.cardView_sub_porcen.setBackgroundResource(R.color.WhiteColor)
            porcenText.text = "0 %"


        }

        holder.itemView.cardView_sub_porcen.setOnClickListener {
            dialogPorcentaje(modelSubPorcen.marca, modelSubPorcen.id)
        }

        /*var editPorcen = holder.itemView.ed_text_Porcen
        var porcenText = holder.itemView.textview_pocen
        val porcenDialog = LayoutInflater.from(activity).inflate(R.layout.dialog_porcentaje, null)*/




//        Log.e("ArrayValor", arrayDouble[position].toString())
        /*
        NOTA: ESTO ME SERVIRÁ PARA CUANDO QUIERO SELECCIONAR UN ITEM Y QUE NO SE
        REPITAN LOS VALORES AL SCROLLEAR

        if (modelSubPorcen.check && modelSubPorcen.valor){
            holder.itemView.cardView_sub_porcen.setBackgroundResource(R.color.gris)
            //porcenText.text = editPorcen.text
            porcenText.text = porcenDialog.edit_porcen.text

        }else{
            holder.itemView.cardView_sub_porcen.setBackgroundResource(R.color.WhiteColor)
            porcenText.text = "hola"
            //editPorcen.visibility = View.GONE
            //porcenText.visibility = View.VISIBLE

        }


        holder.itemView.cardView_sub_porcen.setOnLongClickListener {
            if (modelSubPorcen.check && modelSubPorcen.valor){
                holder.itemView.cardView_sub_porcen.setBackgroundResource(R.color.WhiteColor)

                editPorcen.visibility = View.GONE
                porcenText.visibility = View.VISIBLE


            }else{
                holder.itemView.cardView_sub_porcen.setBackgroundResource(R.color.gris)
                editPorcen.visibility = View.VISIBLE
                porcenText.visibility = View.GONE
                porcenText.text = editPorcen.text
                porcenText.text = porcenDialog.edit_porcen.text


            }
            modelSubPorcen.check = !modelSubPorcen.check
            modelSubPorcen.valor = !modelSubPorcen.valor
            mutableListSub[position] = modelSubPorcen


            true

        }*/



    }

    private fun dialogPorcentaje(marca: String, id: String) {

        val dialog = LayoutInflater.from(activity).inflate(R.layout.dialog_porcentaje, null)
        val builder = AlertDialog.Builder(activity).setView(dialog)
        val alertDialog = builder.show()

        var editValor = dialog.edittext_dialog_porcen.text
        dialog.tv_dialog_marca.text = marca




            dialog.bt_dialog_aceptar.setOnClickListener {
                if (editValor.isNullOrEmpty()){
                    val toast = Toast.makeText(activity, "Ingresa un valor", Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.HORIZONTAL_GRAVITY_MASK or Gravity.CENTER_HORIZONTAL, 0, 0)
                    toast.show()
                }else{
                    porcentajes(editValor.toString(), id)

                    irSubCateFragment(id, marca)
                    alertDialog.dismiss()

                    val aplicarPorcen = AplicarPorcentajesPrecios()
                    aplicarPorcen.userData(editValor.toString(), marca)
                }



            }



        dialog.bt_dialog_cancelar.setOnClickListener {
            alertDialog.dismiss()

        }

        //
    }
    fun porcentajes(porcen: String, id: String){
        val por = porcen + porcen.toDouble()/100

        val modificar = ModificarFirebase()
        modificar.modificarPorcentajes(porcen, id)

    }

    private fun irSubCateFragment(id: String, marca: String) {
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, dependienteFragment.newInstance(marca, id))
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .addToBackStack(dependienteFragment.VOLVERPORCENTAJE)
            .commit()

    }

}