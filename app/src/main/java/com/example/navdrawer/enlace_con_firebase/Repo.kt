package com.example.navdrawer.enlace_con_firebase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

}