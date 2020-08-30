package com.example.navdrawer.enlace_con_firebase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.navdrawer.adapters.PagerPrincipalAdapter
import com.example.navdrawer.modelos_de_datos.CartelPrincipal
import com.example.navdrawer.modelos_de_datos.ModeloDeIndumentaria
import com.google.firebase.firestore.FirebaseFirestore

class Repo {
    fun getUserData(): LiveData<MutableList<ModeloDeIndumentaria>> {

        val mutableData = MutableLiveData<MutableList<ModeloDeIndumentaria>>()
        FirebaseFirestore.getInstance().collection("ModeloDeIndumentaria")
            .get().addOnSuccessListener {

                val listData = mutableListOf<ModeloDeIndumentaria>()

                for (obtenerFireBase in it.documents){
                    val indument = obtenerFireBase.toObject(ModeloDeIndumentaria::class.java)
                    indument?.id = obtenerFireBase.id
                    if (indument != null)

                        listData.add(indument)

                }
                mutableData.value = listData

            }.addOnFailureListener {
                Log.e("ErrorMODELO", it.toString())
                //Esto lo hice para probar si llega internet a la app.
            }
        return mutableData
    }

    fun getOfertas():LiveData<MutableList<CartelPrincipal>>{
        val mutableDatabaseOfertas = MutableLiveData<MutableList<CartelPrincipal>>()
        FirebaseFirestore.getInstance().collection("NuevoModelo").get()
            .addOnSuccessListener {
                val listDataOfertas = mutableListOf<CartelPrincipal>()

                for (ofertas in it.documents){
                    val o = ofertas.toObject(CartelPrincipal::class.java)
                    o?.id = ofertas.id
                    if (o != null){
                        listDataOfertas.add(o)
                    }
                }
                mutableDatabaseOfertas.value = listDataOfertas
            }.addOnFailureListener { Log.e("ErrorMODELO", it.toString()) }
        return mutableDatabaseOfertas
    }

}